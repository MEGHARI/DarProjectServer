package com.dar.hibernate.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="platforms")
public class Platform implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	int id;
	
	@Column(name="id_platform")
	int idPlatform;
	
	@Column(name="name")
	String name;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy="platform")
	private Set<GamePlatform> gamePlatforms = new HashSet<GamePlatform>();

	public Platform() {
		super();
	}

	public Platform(int idPlatform, String name) {
		super();
		this.idPlatform = idPlatform;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdPlatform() {
		return idPlatform;
	}

	public void setIdPlatform(int idPlatform) {
		this.idPlatform = idPlatform;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<GamePlatform> getGamePlatforms() {
		return gamePlatforms;
	}

	public void setGamePlatforms(Set<GamePlatform> gamePlatforms) {
		this.gamePlatforms = gamePlatforms;
	}

}
