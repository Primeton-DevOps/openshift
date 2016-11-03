/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.List;

import org.junit.Test;

import com.openshift.internal.restclient.model.Service;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ServiceQueryApiTestCase {
	
	@Test
	public void test() {
		List<Service> resources = OpenshiftClient.getClient().list(ResourceKind.SERVICE, "default");
		if (null == resources || resources.isEmpty()) {
			System.err.println("None services.");
			return;
		}
		for (Service service : resources) {
			System.out.println(new StringBuffer()
					.append(service.getCreationTimeStamp()).append(", ")
					.append(service.getName()).append(", ")
					.append(service.getNamespace()).append(",")
					.append(service.getPort()).append(", ")
					.append(service.getPortalIP()).append(", ")
					.append(service.getTargetPort()).append(", ")
					.append(service.getLabels()).append(", ")
					.append(service.getPorts()).append(", ")
					.append(service.getPods()).append(", ")
					.toString());
			System.err.println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
		}
	}

}
