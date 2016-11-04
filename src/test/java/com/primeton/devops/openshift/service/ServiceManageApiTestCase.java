/**
 * 
 */
package com.primeton.devops.openshift.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IPod;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * Reference: 
 * src/test/resources/service.json
 * src/test/resources/route.json
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ServiceManageApiTestCase extends AbstractTestCase {
	
	private String serviceName = "myservice-" + uid;
	private String podName = "mypod-" + uid;
	private String routeName = serviceName + "-route";
	private String domainName = serviceName + ".openshift.primeton.com";
	
	@Test
	public void test() {
		// Create pod with a container
		IPod pod = getOsClient().getResourceFactory().stub(ResourceKind.POD, podName, projectName);
		// Creating a pod directly without a resource controller (RC)
		IContainer container = pod.addContainer(podName + "-1");
		container.setImage(new DockerImageURI("nginx:latest"));
		pod.addLabel("name", podName);
		
		pod = getOsClient().create(pod);
		Assert.assertNotNull(pod);
		
		// Create service
		IService service = getOsClient().getResourceFactory().stub(ResourceKind.SERVICE, serviceName, projectName);
		
		Map<String, String> labelSelectors = new HashMap<>();
		labelSelectors.put("name", podName); // match line 60
		service.setSelector(labelSelectors);
		// or easy set
		// service.setSelector("name", POD_NAME);
		
		service.addPort(80, 80); // port : targetPort (container)
		
		service = getOsClient().create(service);
		
		Assert.assertNotNull(service);
		
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
		
		System.out.println("Service '" + serviceName + "' success created. Will delete after 600 seconds.");
		 
		// Create route for service
		IRoute route = getOsClient().getResourceFactory().stub(ResourceKind.ROUTE, routeName, projectName);
		
		route.setHost(domainName);
		// route.setPath("/");
		route.setServiceName(serviceName); // binding target service
		route = getOsClient().create(route, projectName);
		
		sleep(600);
		
		// clean resource
		getOsClient().delete(pod);
		getOsClient().delete(route);
		getOsClient().delete(service);
		System.out.println("Service '" + serviceName + " success deleted.");
	}
	
}
