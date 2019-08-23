package com.upgrad.quora.service.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
//@NamedQuery(name="Answer.findAll", query="SELECT a FROM Answer a")
public class AnswerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String ans;

	private Timestamp date;

	private String uuid;

	//bi-directional many-to-one association to Question
	@ManyToOne
	private QuestionEntity question;

	//bi-directional many-to-one association to User
	@ManyToOne
	private UserEntity user;

	public AnswerEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAns() {
		return this.ans;
	}

	public void setAns(String ans) {
		this.ans = ans;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public QuestionEntity getQuestion() {
		return this.question;
	}

	public void setQuestion(QuestionEntity question) {
		this.question = question;
	}

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}