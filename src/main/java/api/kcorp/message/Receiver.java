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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class Receiver {

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	@Value("${spring.mail.username}")
	private String senderEmail;

	@Autowired
	public Receiver(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	@JmsListener(destination = "shreebala.submitfeedback", containerFactory = "myFactory")
    public void receiveMessage(ContactForm form) {
        //Send mail.
        try {
	        MimeMessage msg = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
	        
	        helper.setFrom("contact@shreebalanrithyalaya.com");
	        helper.setTo("contact@shreebalanrithyalaya.com");
	        helper.setReplyTo(form.getEmail());
	        helper.setSubject("New comment received on shreebalanrithyalaya.com");
	        helper.setText(	"New comment received with the following details\n\nName: " + form.getName() 
	        				+ "\nEmail: " + form.getEmail() 
	        				+ "\nSubject: " + form.getSubject()
	        				+ "\nMessage: " + form.getMessage() 
	        				+ "\nSubscription: " + (form.getOptin()? "Yes" : "No") + "\n\nThanks\nShreebala Nrithyalaya\n\nNote: A confirmation mail has been sent to the sender's mail address");
	        mailSender.send(msg);

	        helper.setFrom("contact@shreebalanrithyalaya.com");
	        helper.setTo(form.getEmail());
	        helper.setReplyTo("contact@shreebalanrithyalaya.com");
	        helper.setSubject("Thank you for your enquiry / suggestion.");
            Context context = new Context();
            context.setVariable("name", form.getName());
            String html = templateEngine.process("email_shreebala", context);
	        helper.setText(html, true);
	        mailSender.send(msg);
        }
        catch(MailException | MessagingException me) {
        	System.err.println(me.getMessage());
        }
	}
    
    @JmsListener(destination = "simplyvijay.emailvijay", containerFactory = "myFactory")
    public void receiveEmailNotification(EmailForm form) {
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

	        helper.setFrom(senderEmail);
	        helper.setTo(form.getEmail());
	        helper.setReplyTo("contact@simplyvijay.com");
	        helper.setSubject("Thank you for your enquiry");
            Context context = new Context();
            context.setVariable("name", form.getName());
            String html = templateEngine.process("email_simplyvijay", context);
            helper.setText(html, true);
            mailSender.send(msg);
        }
        catch(MailException | MessagingException me) {
        	System.err.println(me.getMessage());
        }
	}

}
