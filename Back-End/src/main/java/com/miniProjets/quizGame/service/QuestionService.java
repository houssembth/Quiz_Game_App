package com.miniProjets.quizGame.service;

import com.miniProjets.quizGame.model.Question;
import com.miniProjets.quizGame.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuiz_Id(quizId);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = getQuestionById(id);
        question.setQuestionText(questionDetails.getQuestionText());
        question.setOptions(questionDetails.getOptions());
        question.setCorrectAnswer(questionDetails.getCorrectAnswer());
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        Question question = getQuestionById(id);
        questionRepository.delete(question);
    }
}
