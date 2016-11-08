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
		// 1
		IResource request = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, projectName);
		// 2
		/*
		IProject request = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.PROJECT, projectName);
		request.setDescription("This is my first project.");
		request.setDisplayName("My First Project");
		*/
		IProject project = (IProject)OpenshiftClient.getClient().create(request);
		System.out.println(String.format("Project '%s' success created.", project));

		Assert.assertNotNull(project);
		
		System.out.println(project);
		
		project = OpenshiftClient.getClient().get(ResourceKind.PROJECT, projectName, null);
		Assert.assertNotNull(project);
		
		project.setDescription("This is my first project.");
		project.setDisplayName("My First Project");
		
		OpenshiftClient.getClient().update(project);
		
		List<Project> projects = OpenshiftClient.getClient().list(ResourceKind.PROJECT);
		Assert.assertNotNull(projects);
		Assert.assertFalse(projects.isEmpty());
		
		System.out.println("Project '" + projectName + "' success created. Will auto delete after 30 seconds.");
		TimeUnit.SECONDS.sleep(30);
		
		OpenshiftClient.getClient().delete(project);
	}

}
