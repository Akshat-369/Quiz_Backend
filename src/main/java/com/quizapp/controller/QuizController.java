package com.quizapp.controller;

import com.quizapp.controller.dto.AddQuestionRequest;
import com.quizapp.controller.dto.CreateQuizRequest;
import com.quizapp.model.Quiz;
import com.quizapp.model.Question;
import com.quizapp.model.User;
import com.quizapp.service.QuizService;
import com.quizapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @RequestBody CreateQuizRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        Quiz quiz = quizService.createQuiz(
            request.getTitle(),
            request.getDescription(),
            request.getTimePerQuestion(),
            user
        );
        return ResponseEntity.ok(quiz);
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<Question> addQuestion(
            @PathVariable Long quizId,
            @RequestBody AddQuestionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Quiz quiz = quizService.findById(quizId);
        User user = userService.findByUsername(userDetails.getUsername());
        
        if (!quiz.getCreatedBy().equals(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Question question = quizService.addQuestion(
            quiz,
            request.getQuestionText(),
            request.getOptionA(),
            request.getOptionB(),
            request.getOptionC(),
            request.getOptionD(),
            request.getCorrectOption()
        );
        return ResponseEntity.ok(question);
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizService.findById(quizId));
    }

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuizQuestions(@PathVariable Long quizId) {
        Quiz quiz = quizService.findById(quizId);
        return ResponseEntity.ok(quizService.getQuizQuestions(quiz));
    }

    @GetMapping("/my-quizzes")
    public ResponseEntity<List<Quiz>> getMyQuizzes(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return ResponseEntity.ok(quizService.findByCreator(user));
    }
}
