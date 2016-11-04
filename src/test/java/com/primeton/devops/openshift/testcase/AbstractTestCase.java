/**
 * 
 */
package com.primeton.devops.openshift.testcase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class AbstractTestCase {
	
	final protected String uid = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	final protected String projectName = "project-" + uid;
	
	protected IProject project;
	
	@Before
	public void init() {
		IResource request = OpenshiftClient.getClient().getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, projectName);
		project = (IProject)OpenshiftClient.getClient().create(request);
		System.out.println("Project '" + projectName + "' success created.");
	}
	
	@After
	public void clean() {
		OpenshiftClient.getClient().delete(project);
		System.out.println("Project '" + projectName + "' success deleted.");
	}
	
	protected void sleep(int senconds) {
		try {
			TimeUnit.SECONDS.sleep(senconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
