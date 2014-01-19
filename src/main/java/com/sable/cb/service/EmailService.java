package com.sable.cb.service;

/**
 * Email servies
 * @author Intesar Mohammed
 *
 */
public interface EmailService {

	/**
	 * 
	 * @param mailTo - Receiver
	 * @param subject 
	 * @param message
	 */
	public void sendMessage(String mailTo, String subject, String message);

}
