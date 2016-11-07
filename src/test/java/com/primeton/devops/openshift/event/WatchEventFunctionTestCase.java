/**
 * 
 */
package com.primeton.devops.openshift.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;

import com.openshift.restclient.IOpenShiftWatchListener;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IPod;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.IService;
import com.openshift.restclient.model.route.IRoute;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class WatchEventFunctionTestCase extends AbstractTestCase {
	
	private String shutdownSignal = "no";
	
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
		
		new Thread(() -> {
			while ("no".equalsIgnoreCase(shutdownSignal)) { //$NON-NLS-1$
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); //$NON-NLS-1$
				sleep(10);
			}
		}).start();
		
		// sleep(30);
		// shutdownSignal = "yes";
		
		//
		// Bind watch listener
		//
		getOsClient().watch(projectName, new IOpenShiftWatchListener() {
			
			@Override
			public void received(IResource resource, ChangeType change) {
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				System.out.println(String.format("Resource %s changed [%s]", resource, change.getValue())); //$NON-NLS-1$
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			}
			
			@Override
			public void error(Throwable err) {
				err.printStackTrace();
			}
			
			@Override
			public void disconnected() {
				System.out.println("Disconnected"); //$NON-NLS-1$
			}
			
			@Override
			public void connected(List<IResource> resources) {
				System.out.println("Connected"); //$NON-NLS-1$
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
				for (IResource resource : resources) {
					System.out.println(resource);
				}
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			}
		}, ResourceKind.SERVICE, ResourceKind.POD, ResourceKind.ROUTE, ResourceKind.DEPLOYMENT_CONFIG);
		
		//
		// Event
		//
		IDeploymentConfig deploymentConfig = getOsClient().getResourceFactory()
				.stub(ResourceKind.DEPLOYMENT_CONFIG, deploymentConfigName, projectName);
		IContainer container = deploymentConfig.addContainer(containerName);
		container.setImage(new DockerImageURI("nginx:latest")); //$NON-NLS-1$
		container.addEnvVar("MEM_MAX", "2048"); //$NON-NLS-1$ //$NON-NLS-1$
		container.addEnvVar("MEM_MIN", "1024"); //$NON-NLS-1$ //$NON-NLS-2$
		
		deploymentConfig.setReplicas(2); // pod size
		//deploymentConfig.setDesiredReplicaCount(2); // pod size
		
		deploymentConfig.addLabel("name", labelName);
		
		deploymentConfig.setReplicaSelector("name", labelName);
		
		// create pod
		IDeploymentConfig dc = getOsClient().create(deploymentConfig);
		Assert.assertNotNull(dc);
		
		System.out.println("DeploymentConfig '" + deploymentConfigName + "' success created. Will delete after 100 seconds.");
		
		// create service
		IService service = getOsClient().getResourceFactory().stub(ResourceKind.SERVICE, serviceName, projectName);
		service.setSelector("name", labelName); // bind target pod //$NON-NLS-1$
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
		
		System.out.println("Scaling pod size 2 to 3"); //$NON-NLS-1$
		cfg.setReplicas(3);
		cfg = getOsClient().update(cfg);
		System.out.println(cfg);
		// query pod scaling result
		sleep(15);
		List<IPod> pods2 = getOsClient().list(ResourceKind.POD, projectName);
		Assert.assertTrue(pods2.size() - pods1.size() == 1);
		Assert.assertTrue(pods2.size() == 3);
		
		sleep(100);
		
		// clean resources
		getOsClient().delete(route);
		getOsClient().delete(service);
		getOsClient().delete(dc);
		
		//
		// shutdown non-daemon thread
		//
		shutdownSignal = "yes"; //$NON-NLS-1$
	}

}
