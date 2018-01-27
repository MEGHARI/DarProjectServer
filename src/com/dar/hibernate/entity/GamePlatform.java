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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="game_platform")
public class GamePlatform implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	int id;

	@ManyToOne()
	@JoinColumn(name = "id_game")
	Game gamep;

	@ManyToOne()
	@JoinColumn(name = "id_platform") 
	Platform platform;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "game")
	private Set<GameUser> gameUsers = new HashSet<GameUser>();

	public GamePlatform() {
		super();
	}


	public GamePlatform(Game gamep, Platform platform) {
		super();
		this.platform = platform;
		this.gamep = gamep;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Platform getPlatform() {
		return platform;
	}


	public void setPlatform(Platform platform) {
		this.platform = platform;
	}


	public Game getGamep() {
		return gamep;
	}


	public void setGamep(Game gamep) {
		this.gamep = gamep;
	}


	public Set<GameUser> getGameUsers() {
		return gameUsers;
	}


	public void setGameUsers(Set<GameUser> gameUsers) {
		this.gameUsers = gameUsers;
	}
	
	


}
