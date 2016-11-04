/**
 * 
 */
package com.primeton.devops.openshift.deployment;

import org.junit.Assert;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;
import com.primeton.devops.openshift.testcase.AbstractTestCase;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class DeploymentApiTestCase extends AbstractTestCase {
	
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
		IDeploymentConfig deploymentConfig = OpenshiftClient.getClient().getResourceFactory()
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
		IDeploymentConfig dc = OpenshiftClient.getClient().create(deploymentConfig);
		Assert.assertNotNull(dc);
		
		System.out.println("DeploymentConfig '" + deploymentConfigName + "' success created. Will delete after 600 seconds.");
		
		// create service
		IService service = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.SERVICE, serviceName, projectName);
		service.setSelector("name", labelName); // bind target pod
		service.addPort(80, 80); // port : targetPort (container)
		service = OpenshiftClient.getClient().create(service);
		Assert.assertNotNull(service);
		System.out.println(String.format("Create service %s success.", service));
		
		// create route
		IRoute route = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.ROUTE, routeName, projectName);
		route.setHost(domainName);
		// route.setPath("/");
		route.setServiceName(serviceName); // binding target service
		route = OpenshiftClient.getClient().create(route);
		Assert.assertNotNull(route);
		System.out.println(String.format("Create route %s for service '%s' success.", route, serviceName));
		
		sleep(600);
		
		// clean resources
		OpenshiftClient.getClient().delete(route);
		OpenshiftClient.getClient().delete(service);
		OpenshiftClient.getClient().delete(dc);
	}

}
