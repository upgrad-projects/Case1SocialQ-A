package com.upgrad.quora.service.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;

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
	
	public QuestionEntity getQuestion(final String questionId) {
		return entityManager.createNamedQuery("Question.getQuestion", QuestionEntity.class)
							.setParameter("questionId", questionId)
							.getSingleResult();
	}
	
	@Transactional
	public QuestionEntity editQuestion(QuestionEntity questionEntity) {
		entityManager.merge(questionEntity);
		return questionEntity;
	}
	
	@Transactional
	public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
		entityManager.remove(questionEntity);
		return questionEntity;
	}
	
	public List<QuestionEntity> getQuestionById(UserEntity userEntity) {
		return entityManager.createNamedQuery("Question.getQuestionByUserId", QuestionEntity.class)
					 .setParameter("user", userEntity)
					 .getResultList();
	}
}
 