/**
 * 
 */
package com.primeton.devops.openshift.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IPod;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * Reference: 
 * src/test/resources/service.json
 * src/test/resources/route.json
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ServiceManageApiTestCase {
	
	private static final String UID = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	private static final String PROJECT_NAME = "myproject-" + UID;
	private static final String SERVICE_NAME = "myservice-" + UID;
	private static final String POD_NAME = "mypod-" + UID;
	private static final String ROUTE_NAME = SERVICE_NAME + "-route";
	private static final String DOMAIN_NAME = SERVICE_NAME + ".openshift.primeton.com";
	
	private IProject project;
	
	@Before
	public void init() {
		IResource request = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, PROJECT_NAME);
		project = (IProject)OpenshiftClient.getClient().create(request);
		System.out.println("Project '" + PROJECT_NAME + "' success created.");
	}
	
	@Test
	public void test() throws Exception {
		// Create pod with a container
		IPod pod = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.POD, POD_NAME, PROJECT_NAME);
		// Creating a pod directly without a resource controller (RC)
		IContainer container = pod.addContainer(POD_NAME + "-1");
		container.setImage(new DockerImageURI("nginx:latest"));
		pod.addLabel("name", POD_NAME);
		
		pod = OpenshiftClient.getClient().create(pod);
		Assert.assertNotNull(pod);
		
		// Create service
		IService service = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.SERVICE, SERVICE_NAME, PROJECT_NAME);
		
		Map<String, String> labelSelectors = new HashMap<>();
		labelSelectors.put("name", POD_NAME); // match line 57
		service.setSelector(labelSelectors);
		
		service.addPort(80, 80); // port : targetPort (container)
		
		service = OpenshiftClient.getClient().create(service);
		
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
		
		System.out.println("Service '" + SERVICE_NAME + "' success created. Will delete after 600 seconds.");
		 
		// Create route for service
		IRoute route = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.ROUTE, ROUTE_NAME, PROJECT_NAME);
		
		route.setHost(DOMAIN_NAME);
		// route.setPath("/");
		route.setServiceName(SERVICE_NAME); // binding target service
		route = OpenshiftClient.getClient().create(route, PROJECT_NAME);
		
		TimeUnit.SECONDS.sleep(600);
		
		// clean resource
		OpenshiftClient.getClient().delete(pod);
		OpenshiftClient.getClient().delete(route);
		OpenshiftClient.getClient().delete(service);
		System.out.println("Service '" + SERVICE_NAME + " success deleted.");
	}

	@After
	public void clean() {
		OpenshiftClient.getClient().delete(project);
		System.out.println("Project '" + PROJECT_NAME + "' success deleted.");
	}
}
