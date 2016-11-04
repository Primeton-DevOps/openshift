/**
 * 
 */
package com.primeton.devops.openshift.resource;

import java.io.InputStream;

import org.junit.Assert;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IResourceQuota;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * <a href="http://kubernetes.io/docs/admin/resourcequota/">Kubernetes Resource Quota</a>
 * <br/>
 * <a href="https://docs.openshift.com/enterprise/3.1/dev_guide/quota.html">OpenShift Resource Quota</a>
 * <br/>
 * <a href="https://docs.openshift.com/enterprise/3.2/dev_guide/compute_resources.html">OpenShift Compute Resource</a>
 * <br/>
 * <a href="https://docs.openshift.com/container-platform/3.3/dev_guide/compute_resources.html">OpenShift Compute Resource</a>
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
		System.out.println(String.format("Create service %s success.", service)); //$NON-NLS-1$
		
		// create route
		IRoute route = getOsClient().getResourceFactory().stub(ResourceKind.ROUTE, routeName, projectName);
		route.setHost(domainName);
		// route.setPath("/");
		route.setServiceName(serviceName); // binding target service
		route = getOsClient().create(route);
		Assert.assertNotNull(route);
		System.out.println(String.format("Create route %s for service '%s' success.", route, serviceName)); //$NON-NLS-1$
		
		//
		// Resource Quota 
		//
		
		// ResourceQuota resourceQuota = getOsClient().getResourceFactory()
		//	.stub(ResourceKind.RESOURCE_QUOTA, resourceQuotaName, projectName);
		InputStream in = QuotaApiTestCase.class.getResourceAsStream("/quota.json"); //$NON-NLS-1$
		IResourceQuota resourceQuota = getOsClient().getResourceFactory().create(in);
		
		IResourceQuota quota = getOsClient().create(resourceQuota, projectName);
		Assert.assertNotNull(quota);
		
		System.out.println(String.format("Resource Quota '%s' success created, will delete after 600s.", resourceQuotaName));
		
		sleep(600);
		
		// clean
		getOsClient().delete(route);
		getOsClient().delete(service);
		getOsClient().delete(dc);
		getOsClient().delete(quota);
	}

}
