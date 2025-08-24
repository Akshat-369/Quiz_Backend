package com.quizapp.repository;

import com.quizapp.model.Question;
import com.quizapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByQuiz(Quiz quiz);
    void deleteByQuiz(Quiz quiz);
}
