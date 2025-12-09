package com.miniProjets.quizGame.service;

import com.miniProjets.quizGame.model.Result;
import com.miniProjets.quizGame.model.User;
import com.miniProjets.quizGame.model.Quiz;
import com.miniProjets.quizGame.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    public Result saveResult(Long userId, Long quizId, Integer score, Integer totalQuestions) {
        User user = userService.getUserById(userId);
        Quiz quiz = quizService.getQuizById(quizId);

        Result result = new Result();
        result.setUser(user);
        result.setQuiz(quiz);
        result.setScore(score);
        result.setTotalQuestions(totalQuestions);

        return resultRepository.save(result);
    }

    public List<Result> getResultsByUser(Long userId) {
        return resultRepository.findByUserIdOrderByDateTakenDesc(userId);
    }

    public List<Result> getResultsByQuiz(Long quizId) {
        return resultRepository.findByQuiz_Id(quizId);
    }

    public Result getResultById(Long id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found"));
    }

    public Integer calculateScore(List<String> userAnswers, List<String> correctAnswers) {
        int score = 0;
        for (int i = 0; i < userAnswers.size(); i++) {
            if (userAnswers.get(i).equals(correctAnswers.get(i))) {
                score++;
            }
        }
        return score;
    }
}
