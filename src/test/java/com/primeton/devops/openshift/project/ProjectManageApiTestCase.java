/**
 * 
 */
package com.primeton.devops.openshift.project;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.openshift.internal.restclient.model.Project;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ProjectManageApiTestCase {
	
	@Test
	public void test() throws Exception {
		final String projectName = "myfirstproject";
		IResource request = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, projectName);
		IProject project = (IProject)OpenshiftClient.getClient().create(request);
		System.out.println(String.format("Project '%s' success created.", project));

		Assert.assertNotNull(project);
		
		System.out.println(project);
		
		List<Project> projects = OpenshiftClient.getClient().list(ResourceKind.PROJECT);
		Assert.assertNotNull(projects);
		Assert.assertFalse(projects.isEmpty());
		
		System.out.println("Project '" + projectName + "' success created. Will auto delete after 30 seconds.");
		TimeUnit.SECONDS.sleep(30);
		
		OpenshiftClient.getClient().delete(project);
	}

}
