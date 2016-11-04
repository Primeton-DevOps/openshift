/**
 * 
 */
package com.primeton.devops.openshift.testcase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IProject;
import com.openshift.restclient.model.IResource;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public abstract class AbstractTestCase {
	
	final protected String uid = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	final protected String projectName = "project-" + uid;
	
	protected IProject project;
	
	@Before
	public void init() {
		IResource request = getOsClient().getResourceFactory().stub(ResourceKind.PROJECT_REQUEST, projectName);
		project = (IProject)getOsClient().create(request);
		System.out.println("Project '" + projectName + "' success created.");
	}
	
	@After
	public void clean() {
		getOsClient().delete(project);
		System.out.println("Project '" + projectName + "' success deleted.");
	}
	
	@Test
	public abstract void test();
	
	protected void sleep(int senconds) {
		try {
			TimeUnit.SECONDS.sleep(senconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected IClient getOsClient() {
		return OpenshiftClient.getClient();
	}

}
