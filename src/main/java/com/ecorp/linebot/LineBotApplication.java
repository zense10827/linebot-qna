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

	private static Map<String, String> menuMap = new HashMap<String,String>();

	public static void main(String[] args) {

		priceMap.put("ไข่ไก่", "ฟองละ 3.50 บาทค่ะ");
		priceMap.put("ไข่เป็ด", "ฟองละ 10.25 บาทค่ะ");
		priceMap.put("ไข่นกกะทา", "ฟองละ 25.00 บาทค่ะ");
		priceMap.put("ไข่ห่าน", "ฟองละ 3.25 บาทค่ะ");

		menuMap.put("sizzler", "https://www.sizzler.co.th/storage/upload/201802/1518158751_27867337_1699093963482286_7314833685718772232_n.jpg");
		menuMap.put("kfc", "http://aroundtheworldtravels.co.uk/wp-content/uploads/2011/06/P1120968.jpg");
		menuMap.put("mc donald", "https://i2.wp.com/bkkjunk.com/wp-content/uploads/2016/07/2016-06-17-12.42.29.jpg?w=1200");

		SpringApplication.run(LineBotApplication.class, args);
	}

	@EventMapping
	public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		System.out.println("event: " + event);

		String gotMsg = event.getMessage().getText();
		String sentMsg = "";

		if(gotMsg.equals("@image")){

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

			case Menu:

				if(gotMsg.equals("@cancel")){
					botState = BotState.Default;
					registerState = RegisterState.None;
					sentMsg = "ออกจากบริการถามเมนูแล้วค่ะ";
				}
				else{

					String targetKey = null;
					for ( String key : menuMap.keySet() ) {
						if(gotMsg.toLowerCase().contains(key)){
							targetKey = key;
							break;
						}
					}

					if(targetKey != null){
						return new ImageMessage(menuMap.get(targetKey), menuMap.get(targetKey));
					}
					else{
						sentMsg = "ไม่พบร้านค้าที่ต้องการค่ะ";
					}

				}

				break;

			default:
				if (gotMsg.equals("@register")){
					botState = BotState.Register;
					registerState = RegisterState.WaitName;
					sentMsg = "ยินดีต้อนรับเข้าสู่บริการลงทะเบียน ถ้าต้องการยกเลิกการลงทะเบียนโปรดพิมพ์ @cancel\nโปรดกรอกชื่อด้วยค่ะ";
				}
				else if(gotMsg.equals("@menu")){
					botState = BotState.Menu;
					sentMsg = "ยินดีต้อนรับเข้าสู่บริการถามเมนู ถ้าต้องการออกจากการถามเมนูโปรดพิมพ์ @cancel";
				}
				else {

					botState = BotState.Default;
					registerState = RegisterState.None;

					List<String> sentPrice = new ArrayList<String>();

					for ( String key : priceMap.keySet() ) {
						if(key.contains(gotMsg)){
							sentPrice.add(priceMap.get(key));
						}
					}

					if(sentPrice.size() > 1){
						for(String item : sentPrice){
							sentMsg += item + "\n";
						}
					}
					else{
						String price = priceMap.get(gotMsg);
						if(price == null){
							sentMsg = "ขออภัยค่ะ ไม่สามารถประมวลผลได้";
						}
						else{
							sentMsg = price;
						}
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
