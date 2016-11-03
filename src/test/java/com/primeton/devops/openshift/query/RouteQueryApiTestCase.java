/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.List;

import com.openshift.internal.restclient.model.Route;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class RouteQueryApiTestCase {
	
	public void test() {
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
