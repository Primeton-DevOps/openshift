/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.List;

import org.junit.Test;

import com.openshift.internal.restclient.model.Project;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ProjectQueryApiTestCase {
	
	@Test
	public void test() {
		List<Project> projects = OpenshiftClient.getClient().list(ResourceKind.PROJECT);
		if (null == projects || projects.isEmpty()) {
			System.err.println("None projects.");
			return;
		}
		for (Project project : projects) {
			System.out.println("ApiVersion: " + project.getApiVersion());
			System.out.println("Name: " + project.getName());
			System.out.println("DisplayName: " + project.getDisplayName());
			System.out.println("Description: " + project.getDescription());
			System.out.println("Namespace: " + project.getNamespace());
			System.out.println("ResourceVersion: " + project.getResourceVersion());
			System.out.println("CreationTime:" + project.getCreationTimeStamp());
			System.out.println("Labels: " + project.getLabels());
			System.out.println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
		}
	}

}
