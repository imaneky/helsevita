package com.urjc.es.helsevita.Services;

import java.util.List;
import java.util.Optional;

import com.urjc.es.helsevita.Entities.Question;
import com.urjc.es.helsevita.Repositories.QuestionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;

    public Question addQuestion(Question question){
        return questionRepository.save(question);
    }

    public List <Question> returnAll(){
        return questionRepository.findAll();
    }

    public Question returnByCosa(String cosa){
        return questionRepository.findQuestionByCosa(cosa);
    }

    public boolean exists(Integer id){
        Optional <Question> temp = questionRepository.findById(id);
        if(temp.isPresent())
            return true;
        return false;
    }

    public void delete(Integer id) {
        Optional <Question> temp = questionRepository.findById(id);
        if(temp.isPresent()){
            questionRepository.deleteById(id);
        }
        else {
            System.out.println("Question not found");
        }
    }


    public Question returnQuestion(Integer id){
        return questionRepository.findById(id).orElse(null);
    }
}
