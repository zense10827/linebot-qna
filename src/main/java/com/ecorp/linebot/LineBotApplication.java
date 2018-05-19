package com.ecorp.linebot;

import com.ecorp.linebot.service.MessageService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@LineMessageHandler
public class LineBotApplication {

	@Autowired
	private MessageService messageService;

	public static void main(String[] args) {
		SpringApplication.run(LineBotApplication.class, args);
	}

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		System.out.println("event: " + event);

		String gotMsg = event.getMessage().getText();
		String sentMsg;
		if(gotMsg.substring(0,1).equals("@")){
			sentMsg = gotMsg.substring(1);
			// Condition to check price of which coin and change path to query price
			sentMsg = messageService.calculateData(sentMsg);
		}
		else {
			sentMsg = event.getMessage().getText();
		}
		return new TextMessage(sentMsg);
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

}
