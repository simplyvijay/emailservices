package api.kcorp;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import api.kcorp.model.ContactForm;
import api.kcorp.model.EmailForm;
import api.kcorp.model.Status;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/v1")
public class EmailServicesController implements ApplicationContextAware {
	
	private ApplicationContext context = null;
	
	@RequestMapping(value = "/submitfeedback", method = RequestMethod.POST)
	public @ResponseBody Status submit(@Valid @RequestBody ContactForm form, BindingResult bindingResult) throws EmailServicesException {
		
		if(bindingResult.hasErrors()) {
			throw new EmailServicesException("Invalid input data format");
		}
		
		// Push it to the message queue.
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println("Sending a message.");
		jmsTemplate.convertAndSend("shreebala.submitfeedback", form);
		return new Status("All fine", 201);
	}

	@PostMapping("/emailvijay")
	public @ResponseBody Status emailvijay(@Valid @RequestBody EmailForm form, BindingResult bindingResult) throws EmailServicesException {
		
		if(bindingResult.hasErrors()) {
			throw new EmailServicesException("Invalid input data format");
		}
		
		// Push it to the message queue.
		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
		System.out.println("Sending a message");
		jmsTemplate.convertAndSend("simplyvijay.emailvijay", form);
		return new Status("All fine", 201);
	}
	
	// Health check.
	@GetMapping(value="/check")
	public @ResponseBody Status check() {
		return new Status("All fine", 200);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	// Handle exceptions
	@SuppressWarnings("unused")
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(EmailServicesException.class)
	@ResponseBody Status
	handleBadRequest(HttpServletRequest req, Exception ex) {
	    return new Status(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
	}
	
}
