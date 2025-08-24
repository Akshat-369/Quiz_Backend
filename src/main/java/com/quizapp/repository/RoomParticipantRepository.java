package com.quizapp.repository;

import com.quizapp.model.Room;
import com.quizapp.model.RoomParticipant;
import com.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RoomParticipantRepository extends JpaRepository<RoomParticipant, Long> {
    List<RoomParticipant> findByRoom(Room room);
    List<RoomParticipant> findByUser(User user);
    Optional<RoomParticipant> findByRoomAndUser(Room room, User user);
    boolean existsByRoomAndUser(Room room, User user);
    List<RoomParticipant> findByRoomAndIsActiveTrue(Room room);
}
