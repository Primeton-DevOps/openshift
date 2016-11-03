package com.primeton.devops.openshift;

import java.util.List;

import com.openshift.internal.restclient.model.Project;
import com.openshift.internal.restclient.model.Route;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class HelloWorld {
	
	public static void main(String[] args) {
		queryProject();
		queryRoute();
		
	}
	
	private static void queryProject() {
		List<Project> projects = OpenshiftClient.getClient().list(ResourceKind.PROJECT);
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
		List<Route> routes = OpenshiftClient.getClient().list(ResourceKind.ROUTE, "default");
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
					.append("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n")
					.toString());
		}
	}

}
