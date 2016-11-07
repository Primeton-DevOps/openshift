/**
 * 
 */
package com.primeton.devops.openshift.util;

import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class OpenshiftClient {
	
	private OpenshiftClient() {
		super();
	}

	static final String API_URI = "https://192.168.2.91:8443";
	static final String API_USER = "admin";
	static final String API_PASS = "admin";
	static final String API_TOKEN = "asfdsfd8a70a3qrfafdsadsf786324";
	
	private static IClient client;
	
	public static IClient getClient() {
		if (null != client) {
			return client;
		}
		client = new ClientBuilder(API_URI).withUserName(API_USER).withPassword(API_PASS).build();
		
		//
		// This will authorize the client if the cluster is configured for basic authorization.
		// The alternative is to retrieve your OAUTH token and provide it to the client.
		// The token can be set with the builder or later by accessing the authorization context:
		//
		// client.getAuthorizationContext().setToken(API_TOKEN);
		return client;
	}
	
	public static IClient getNewClient() {
		return new ClientBuilder(API_URI).withUserName(API_USER).withPassword(API_PASS).build();
	}

}
