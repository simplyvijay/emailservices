package api.kcorp.message;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import api.kcorp.model.ContactForm;
import api.kcorp.model.EmailForm;

@Component
public class Receiver {

	private final JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Autowired
	public Receiver(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@JmsListener(destination = "shreebala.submitfeedback", containerFactory = "myFactory")
    public void receiveMessage(ContactForm form) {
        System.out.println("Received <" + form + ">");
        
        //Send mail.
        try {
	        MimeMessage msg = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	        
	        helper.setFrom(senderEmail);
	        helper.setTo("contact@shreebalanrithyalaya.com");
	        helper.setSubject("New comment received on shreebalanrithyalaya.com");
	        helper.setText(	"New comment received with the following details\n\nName: " + form.getName() 
	        				+ "\nEmail: " + form.getEmail() 
	        				+ "\nSubject: " + form.getSubject()
	        				+ "\nMessage: " + form.getMessage() 
	        				+ "\nSubscription: " + (form.getOptin()? "Yes" : "No") + "\n\nThanks\nShreebala Nrithyalaya\n\nNote: A confirmation mail has been sent to the sender's mail address");
	        
	        mailSender.send(msg);
	        System.out.println("Mail 1 sent");
	        helper.setTo(form.getEmail());
	        helper.setReplyTo("contact@shreebalanrithyalaya.com");
	        helper.setSubject("Thank you for your valuable comment.");
	        helper.setText("We respect your feedback.\n"
	        				+ "As we are constantly striving to provide the best learning experience to our students, we value your feedback and take actions if required."
	        				+ "Thank You\nShreebala Nrithyalaya", "<html><body><h1>We respect your feedback.</h1><p>As we are constantly striving to provide the best learning experience to our students, we value your feedback and take actions if required.</p><a href='http://www.shreebalanrithyalaya.com'>Shreebala Nrithyalaya</a></body></html>");

	        mailSender.send(msg);
        	System.out.println("Mail 2 sent");
        }
        catch(MailException | MessagingException me) {
        	System.err.println(me.getMessage());
        }
	}
    
    @JmsListener(destination = "simplyvijay.emailvijay", containerFactory = "myFactory")
    public void receiveEmailNotification(EmailForm form) {
    	System.out.println("Received <" + form + ">");
    	
        //Send mail.
        try {
	        MimeMessage msg = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	        helper.setFrom(senderEmail);
	        helper.setTo("contact@simplyvijay.com");
	        helper.setSubject("New message received on simplyvijay.com");
	        helper.setText(	"New message received with the following details\n\nName: " + form.getName() 
	        				+ "\nEmail: " + form.getEmail() 
	        				+ "\nPurpose: " + form.getPurpose()
	        				+ "\nSubject: " + form.getSubject()
	        				+ "\nMessage: " + form.getMessage()
	        				+ "\nJob Description: " + form.getJd()
	        				+ "\nDivision: " + form.getDivision()
	        				+ "\n\nThanks\nVijay");
	        
	        mailSender.send(msg);
        }
        catch(MailException | MessagingException me) {
        	System.err.println(me.getMessage());
        }
	}

}
