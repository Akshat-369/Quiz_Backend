package com.quizapp.repository;

import com.quizapp.model.Quiz;
import com.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCreatedBy(User user);
    List<Quiz> findByTitleContainingIgnoreCase(String title);
}
