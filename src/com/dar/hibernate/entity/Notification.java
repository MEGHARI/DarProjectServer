package com.dar.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="notifications")
public class Notification implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@Column(name="id_sender")
	private int idSender;

	@Column(name="id_receiver")
	private int idReceiver;

	@Column(name="type")
	private int type;
	
	@Column(name="vu")
	private int vu;

	public Notification() {
		super();
	}

	public Notification(int idSender, int idReceiver, int type, int vu) {
		super();
		this.idSender = idSender;
		this.idReceiver = idReceiver;
		this.type = type;
		this.vu = vu;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getVu() {
		return vu;
	}

	public void setVu(int vu) {
		this.vu = vu;
	}

	
}
