package com.miniProjets.quizGame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonIgnore
    private Quiz quiz;

    @Column(nullable = false)
    private Integer score;

    private Integer totalQuestions;

    @Column(nullable = false)
    private LocalDateTime dateTaken;

    @PrePersist
    protected void onCreate() {
        dateTaken = LocalDateTime.now();
    }

    @JsonProperty("quizId")
    public Long getQuizId() {
        return quiz != null ? quiz.getId() : null;
    }

    @JsonProperty("quizTitle")
    public String getQuizTitle() {
        return quiz != null ? quiz.getTitle() : null;
    }
}
