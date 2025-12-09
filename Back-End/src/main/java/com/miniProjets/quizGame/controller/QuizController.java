package com.miniProjets.quizGame.controller;

import com.miniProjets.quizGame.model.Quiz;
import com.miniProjets.quizGame.model.Question;
import com.miniProjets.quizGame.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
@CrossOrigin(origins = "*")
public class QuizController {
    @Autowired
    private QuizService quizService;

    // ADMIN ONLY - Create Quiz
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz) {
        try {
            Quiz createdQuiz = quizService.createQuiz(quiz);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUBLIC - Get all quizzes
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        return ResponseEntity.ok(quizService.getAllQuizzes());
    }

    // PUBLIC - Get quiz by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable Long id) {
        try {
            Quiz quiz = quizService.getQuizById(id);
            return ResponseEntity.ok(quiz);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUBLIC - Get by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Quiz>> getQuizzesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(quizService.getQuizzesByCategory(category));
    }

    // PUBLIC - Get by difficulty
    @GetMapping("/difficulty/{difficulty}")
    public ResponseEntity<List<Quiz>> getQuizzesByDifficulty(@PathVariable String difficulty) {
        return ResponseEntity.ok(quizService.getQuizzesByDifficulty(difficulty));
    }

    // ADMIN ONLY - Update Quiz
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateQuiz(@PathVariable Long id, @RequestBody Quiz quiz) {
        try {
            Quiz updatedQuiz = quizService.updateQuiz(id, quiz);
            return ResponseEntity.ok(updatedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ADMIN ONLY - Delete Quiz
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.ok("Quiz deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ADMIN ONLY - Add question to quiz
    @PostMapping("/{quizId}/questions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addQuestionToQuiz(@PathVariable Long quizId,
                                               @RequestBody Question question) {
        try {
            Quiz updatedQuiz = quizService.addQuestionToQuiz(quizId, question);
            return ResponseEntity.ok(updatedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}