package com.sable.cb.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private transient MailSender mailTemplate;

	@Value("${email.username}")
	private String mailFrom;

	@Override
	@Async
	public void sendMessage(String mailTo, String subject, String message) {
		
		logger.info("sending email " + mailTo);
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom(mailFrom);
		mailMessage.setTo(mailTo);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);

		mailTemplate.send(mailMessage);
	}

}
