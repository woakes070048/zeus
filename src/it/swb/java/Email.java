package it.swb.java;

import it.swb.log.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	
	public static void main(String[] args) {
		
		//inviaEmail("zeldabomboniere@gmail.com","123456789");
		
	}
	
	public static void inviaEmail(String destinatario, String numeroTracciamento) {
		
		final String username = "info@zeldabomboniere.it";
		final String password = "Moraldi2015";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.starttls.enable", "true");
//		props.put("mail.smtp.host", "srv-hb3.netsons.net");
//		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.host", "mail.zeldabomboniere.it");
		props.put("mail.smtp.port", "25");
		
		try {
			
			if (destinatario!=null && !destinatario.isEmpty() && numeroTracciamento!=null && !numeroTracciamento.isEmpty()){
 
				Session session = Session.getInstance(props,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				  });
			
				String testo = 
						"Gentile utente," +
						"\n\n la informiamo che abbiamo provveduto a spedire gli oggetti che ha acquistato sul sito di Zelda Bomboniere." +
						"\n Può tracciare il suo pacco andando sul sito del corriere SDA alla voce \"Ricerca una spedizione\" inserendo questo codice: "+
						"\n\n"+numeroTracciamento +
						"\n\n Per qualsiasi dubbio o domanda può telefonarci oppure scriverci rispondendo a questa email." +
						"\n\n Cordiali saluti," +
						"\n Lo staff di Zelda Bomboniere";
				
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username, "Zelda Bomboniere"));
				message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destinatario));
				message.setSubject("Il suo ordine su Zelda Bomboniere è stato spedito");
				message.setText(testo);
				
				Transport.send(message);
			}
 
		} catch (Exception e) {
			e.printStackTrace();
			Log.info(e);
		}
	}
}