package com.dar.hibernate.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Table(name="game_user")
public class GameUser implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	int id;

    @ManyToOne()
    @JoinColumn(name = "id_user") 
	User user;

    @ManyToOne()
    @JoinColumn(name = "id_game")
	GamePlatform game;

    @Column(name = "relation")
	int relation;

	public GameUser() {
		super();
	}

	public GameUser(User user, GamePlatform game, int relation) {
		super();
		this.user = user;
		this.game = game;
		this.relation=relation;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	public GamePlatform getGame(){
		return game;
	}
	
	public void setGame(GamePlatform game){
		this.game = game;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	
	
	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}

}
