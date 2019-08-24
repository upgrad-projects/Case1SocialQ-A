package com.upgrad.quora.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.entity.AnswerEntity;

@Repository
public class AnswerDAO {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Transactional
	public AnswerEntity createAnswer(AnswerEntity answerEntity) {
		entityManager.persist(answerEntity);		
		return answerEntity;
	}
	
	public AnswerEntity getAnswer(final String answerId) {
		try {
			return entityManager.createNamedQuery("Answer.findAnswerById", AnswerEntity.class)
								.setParameter("answerId", answerId)
								.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	public AnswerEntity editAnswer(AnswerEntity answerEntity) {
		entityManager.merge(answerEntity);
		return answerEntity;
	}
}
