package com.ecorp.linebot.config;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Configuration
public class HttpClientConfiguration {

    @SuppressWarnings("deprecation")
    @Bean
    public CloseableHttpClient getHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
	SSLContextBuilder builder = SSLContexts.custom();
	builder.loadTrustMaterial(null, new TrustStrategy() {
	    @Override
	    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		return true;
	    }
	});

	SSLContext sslContext = builder.build();
	LayeredConnectionSocketFactory connectionSocketFactory = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
		.register("http", new PlainConnectionSocketFactory())
		.register("https", connectionSocketFactory)
		.build();

	PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	connectionManager.setDefaultMaxPerRoute(100);
	connectionManager.setMaxTotal(200);
	CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
	return httpClient;
    }
}
