package com.quizapp.repository;

import com.quizapp.model.Room;
import com.quizapp.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoomCode(String roomCode);
    List<Room> findByStatus(RoomStatus status);
    boolean existsByRoomCode(String roomCode);
}
