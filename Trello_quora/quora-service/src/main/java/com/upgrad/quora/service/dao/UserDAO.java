package com.upgrad.quora.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.upgrad.quora.service.entity.UserEntity;

@Repository
public class UserDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional()
	public UserEntity createUser(UserEntity userEntity) throws PersistenceException{
		entityManager.persist(userEntity);
		return userEntity;
	}
	
	public UserEntity getUserByEmail(final String email) {
		try {
			return entityManager.createNamedQuery("Users.getUserByEmail", UserEntity.class)
								.setParameter("email", email)
								.getSingleResult();
		}
		catch (NoResultException nre) {
			return null;
		}
	}
	
	public UserEntity getUserById(final String userId) {
		try {
			return entityManager.createNamedQuery("Users.getUserById", UserEntity.class)
								.setParameter("userId", userId)
								.getSingleResult();
		}catch (NoResultException nre) {
			return null;
		}
	}
	
	@Transactional
	public String deleteUser(final String userId) {
		entityManager.remove(userId);
		return userId;
	}
}