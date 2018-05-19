package com.ecorp.linebot;

import com.ecorp.linebot.config.BotState;
import com.ecorp.linebot.config.RegisterState;
import com.ecorp.linebot.service.MessageService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@LineMessageHandler
public class LineBotApplication {

	@Autowired
	private MessageService messageService;

	private BotState botState = BotState.Default;

	private RegisterState registerState = RegisterState.None;

	private static Map<String, String> priceMap = new HashMap<String,String>();

	public static void main(String[] args) {

		priceMap.put("ไข่ไก่", "ฟองละ 3.50 บาทค่ะ");
		priceMap.put("ไข่เป็ด", "ฟองละ 10.25 บาทค่ะ");
		priceMap.put("ไข่นกกะทา", "ฟองละ 25.00 บาทค่ะ");
		priceMap.put("ไข่ห่าน", "ฟองละ 3.25 บาทค่ะ");

		SpringApplication.run(LineBotApplication.class, args);
	}

	@EventMapping
	public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		System.out.println("event: " + event);

		String gotMsg = event.getMessage().getText();
		String sentMsg = "";

		if(gotMsg.equals("@image")){
			return new ImageMessage("https://cdn.pixabay.com/photo/2016/06/18/17/42/image-1465348_1280.jpg",
					"https://cdn.pixabay.com/photo/2016/06/18/17/42/image-1465348_1280.jpg");
		}

		switch (botState) {
			case Register:

				if(gotMsg.equals("@cancel")){
					botState = BotState.Default;
					registerState = RegisterState.None;
					sentMsg = "ยกเลิกการลงทะเบียนแล้วค่ะ";
				}
				else{
					switch (registerState){
						case WaitName:
							registerState = RegisterState.WaitBirthDate;
							sentMsg = "โปรดกรอกวันเกิดด้วยค่ะ";
							break;
						case WaitBirthDate:
							registerState = RegisterState.WaitPhoneNumber;
							sentMsg = "โปรดกรอกวันเกิดด้วยค่ะ";
							break;
						case WaitPhoneNumber:
							botState = BotState.Default;
							registerState = RegisterState.None;
							sentMsg = "ลงทะเบียนสำเร็จ ขอบคุณค่ะ";
							break;
					}
				}

				break;

			default:
				if (gotMsg.equals("@register")){
					botState = BotState.Register;
					registerState = RegisterState.WaitName;
					sentMsg = "ยินดีต้อนรับเข้าสู่บริการลงทะเบียน ถ้าต้องการยกเลิกการลงทะเบียนโปรดพิมพ์ @cancel\nโปรดกรอกชื่อด้วยค่ะ";
				} else {

					botState = BotState.Default;
					registerState = RegisterState.None;

					List<String> sentPrice = new ArrayList<String>();

					for ( String key : priceMap.keySet() ) {
						if(key.contains(gotMsg)){
							
						}
					}

					String price = priceMap.get(gotMsg);
					if(price == null){
						sentMsg = "ขออภัยค่ะ ไม่สามารถประมวลผลได้";
					}
					else{
						sentMsg = price;
					}
				}

				break;
		}

		return new TextMessage(sentMsg);
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

}
