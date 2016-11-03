/**
 * 
 */
package com.primeton.devops.openshift.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.IService;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * Reference src/test/service.json
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ServiceManageApiTestCase {
	
	private static final String PROJECT_NAME = "my-test-project";
	private static final String SERVICE_NAME = "my-test-service";
	
	private IProject project;
	
	@Before
	public void init() {
		IResource request = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, PROJECT_NAME);
		project = (IProject)OpenshiftClient.getClient().create(request);
		System.out.println("Project '" + PROJECT_NAME + "' success created.");
	}
	
	@Test
	public void test() throws Exception {
		IService service = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.SERVICE, SERVICE_NAME, PROJECT_NAME);
		
		Map<String, String> labelSelectors = new HashMap<>();
		labelSelectors.put("name", "jenkins");
		service.setSelector(labelSelectors);

		service.addPort(80, 8080);
		
		service.addLabel("app", "jenkins-ephemeral");
		service.addLabel("template", "jenkins-ephemeral-template");
		
		service.setAnnotation("service.alpha.openshift.io/dependencies", "[{\"name\": \"jenkins-jnlp\", \"namespace\": \"" + PROJECT_NAME + "\", \"kind\": \"Service\"}]");
		service.setAnnotation("service.openshift.io/infrastructure", "true");
		
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
		
		System.out.println("Service '" + SERVICE_NAME + "' success created. Will delete after 300 seconds.");
		TimeUnit.SECONDS.sleep(300);
		
		OpenshiftClient.getClient().delete(service);
		System.out.println("Service '" + SERVICE_NAME + " success deleted.");
	}

	public void clean() {
		OpenshiftClient.getClient().delete(project);
		System.out.println("Project '" + PROJECT_NAME + "' success deleted.");
	}
}
