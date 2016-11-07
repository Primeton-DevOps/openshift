/**
 * 
 */
package com.primeton.devops.openshift.query;

import org.junit.Assert;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IService;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ResourceUrlTestCase extends AbstractTestCase {

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		String url = getOsClient().getResourceURI(project);
		System.out.println(url);
		Assert.assertNotNull(url);
		Assert.assertTrue(url.endsWith(projectName));
		
		IService service = getOsClient().getResourceFactory().stub(ResourceKind.SERVICE, "test", projectName);
		service.addPort(80, 80);
		service = getOsClient().create(service);
		
		url = getOsClient().getResourceURI(service);
		System.out.println(url);
		Assert.assertNotNull(url);
		Assert.assertTrue(url.endsWith(projectName + "/services/test"));
		
		getOsClient().delete(service);
	}

}
