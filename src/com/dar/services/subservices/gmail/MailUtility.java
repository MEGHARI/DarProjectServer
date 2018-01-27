package com.dar.services.subservices.gmail;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class MailUtility {

	private static Session mailSession;
	private static String emailSender;
	
	public static Session getMailSession(){
		
		emailSender = "games.exchange.dar@gmail.com";
		final String password = "dar2017UPMC";
/**
 *  SERVER MAIL CONFIGURATIONS
 */
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.trust", "*");
		

		/**
		 * OPEN CONNEXION WITH OUR GMAIL ACOUNT
		 */
		mailSession = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailSender, password);
			}
		  });
		
		return mailSession;
	}
	
	public static InternetAddress getMailSender() throws AddressException{
		return new InternetAddress(emailSender);
	}
	
	public static String getMessageMail(String userName, String pw){
		String messageHTML = "";
		messageHTML +="<center><h3>Hi "+userName+" ! </h3></center><br />";
		messageHTML +="<p>Please use the password below when logging into your account. Thank you !<p/><br />";
		messageHTML +="<p><b>Password : </b>"+pw+"<p/>";
		messageHTML +="<p><b>User : </b>"+userName+" <p><br />";
		messageHTML +="<p style='float : right;'>&copy;2017 DAR GamesExchange</p><br />";
		return messageHTML;
	}
	
	public static String getMessageConfirmationCode(String CODE, String userName){
		String messageHTML = "";
		messageHTML +="<center><h3>Hi "+userName+" ! </h3></center><br />";
		messageHTML +="<p>Please use the code below to confirme your subscription. Thank you !<p/><br />";
		messageHTML +="<p><b>Code : </b>"+CODE+"<p/>";
		messageHTML +="<p style='float : right;'>&copy;2017 DAR GamesExchange</p><br />";
		return messageHTML;
}
}