package com.incomm.scheduler.notification;

import org.springframework.stereotype.Service;

@Service
public interface NotificationService {
	public String mailAlert(String[] receipent,String fromMail,String subject,String text);
	public void sendEmailNotification(String userIds,String jobName,String responseMsg);
}
