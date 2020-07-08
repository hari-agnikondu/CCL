package com.incomm.scheduler.notification.impl;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.notification.NotificationService;



@Service
public class NotificationServiceImpl implements NotificationService{
	private static final Logger logger = LogManager.getLogger(NotificationServiceImpl.class.getName());

	@Value("${FILE_UPLOAD_FROM_MAIL}")
	String fileUploadFromMail;

	@Autowired
	SchedulerJobDAO schedulerJobDAO;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	RestTemplate restTemplate;
	
	/*
	 * Method Name smsProcess for send SMS
	 * @Param hostURL
	 * @Param requestXML
	 */
	
	
	
	/*
	 * Method Name mailAlert for send Mail
	 *
	 *  
	 */
	
	public String mailAlert(String[] receipent,String fromMail,String subject,String text) {

		logger.info("mailAlert service invoked with above values notification");
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper mail;
		String retmessage="Success";
		try {
			mail = new MimeMessageHelper(message, true);

			mail.setFrom(fromMail);
			mail.setTo(receipent);
			
			mail.setSubject(subject);
			mail.setText(text);

			emailSender.send(message);

		} catch (MessagingException me) {

			retmessage="Fail";
			logger.error("Exception occure while raise mailAlert",me);

		}
		catch (Exception ex) {
			logger.error("Exception occure while raise mailAlert",ex);
			retmessage="Fail";
		} 
		logger.info("Execution has finished");

		return retmessage;

	}
	
	
	public void sendEmailNotification(String userIds,String jobName,String responseMsg){
		List<String> mailList = schedulerJobDAO.retrieveMailList(userIds);
		if( mailList!=null && !mailList.isEmpty())
		{
			String[] toMails = mailList.stream().toArray(String[]::new);
			mailAlert(toMails,fileUploadFromMail, jobName,responseMsg);
		}
	}



}


