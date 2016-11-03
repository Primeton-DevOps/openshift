/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.List;

import org.junit.Test;

import com.openshift.internal.restclient.model.Build;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class BuildQueryApiTestCase {
	
	@Test
	public void test() {
		List<Build> resources = OpenshiftClient.getClient().list(ResourceKind.BUILD, "default");
		if (null == resources || resources.isEmpty()) {
			System.err.println("None build history.");
			return;
		}
		for (Build build : resources) {
			System.out.println(new StringBuffer()
					.append(build.getCreationTimeStamp()).append(", ")
					.append(build.getName()).append(", ")
					.append(build.getNamespace()).append(",")
					.append(build.getMessage()).append(", ")
					.append(build.getOutputKind()).append(", ")
					.append(build.getOutputTo()).append(", ")
					.append(build.getPodName()).append(", ")
					.append(build.getPushSecret()).append(", ")
					.append(build.getStatus()).append(", ")
					.append(build.getBuildSource().toString()).append(", ")
					.toString());
			System.err.println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
		}
	}

}
