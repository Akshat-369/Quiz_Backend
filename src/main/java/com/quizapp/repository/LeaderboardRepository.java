package com.quizapp.repository;

import com.quizapp.model.Leaderboard;
import com.quizapp.model.Room;
import com.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    List<Leaderboard> findByRoomOrderByScorePercentageDesc(Room room);
    Optional<Leaderboard> findByRoomAndUser(Room room, User user);
    void deleteByRoom(Room room);
}
