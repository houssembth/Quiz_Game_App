package com.miniProjets.quizGame.service;

import com.miniProjets.quizGame.model.Quiz;
import com.miniProjets.quizGame.model.Question;
import com.miniProjets.quizGame.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public List<Quiz> getQuizzesByCategory(String category) {
        return quizRepository.findByCategory(category);
    }

    public List<Quiz> getQuizzesByDifficulty(String difficulty) {
        return quizRepository.findByDifficulty(difficulty);
    }

    public Quiz updateQuiz(Long id, Quiz quizDetails) {
        Quiz quiz = getQuizById(id);
        quiz.setTitle(quizDetails.getTitle());
        quiz.setDescription(quizDetails.getDescription());
        quiz.setCategory(quizDetails.getCategory());
        quiz.setDifficulty(quizDetails.getDifficulty());
        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        Quiz quiz = getQuizById(id);
        quizRepository.delete(quiz);
    }

    public Quiz addQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = getQuizById(quizId);
        question.setQuiz(quiz);
        quiz.getQuestions().add(question);
        return quizRepository.save(quiz);
    }
}
