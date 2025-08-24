package com.quizapp.controller;

import com.quizapp.controller.dto.CreateRoomRequest;
import com.quizapp.model.Room;
import com.quizapp.model.RoomParticipant;
import com.quizapp.model.User;
import com.quizapp.service.QuizService;
import com.quizapp.service.RoomService;
import com.quizapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final QuizService quizService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody CreateRoomRequest request,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Room room = roomService.createRoom(
            quizService.findById(request.getQuizId()),
            user,
            request.getMaxParticipants()
        );
        return ResponseEntity.ok(room);
    }

    @PostMapping("/join/{roomCode}")
    public ResponseEntity<RoomParticipant> joinRoom(@PathVariable String roomCode,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        RoomParticipant participant = roomService.joinRoom(roomCode, user);
        return ResponseEntity.ok(participant);
    }

    @PostMapping("/{roomCode}/start")
    public ResponseEntity<Void> startQuiz(@PathVariable String roomCode,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        roomService.startQuiz(roomCode, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomCode}/end")
    public ResponseEntity<Void> endQuiz(@PathVariable String roomCode,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        roomService.endQuiz(roomCode, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<Room> getRoom(@PathVariable String roomCode) {
        return ResponseEntity.ok(roomService.findByRoomCode(roomCode));
    }

    @GetMapping("/{roomCode}/participants")
    public ResponseEntity<List<RoomParticipant>> getRoomParticipants(@PathVariable String roomCode) {
        Room room = roomService.findByRoomCode(roomCode);
        return ResponseEntity.ok(roomService.getRoomParticipants(room));
    }
}
