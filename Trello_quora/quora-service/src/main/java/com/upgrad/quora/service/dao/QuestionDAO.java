package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.entity.QuestionEntity;

@Repository
public class QuestionDAO {
	
	@PersistenceContext
	private EntityManager entityManager; 
	
	@Transactional
	public QuestionEntity createQuestion(QuestionEntity questionEntity) {
		entityManager.persist(questionEntity);
		return questionEntity;
	}
	
	public List<QuestionEntity> getAllQuestions() {
		return entityManager.createNamedQuery("Question.getAll", QuestionEntity.class)
														  .getResultList();
	}
}
