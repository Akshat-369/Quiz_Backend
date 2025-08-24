package com.quizapp.repository;

import com.quizapp.model.Room;
import com.quizapp.model.User;
import com.quizapp.model.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserResponseRepository extends JpaRepository<UserResponse, Long> {
    List<UserResponse> findByRoomAndUser(Room room, User user);
    List<UserResponse> findByRoom(Room room);
    int countByRoomAndUserAndIsCorrectTrue(Room room, User user);
    int countByRoomAndUser(Room room, User user);
}
