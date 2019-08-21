package com.upgrad.quora.service.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the user_auth database table.
 * 
 */
@Entity
@Table(name="user_auth")
//@NamedQuery(name="UserAuth.findAll", query="SELECT u FROM UserAuth u")
public class UserAuthEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="access_token")
	private String accessToken;

	@Column(name="expires_at")
	private Timestamp expiresAt;

	@Column(name="login_at")
	private Timestamp loginAt;

	@Column(name="logout_at")
	private Timestamp logoutAt;

	private String uuid;

	//bi-directional many-to-one association to User
	@ManyToOne
	private UserEntity user;

	public UserAuthEntity() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Timestamp getExpiresAt() {
		return this.expiresAt;
	}

	public void setExpiresAt(Timestamp expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Timestamp getLoginAt() {
		return this.loginAt;
	}

	public void setLoginAt(Timestamp loginAt) {
		this.loginAt = loginAt;
	}

	public Timestamp getLogoutAt() {
		return this.logoutAt;
	}

	public void setLogoutAt(Timestamp logoutAt) {
		this.logoutAt = logoutAt;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public UserEntity getUser() {
		return this.user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

}