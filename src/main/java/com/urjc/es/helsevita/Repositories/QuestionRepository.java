package com.urjc.es.helsevita.Repositories;

import java.util.Optional;

import com.urjc.es.helsevita.Entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface QuestionRepository extends JpaRepository<Question,Integer> {
    public Question findQuestionByCosa(String cosa);
    public Optional<Question> findById(Integer id);
    public void deleteById(Integer id);
}
