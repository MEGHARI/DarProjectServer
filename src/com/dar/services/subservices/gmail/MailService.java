package com.dar.services.subservices.gmail;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.JSONException;
import org.json.JSONObject;

import com.dar.services.ServicesTools;
import com.dar.tools.ErrorTools;

/**La classe <code>MailService</code> définit la configuration du serveur de mai
 * 
 * @author GamesXchanges Team
 *
 */
public class MailService {

	/**
	 * 
	 * @param mail
	 * @param Code
	 * @param userName
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject sendCodeConfirmation(String mail, String Code,String userName) throws JSONException{
		JSONObject res = new JSONObject();
		try {
			InternetAddress emailReceiver = new InternetAddress(mail);
			emailReceiver.validate();

			Message message = new MimeMessage(MailUtility.getMailSession());
			message.setFrom(MailUtility.getMailSender());
			message.setRecipient(Message.RecipientType.TO,emailReceiver);

			message.setSubject("Confirmation Code");
			message.setContent(MailUtility.getMessageConfirmationCode(Code, userName), "text/html");
			Transport.send(message);
			res = ServicesTools.serviceAccepted("Inscription r�ussie. Verifier votre boite mail");
		} catch (AddressException e) {
			res = ServicesTools.serviceRefused("adresse incorrect", ErrorTools.FALSE_MAIL);
		} catch (MessagingException e) {
			res = ServicesTools.serviceRefused("erreur serveur mail", ErrorTools.SERVER_MAIL);
		}

		return res;
	}


	/**
	 * 
	 * @param mail
	 * @param userName
	 * @param password
	 * @return
	 * @throws JSONException
	 */
	public static JSONObject recoverUserPassword(String mail, String userName, String password) throws JSONException{
		JSONObject res = new JSONObject();

			try{
				Message message = new MimeMessage(MailUtility.getMailSession());
				InternetAddress emailReceiver = new InternetAddress(mail);
				emailReceiver.validate();
				message.setFrom(MailUtility.getMailSender());
				message.setRecipient(Message.RecipientType.TO,emailReceiver);

				message.setSubject("Recover your password");
				message.setContent(MailUtility.getMessageMail(userName,password), "text/html");


				Transport.send(message);
				res = ServicesTools.serviceAccepted("Mot de passe envoy� avec succ�s");
				return res;

			}catch (AddressException e) {
				res = ServicesTools.serviceRefused("adresse incorrect", ErrorTools.FALSE_MAIL);
			} catch (MessagingException e) {
				res = ServicesTools.serviceRefused("erreur serveur mail", ErrorTools.SERVER_MAIL);
			}
			return res;


}
}