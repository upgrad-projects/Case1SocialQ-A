package com.upgrad.quora.service.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String aboutme;

	private String contactnumber;

	private String country;

	private String dob;

	private String email;

	private String firstname;

	private String lastname;

	private String password;

	private String role;

	private String salt;

	private String username;

	private String uuid;

	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="user")
	private List<AnswerEntity> answers;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="user")
	private List<QuestionEntity> questions;

	//bi-directional many-to-one association to UserAuth
	@OneToMany(mappedBy="user")
	private List<UserAuthEntity> userAuths;

	public UserEntity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAboutme() {
		return this.aboutme;
	}

	public void setAboutme(String aboutme) {
		this.aboutme = aboutme;
	}

	public String getContactnumber() {
		return this.contactnumber;
	}

	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		answer.setUser(this);

		return answer;
	}

	public AnswerEntity removeAnswer(AnswerEntity answer) {
		getAnswers().remove(answer);
		answer.setUser(null);

		return answer;
	}

	public List<QuestionEntity> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<QuestionEntity> questions) {
		this.questions = questions;
	}

	public QuestionEntity addQuestion(QuestionEntity question) {
		getQuestions().add(question);
		question.setUser(this);

		return question;
	}

	public QuestionEntity removeQuestion(QuestionEntity question) {
		getQuestions().remove(question);
		question.setUser(null);

		return question;
	}

	public List<UserAuthEntity> getUserAuths() {
		return this.userAuths;
	}

	public void setUserAuths(List<UserAuthEntity> userAuths) {
		this.userAuths = userAuths;
	}

	public UserAuthEntity addUserAuth(UserAuthEntity userAuth) {
		getUserAuths().add(userAuth);
		userAuth.setUser(this);

		return userAuth;
	}

	public UserAuthEntity removeUserAuth(UserAuthEntity userAuth) {
		getUserAuths().remove(userAuth);
		userAuth.setUser(null);

		return userAuth;
	}

}