package com.miniProjets.quizGame.repository;

import com.miniProjets.quizGame.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByUserId(Long userId);

    List<Result> findByQuiz_Id(Long quizId);

    List<Result> findByUserIdOrderByDateTakenDesc(Long userId);
}