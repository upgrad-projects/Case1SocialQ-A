package com.upgrad.quora.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.entity.UserAuthEntity;

@Repository
public class UserAuthDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional()
	public UserAuthEntity createUserAuthToken(final UserAuthEntity userAuthEntity) {
		entityManager.persist(userAuthEntity);
		return userAuthEntity;
	}

	public UserAuthEntity getUserByAuthorization(final String authorization) {
		try {
			return entityManager.createNamedQuery("UserAuth.findUserByToken", UserAuthEntity.class)
					.setParameter("access_token", authorization).getSingleResult();

		} catch (NoResultException nre) {
			return null;
		}
	}

	@Transactional
	public UserAuthEntity update(UserAuthEntity userAuthEntity) {
			entityManager.merge(userAuthEntity);
			return userAuthEntity;
	}
}
