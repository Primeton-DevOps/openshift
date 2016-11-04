/**
 * 
 */
package com.primeton.devops.openshift.deployment;

import org.junit.Assert;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IDeploymentConfig;
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

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		IDeploymentConfig deploymentConfig = OpenshiftClient.getClient().getResourceFactory()
				.stub(ResourceKind.DEPLOYMENT_CONFIG, deploymentConfigName, projectName);
		
		deploymentConfig.setEnvironmentVariable("MEM_MAX", "2048");
		deploymentConfig.setEnvironmentVariable("MEM_MIN", "1024");
		
		IContainer container = deploymentConfig.addContainer(containerName);
		container.setImage(new DockerImageURI("nginx:latest"));
		
		deploymentConfig.setReplicas(2); // container size
		
		deploymentConfig.addLabel("name", labelName);
		
		deploymentConfig.setReplicaSelector("name", labelName);
		
		IDeploymentConfig dc = OpenshiftClient.getClient().create(deploymentConfig);
		Assert.assertNotNull(dc);
		
		System.out.println("DeploymentConfig '" + deploymentConfigName + "' success created. Will delete after 600 seconds.");
		sleep(600);
		OpenshiftClient.getClient().delete(dc);
	}

}
