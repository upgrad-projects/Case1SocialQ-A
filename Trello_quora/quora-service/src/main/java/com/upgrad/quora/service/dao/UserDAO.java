package com.upgrad.quora.service.dao;

import javax.persistence.EntityManager;
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
}