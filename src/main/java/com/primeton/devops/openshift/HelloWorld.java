package com.primeton.devops.openshift;

import java.util.List;

import com.openshift.internal.restclient.model.Project;
import com.openshift.internal.restclient.model.Route;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;

/**
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class HelloWorld {
	
	static final String API_URI = "https://192.168.2.91:8443";
	static final String API_USER = "admin";
	static final String API_PASS = "admin";
	static final String API_TOKEN = "asfdsfd8a70a3qrfafdsadsf786324";
	
	static IClient client;
	
	public static void main(String[] args) {
		queryProject();
		queryRoute();
	}
	
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
	
	private static void queryProject() {
		List<Project> projects = getClient().list(ResourceKind.PROJECT);
		if (null == projects || projects.isEmpty()) {
			System.err.println("None projects.");
			return;
		}
		for (Project project : projects) {
			System.out.println("ApiVersion: " + project.getApiVersion());
			System.out.println("Name: " + project.getName());
			System.out.println("DisplayName: " + project.getDisplayName());
			System.out.println("Description: " + project.getDescription());
			System.out.println("Namespace: " + project.getNamespace());
			System.out.println("ResourceVersion: " + project.getResourceVersion());
			System.out.println("CreationTime:" + project.getCreationTimeStamp());
			System.out.println("Labels: " + project.getLabels());
			System.out.println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
		}
	}
	
	private static void queryRoute() {
		List<Route> routes = getClient().list(ResourceKind.ROUTE, "default");
		if (null == routes || routes.isEmpty()) {
			System.err.println("None routes.");
			return;
		}
		for (Route route : routes) {
			System.out.println(new StringBuffer()
					.append("host:")
					.append(route.getHost())
					.append(", name: ")
					.append(route.getName())
					.append(", namespace:")
					.append(route.getNamespace())
					.append(", servicename:")
					.append(route.getServiceName())
					.append("\n$$$$$$$$$$$$$$$$$$$$$$$\n")
					.toString());
		}
	}

}
