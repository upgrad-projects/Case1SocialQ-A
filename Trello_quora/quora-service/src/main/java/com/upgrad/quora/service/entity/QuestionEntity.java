package com.upgrad.quora.service.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the question database table.
 * 
 */
@Entity
@NamedQuery(name="Question.findAll", query="SELECT q FROM Question q")
public class QuestionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String content;

	private Timestamp date;

	private String uuid;

	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="question")
	private List<AnswerEntity> answers;

	//bi-directional many-to-one association to User
	@ManyToOne
	private UserEntity user;

	public QuestionEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public List<AnswerEntity> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<AnswerEntity> answers) {
		this.answers = answers;
	}

	public AnswerEntity addAnswer(AnswerEntity answer) {
		getAnswers().add(answer);
		answer.setQuestion(this);

		return answer;
	}

	public AnswerEntity removeAnswer(AnswerEntity answer) {
		getAnswers().remove(answer);
		answer.setQuestion(null);

		return answer;
	}

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}