/**
 * 
 */
package com.primeton.devops.openshift.build;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.capability.CapabilityVisitor;
import com.openshift.restclient.model.IBuildConfig;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.openshift.restclient.model.build.IBuildConfigBuilder;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class DockerBuildApiTestCase {
	
	private static final String UID = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	private static final String BUILD_NAME = "mybuild-" + UID;
	private static final String PROJECT_NAME = "myproject-" + UID;
	private static final String IMAGE_TAG = "myimage:" + UID;
	
	private IProject project;
	
	@Before
	public void init() {
		IResource request = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, PROJECT_NAME);
		project = (IProject)OpenshiftClient.getClient().create(request);
		System.out.println("Project '" + PROJECT_NAME + "' success created.");
	}
	
	@Test
	public void test() throws Exception {
		IBuildConfig buildConfig = OpenshiftClient.getClient().accept(new CapabilityVisitor<IBuildConfigBuilder, IBuildConfig>() {

			@Override
			public IBuildConfig visit(IBuildConfigBuilder capability) {
				return capability
						.named(BUILD_NAME)
						.inNamespace(PROJECT_NAME)
						.fromGitSource()
						.fromGitUrl("https://github.com/Official-Registry/redis.git")
						.usingGitReference("master")
						.end()
						.usingSourceStrategy()
						.fromDockerImage("redis:3.2.5")
						.end()
						.buildOnSourceChange(true)
						.buildOnConfigChange(true)
						.buildOnImageChange(true)
						.toImageStreamTag(IMAGE_TAG)
						.build();
			}
		}, null);
		
		// Create build
		buildConfig = OpenshiftClient.getClient().create(buildConfig, PROJECT_NAME);
		
		System.out.println("Docker build configuration success created. Will delete after 600 seconds.");
		TimeUnit.SECONDS.sleep(600);
		
		OpenshiftClient.getClient().delete(buildConfig);
		System.out.println("Docker build configuration success deleted.");
	}

	@After
	public void clean() {
		OpenshiftClient.getClient().delete(project);
		System.out.println("Project '" + PROJECT_NAME + "' success deleted.");
	}

}
