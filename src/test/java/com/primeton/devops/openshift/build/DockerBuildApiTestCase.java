/**
 * 
 */
package com.primeton.devops.openshift.build;

import org.junit.Test;

import com.openshift.restclient.capability.CapabilityVisitor;
import com.openshift.restclient.model.IBuildConfig;
import com.openshift.restclient.model.build.IBuildConfigBuilder;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class DockerBuildApiTestCase extends AbstractTestCase {
	
	private String buildName = "mybuild-" + uid;
	private String imageTag = "myimage:" + uid;
	
	@Test
	public void test() {
		IBuildConfig buildConfig = getOsClient().accept(new CapabilityVisitor<IBuildConfigBuilder, IBuildConfig>() {

			@Override
			public IBuildConfig visit(IBuildConfigBuilder capability) {
				return capability
						.named(buildName)
						.inNamespace(projectName)
						.fromGitSource()
						.fromGitUrl("https://github.com/Official-Registry/redis.git")
						// .inContextDir("docker/redis") // if Dockerfile not in root directory
						.usingGitReference("master")
						.end()
						.usingSourceStrategy()
						.fromDockerImage("redis:3.2.5") // ? Dockerfile
						.end()
						.buildOnSourceChange(true)
						.buildOnConfigChange(true)
						.buildOnImageChange(true)
						.toImageStreamTag(imageTag)
						.build();
			}
		}, null);
		
		// Create build
		buildConfig = getOsClient().create(buildConfig, projectName);
		
		System.out.println("Docker build configuration success created. Will delete after 600 seconds.");
		sleep(600);
		
		getOsClient().delete(buildConfig);
		System.out.println("Docker build configuration success deleted.");
	}

}
