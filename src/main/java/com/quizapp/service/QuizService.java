package com.quizapp.service;

import com.quizapp.model.Quiz;
import com.quizapp.model.Question;
import com.quizapp.model.User;
import com.quizapp.repository.QuizRepository;
import com.quizapp.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public Quiz createQuiz(String title, String description, Integer timePerQuestion, User createdBy) {
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setTimePerQuestion(timePerQuestion);
        quiz.setCreatedBy(createdBy);
        quiz.setTotalQuestions(0);
        return quizRepository.save(quiz);
    }

    @Transactional
    public Question addQuestion(Quiz quiz, String questionText, 
                              String optionA, String optionB, 
                              String optionC, String optionD, 
                              char correctOption) {
        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(questionText);
        question.setOptionA(optionA);
        question.setOptionB(optionB);
        question.setOptionC(optionC);
        question.setOptionD(optionD);
        question.setCorrectOption(correctOption);

        question = questionRepository.save(question);
        
        // Update total questions count
        quiz.setTotalQuestions(quiz.getTotalQuestions() + 1);
        quizRepository.save(quiz);
        
        return question;
    }

    public Quiz findById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
    }

    public List<Question> getQuizQuestions(Quiz quiz) {
        return questionRepository.findByQuiz(quiz);
    }

    public List<Quiz> findByCreator(User user) {
        return quizRepository.findByCreatedBy(user);
    }
}
