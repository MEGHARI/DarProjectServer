package com.dar.hibernate.entity;

import java.io.Serializable;
import java.util.Date;
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
@Table(name="games")
public class Game implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	int id;
	
	@Column(name="id_game")
	int idGame;

	@Column(name="name")
	String name;

	@Column(name="summary")
	String summary;
	//Changed to string from long
	@Column(name="release_date")
	String releaseDate;

	@Column(name="cover")
	String cover;

	@Column(name="genre")
	String genre;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "gamep")
	private Set<GamePlatform> gamePlatforms = new HashSet<GamePlatform>();	

	public Game() {
		super();
	}

	public Game(int idGame, String name, String summary, String releaseDate, String cover, String genre) {
		super();
		this.idGame = idGame;
		this.name = name;
		this.summary = summary;
		this.releaseDate = releaseDate;
		this.cover = cover;
		this.genre = genre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdGame() {
		return idGame;
	}

	public void setIdGame(int idGame) {
		this.idGame = idGame;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Set<GamePlatform> getGamePlatforms() {
		return gamePlatforms;
	}

	public void setGamePlatforms(Set<GamePlatform> gamePlatforms) {
		this.gamePlatforms = gamePlatforms;
	}
	

	
}
