/**
 * 
 */
package com.primeton.devops.openshift.resource;

import org.junit.Assert;

import com.openshift.internal.restclient.model.ResourceQuota;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IResourceQuota;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;
import com.primeton.devops.openshift.testcase.AbstractTestCase;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * <a href="http://kubernetes.io/docs/admin/resourcequota/">Resource Quota</a>
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class QuotaApiTestCase extends AbstractTestCase {
	
	private String deploymentConfigName = "cfg-nginx-" + uid;
	private String containerName = "c-nginx-" + uid;
	private String labelName = "label-nginx-" + uid;
	private String serviceName = "s-nginx-" + uid;
	private String domainName = "nginx" + uid + ".openshift.primeton.com";
	private String routeName = "r-nginx-" + uid;
	
	private String resourceQuotaName = "quota-" + uid; //$NON-NLS-1$

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
		
		//
		// Resource Quota 
		//
		
		ResourceQuota resourceQuota = OpenshiftClient.getClient().getResourceFactory()
				.stub(ResourceKind.RESOURCE_QUOTA, resourceQuotaName, projectName);
		
//		resourceQuota.addLabel("spec.hard.cpu", "10000");
//		resourceQuota.addLabel("spec.hard.limits.cpu", "10000");
//		resourceQuota.addLabel("spec.hard.limits.memory", "1024Gi");
//		resourceQuota.addLabel("spec.hard.memory", "1024Gi");
//		resourceQuota.addLabel("spec.hard.request.cpu", "1");
//		resourceQuota.addLabel("spec.hard.request.memory", "1Gi");
//		resourceQuota.addLabel("spec.hard.reuest.storage", "4096G");
//		resourceQuota.addLabel("spec.hard.pods", "100");

		
		resourceQuota.setAnnotation("cpu", "10000");
		resourceQuota.setAnnotation("limits.cpu", "10000");
		resourceQuota.setAnnotation("limits.memory", "1024Gi");
		resourceQuota.setAnnotation("memory", "1024Gi");
		resourceQuota.setAnnotation("request.cpu", "1");
		resourceQuota.setAnnotation("request.memory", "1Gi");
		resourceQuota.setAnnotation("reuest.storage", "4096G");
		resourceQuota.setAnnotation("pods", "100");
		
		IResourceQuota quota = OpenshiftClient.getClient().create(resourceQuota);
		Assert.assertNotNull(quota);
		
		System.out.println(String.format("Resource Quota '%s' success created, will delete after 600s.", resourceQuotaName));
		
		sleep(600);
		
		// clean
		OpenshiftClient.getClient().delete(route);
		OpenshiftClient.getClient().delete(service);
		OpenshiftClient.getClient().delete(dc);
		OpenshiftClient.getClient().delete(quota);
	}

}
