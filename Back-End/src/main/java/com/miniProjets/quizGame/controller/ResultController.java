package com.miniProjets.quizGame.controller;

import com.miniProjets.quizGame.model.Result;
import com.miniProjets.quizGame.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {
    @Autowired
    private ResultService resultService;

    // Submit result - Allow all authenticated users
    @PostMapping
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<?> submitResult(@RequestBody Map<String, Object> resultData) {
        try {
            Long userId = Long.valueOf(resultData.get("userId").toString());
            Long quizId = Long.valueOf(resultData.get("quizId").toString());
            Integer score = Integer.valueOf(resultData.get("score").toString());
            Integer totalQuestions = Integer.valueOf(resultData.get("totalQuestions").toString());

            Result result = resultService.saveResult(userId, quizId, score, totalQuestions);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get user results
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<List<Result>> getResultsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(resultService.getResultsByUser(userId));
    }

    // ADMIN can view all results by quiz
    @GetMapping("/quiz/{quizId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Result>> getResultsByQuiz(@PathVariable Long quizId) {
        return ResponseEntity.ok(resultService.getResultsByQuiz(quizId));
    }

    // Get specific result
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PLAYER', 'ADMIN')")
    public ResponseEntity<?> getResultById(@PathVariable Long id) {
        try {
            Result result = resultService.getResultById(id);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}