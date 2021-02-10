package api.kcorp.message;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import api.kcorp.model.ContactForm;

import com.sendgrid.*;

import java.io.IOException;

@Component
public class Receiver {

	@Value("${spring.sendgrid.api-key}")
	private String apiKey;

	@Value("${sender.email}")
	private String senderEmail;

	@JmsListener(destination = "shreebala.submitfeedback", containerFactory = "myFactory")
    public void receiveMessage(ContactForm form) {
        //Send mail.
        try {
        	Email from = new Email(senderEmail, "Shreebala Nrithyalaya");
        	Email to = new Email("contact@shreebalanrithyalaya.com");
        	String message = 	"New comment received with the following details\n\nName: " + form.getName()
					+ "\nEmail: " + form.getEmail()
					+ "\nSubject: " + form.getSubject()
					+ "\nMessage: " + form.getMessage()
					+ "\nSubscription: " + (form.getOptin()? "Yes" : "No") + "\n\nThanks\nShreebala Nrithyalaya\n\nNote: A confirmation mail has been sent to the sender's mail address";

        	Content content = new Content("text/plain", message);
        	Mail mail = new Mail(from, "New comment received on shreebalanrithyalaya.com", to, content);
        	mail.setReplyTo(new Email(form.getEmail()));

        	SendGrid sg = new SendGrid(apiKey);

        	Request request = new Request();
        	request.setMethod(Method.POST);
        	request.setEndpoint("mail/send");
        	request.setBody(mail.build());
        	Response response = sg.api(request);
        	if(response.getStatusCode() != HttpStatus.ACCEPTED.value()) {
        		System.err.println("Received an error code " + response.getStatusCode() + " from SendGrid");
			}

			String body = String.format("{\"personalizations\": [{\"to\": [{\"email\": \"%s\",\"name\": \"%s\"}],\"dynamic_template_data\": {\"name\": \"%s\"},\"subject\": \"Thank you for your enquiry / suggestion.\"}],\"from\": {\"email\": \"%s\",\"name\": \"Shreebala Nrithyalaya\"},\"reply_to\": {\"email\": \"contact@shreebalanrithyalaya.com\",\"name\": \"Shreebala Nrithyalaya\"},\"template_id\": \"d-b1e5eb09c1584e08b1f5e2ce2ae4511a\"}",
					form.getEmail(), form.getName(), form.getName(), senderEmail);
        	request.setBody(body);
			response = sg.api(request);
			if(response.getStatusCode() != HttpStatus.ACCEPTED.value()) {
				System.err.println("Received an error code " + response.getStatusCode() + " from SendGrid");
			}
        }
        catch(IOException ex) {
        	System.err.println("Sendgrid: " + ex.getMessage());
        }
	}
}
