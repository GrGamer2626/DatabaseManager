package com.mcfan.main;

import com.mcfan.messages.MessageFormatter;

public class ConsoleSender {
	
	public void sendMessageNl(String msg) {
		System.out.println(MessageFormatter.textFormater(msg));
		
	}
	
	public void sendMessage(String msg) {
		System.out.print(MessageFormatter.textFormater(msg));
		
	}
}
