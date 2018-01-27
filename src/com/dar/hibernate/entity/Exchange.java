package com.dar.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="exchanges")
public class Exchange implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
    @Column(name = "id_game_user_sender")
	private int idSender;
	
    @Column(name = "id_game_user_receiver")
	private int idReceiver;
	
	@Column(name="date_exchange")
	private String dateExchange;
	
	@Column(name="date_return")
	private String dateReturn;
	
	@Column(name="confirm_exchange")
	private int confirmExchange;
	
	@Column(name="confirm_return_sender")
	private int confirmReturnSender;
	
	@Column(name="confirm_return_receiver")
	private int confirmReturnReceiver;

	@Column(name="return_ok")
	private int returnOk;	
	
	public Exchange() {
		super();
	}

	public Exchange(int idSender, int idReceiver, String dateExchange, String dateReturn) {
		super();
		this.idSender = idSender;
		this.idReceiver = idReceiver;
		this.dateExchange = dateExchange;
		this.dateReturn = dateReturn;
		this.confirmExchange = 0;
		this.confirmReturnSender = 0;
		this.confirmReturnReceiver = 0;
		this.returnOk = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdSender() {
		return idSender;
	}

	public void setIdSender(int idSender) {
		this.idSender = idSender;
	}

	public int getIdReceiver() {
		return idReceiver;
	}

	public void setIdReceiver(int idReceiver) {
		this.idReceiver = idReceiver;
	}

	public String getDateExchange() {
		return dateExchange;
	}

	public void setDateExchange(String dateExchange) {
		this.dateExchange = dateExchange;
	}

	public String getDateReturn() {
		return dateReturn;
	}

	public void setDateReturn(String dateReturn) {
		this.dateReturn = dateReturn;
	}

	public int getConfirmExchange() {
		return confirmExchange;
	}

	public void setConfirmExchange(int confirmExchange) {
		this.confirmExchange = confirmExchange;
	}

	public int getConfirmReturnSender() {
		return confirmReturnSender;
	}

	public void setConfirmReturnSender(int confirmReturnSender) {
		this.confirmReturnSender = confirmReturnSender;
	}

	public int getConfirmReturnReceiver() {
		return confirmReturnReceiver;
	}

	public void setConfirmReturnReceiver(int confirmReturnReceiver) {
		this.confirmReturnReceiver = confirmReturnReceiver;
	}

	public int getReturnOk() {
		return returnOk;
	}

	public void setReturnOk(int returnOk) {
		this.returnOk = returnOk;
	}

}
