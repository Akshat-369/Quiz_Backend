package com.quizapp.service;

import com.quizapp.model.*;
import com.quizapp.repository.RoomRepository;
import com.quizapp.repository.RoomParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomParticipantRepository participantRepository;

    private String generateUniqueRoomCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder code;
        do {
            code = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                code.append(chars.charAt(random.nextInt(chars.length())));
            }
        } while (roomRepository.existsByRoomCode(code.toString()));
        return code.toString();
    }

    @Transactional
    public Room createRoom(Quiz quiz, User host, Integer maxParticipants) {
        Room room = new Room();
        room.setRoomCode(generateUniqueRoomCode());
        room.setQuiz(quiz);
        room.setHost(host);
        room.setStatus(RoomStatus.WAITING);
        room.setMaxParticipants(maxParticipants);
        return roomRepository.save(room);
    }

    @Transactional
    public RoomParticipant joinRoom(String roomCode, User user) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (room.getStatus() != RoomStatus.WAITING) {
            throw new IllegalStateException("Room is not accepting new participants");
        }

        if (participantRepository.existsByRoomAndUser(room, user)) {
            throw new IllegalStateException("User already joined this room");
        }

        List<RoomParticipant> activeParticipants = participantRepository.findByRoomAndIsActiveTrue(room);
        if (activeParticipants.size() >= room.getMaxParticipants()) {
            throw new IllegalStateException("Room is full");
        }

        RoomParticipant participant = new RoomParticipant();
        participant.setRoom(room);
        participant.setUser(user);
        return participantRepository.save(participant);
    }

    @Transactional
    public void startQuiz(String roomCode, User host) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!room.getHost().equals(host)) {
            throw new IllegalStateException("Only host can start the quiz");
        }

        if (room.getStatus() != RoomStatus.WAITING) {
            throw new IllegalStateException("Quiz has already started or ended");
        }

        room.setStatus(RoomStatus.ACTIVE);
        room.setStartedAt(LocalDateTime.now());
        roomRepository.save(room);
    }

    @Transactional
    public void endQuiz(String roomCode, User host) {
        Room room = roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        if (!room.getHost().equals(host)) {
            throw new IllegalStateException("Only host can end the quiz");
        }

        if (room.getStatus() != RoomStatus.ACTIVE) {
            throw new IllegalStateException("Quiz is not active");
        }

        room.setStatus(RoomStatus.CLOSED);
        room.setEndedAt(LocalDateTime.now());
        roomRepository.save(room);
    }

    public Room findByRoomCode(String roomCode) {
        return roomRepository.findByRoomCode(roomCode)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
    }

    public List<RoomParticipant> getRoomParticipants(Room room) {
        return participantRepository.findByRoom(room);
    }
}
