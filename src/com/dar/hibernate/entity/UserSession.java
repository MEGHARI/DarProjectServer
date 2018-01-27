package com.dar.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_sessions")
public class UserSession implements Serializable{

	@Id
	@Column(name="token")
	private String token;

	@Column(name="id_user")
	private int idUser;
	
	@Column(name="timestamp")
	private double timestamp;
	
	public UserSession() {
		super();
	}

	public UserSession(String token, int idUser, double timestamp) {
		super();
		this.token = token;
		this.idUser = idUser;
		this.timestamp = timestamp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}

	
}
