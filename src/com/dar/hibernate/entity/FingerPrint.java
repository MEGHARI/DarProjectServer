package com.dar.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.boot.model.source.spi.IdentifierSourceSimple;

/**
 * 
 * @author g4
 * this entity presents an instance of FingerPrint of current user.
 *
 */
@Entity
@Table(name="fingerprints")
public class FingerPrint implements Serializable{


	public FingerPrint(int id, int idUser, String resolution, int timezone, int enabledCookie, int nbConnexion) {
		super();
		this.id = id;
		this.idUser = idUser;
		this.resolution = resolution;
		this.timezone = timezone;
		this.enabledCookie = enabledCookie;
		this.nbConnexion = nbConnexion;
	}

	public FingerPrint (){super();}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	int id;
	
	@Column(name="id_user")
	int idUser;

	@Column(name="resolution")
	String resolution;
	
	@Column(name="timezone")
	int timezone;
	
	@Column(name="enabled_cookie")
	int enabledCookie;
	
	@Column(name="nb_connexions")
	int nbConnexion;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public int getTimezone() {
		return timezone;
	}

	public void setTimezone(int timezone) {
		this.timezone = timezone;
	}

	public int getEnabledCookie() {
		return enabledCookie;
	}

	public void setEnabledCookie(int enabledCookie) {
		this.enabledCookie = enabledCookie;
	}

	public int getNbConnexion() {
		return nbConnexion;
	}

	public void setNbConnexion(int nbConnexion) {
		this.nbConnexion = nbConnexion;
	}

	
	

}
