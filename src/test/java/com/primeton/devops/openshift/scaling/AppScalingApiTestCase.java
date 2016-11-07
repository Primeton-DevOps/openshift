/**
 * 
 */
package com.primeton.devops.openshift.scaling;

import java.util.List;

import org.junit.Assert;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IPod;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class AppScalingApiTestCase extends AbstractTestCase {

	private String deploymentConfigName = "cfg-nginx-" + uid;
	private String containerName = "c-nginx-" + uid;
	private String labelName = "label-nginx-" + uid;
	private String serviceName = "s-nginx-" + uid;
	private String domainName = "nginx" + uid + ".openshift.primeton.com";
	private String routeName = "r-nginx-" + uid;

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		IDeploymentConfig deploymentConfig = getOsClient().getResourceFactory()
				.stub(ResourceKind.DEPLOYMENT_CONFIG, deploymentConfigName, projectName);
		IContainer container = deploymentConfig.addContainer(containerName);
		container.setImage(new DockerImageURI("nginx:latest"));
		container.addEnvVar("MEM_MAX", "2048");
		container.addEnvVar("MEM_MIN", "1024");
		
		deploymentConfig.setReplicas(2); // pod size
		//deploymentConfig.setDesiredReplicaCount(2); // pod size
		
		deploymentConfig.addLabel("name", labelName);
		
		deploymentConfig.setReplicaSelector("name", labelName);
		
		// create pod
		IDeploymentConfig dc = getOsClient().create(deploymentConfig);
		Assert.assertNotNull(dc);
		
		System.out.println("DeploymentConfig '" + deploymentConfigName + "' success created. Will delete after 600 seconds.");
		
		// create service
		IService service = getOsClient().getResourceFactory().stub(ResourceKind.SERVICE, serviceName, projectName);
		service.setSelector("name", labelName); // bind target pod
		service.addPort(80, 80); // port : targetPort (container)
		service = getOsClient().create(service);
		Assert.assertNotNull(service);
		System.out.println(String.format("Create service %s success.", service));
		
		// create route
		IRoute route = getOsClient().getResourceFactory().stub(ResourceKind.ROUTE, routeName, projectName);
		route.setHost(domainName);
		// route.setPath("/");
		route.setServiceName(serviceName); // binding target service
		route = getOsClient().create(route);
		Assert.assertNotNull(route);
		System.out.println(String.format("Create route %s for service '%s' success.", route, serviceName));
		
		sleep(30);
		
		//
		// Scaling pod size 2 to 3
		//
		List<IPod> pods1 = getOsClient().list(ResourceKind.POD, projectName);
		IDeploymentConfig cfg = getOsClient().get(ResourceKind.DEPLOYMENT_CONFIG, deploymentConfigName, projectName);
		System.out.println(cfg);
		Assert.assertNotNull(cfg);
		
		System.out.println("Scaling pod size 2 to 3");
		cfg.setReplicas(3);
		cfg = getOsClient().update(cfg);
		System.out.println(cfg);
		// query pod scaling result
		sleep(15);
		List<IPod> pods2 = getOsClient().list(ResourceKind.POD, projectName);
		Assert.assertTrue(pods2.size() - pods1.size() == 1);
		Assert.assertTrue(pods2.size() == 3);
		
		sleep(600);
		
		// clean resources
		getOsClient().delete(route);
		getOsClient().delete(service);
		getOsClient().delete(dc);
	}

}
