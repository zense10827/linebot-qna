package com.ecorp.linebot.dao;

import com.ecorp.linebot.config.DataAPIConfiguration;
import com.ecorp.linebot.service.MessageService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class DataDaoRestImpl {

	@Autowired
	private CloseableHttpClient httpClient;

	@Autowired
	private DataAPIConfiguration dataConfig;

	private static Logger logger = Logger.getLogger(MessageService.class);

	//Add to pretend request call from website
	private  final String USER_AGENT = "Mozilla/5.0";

	public ResponseEntity<String> sendExchange() throws RuntimeException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("User-Agent", USER_AGENT);
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = dataConfig.getDataAPIPath();

		ResponseEntity<String> response = restTemplate.exchange(fooResourceUrl, HttpMethod.GET, entity, String.class);
		return response;
	}

}
