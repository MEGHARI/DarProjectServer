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
@Table(name="users")
public class User implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="last_name")
	private String lastName;

	@Column(name="first_name")
	private String firstName;

	@Column(name="address")
	private String address;

	@Column(name="mail")
	private String mail;

	@Column(name="password")
	private String password;
	
	@Column(name="statut")
	private int statut;
	
	@Column(name="role")
	private int role;
	
	@Column(name="code_confirme")
	private String codeConfirme;
	
	@Column(name="url_picture")
	private String urlPicture;
	

	@OneToMany(fetch = FetchType.EAGER, mappedBy="user")
	private Set<GameUser> gameUsers = new HashSet<GameUser>();
	
	public User() {
		super();
	}

	public User(String lastName, String firstName, String address, String mail,
			String password, int role, String codeConfirme,String urlPicture) {
		super();
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		this.mail = mail;
		this.password = password;
		if(role==1) this.statut = 1;
		else this.statut = 0;
		this.urlPicture=urlPicture;
		
		this.role = role;
		this.codeConfirme = codeConfirme;
	}
	
	public String getUrlPicture() {
		return urlPicture;
	}

	public void setUrlPicture(String urlPicture) {
		this.urlPicture = urlPicture;
	}
	
	public void addGame(GameUser gameUser){
		this.gameUsers.add(gameUser);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatut() {
		return statut;
	}

	public void setStatut(int statut) {
		this.statut = statut;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public Set<GameUser> getGameUsers() {
		return this.gameUsers;
	}

	public void setGameUsers(Set<GameUser> gameUsers) {
		this.gameUsers = gameUsers;
	}
	
	public String getCodeConfirme(){
		return codeConfirme;
	}
	
	public void setCodeConfirme(String codeConfirme){
		this.codeConfirme = codeConfirme;
	}

}
