package com.ecorp.linebot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataAPIConfiguration {

	@Value("${data.api.url}")
	private String dataAPIPath;

	@Value("${buy.data.api.path}")
	private String highBuyJsonPath;

	@Value("${sell.data.api.path}")
	private String highSellJsonPath;

	public String getDataAPIPath() {
		return dataAPIPath;
	}

	public String getHighBuyJsonPath() {
		return highBuyJsonPath;
	}

	public String getHighSellJsonPath() {
		return highSellJsonPath;
	}
}
