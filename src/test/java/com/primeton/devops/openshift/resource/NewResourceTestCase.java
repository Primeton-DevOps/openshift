/**
 * 
 */
package com.primeton.devops.openshift.resource;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IDeploymentConfig;
import com.openshift.restclient.model.IService;
import com.primeton.devops.openshift.testcase.TemplateTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class NewResourceTestCase extends TemplateTestCase {

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		// 1
		IService service = getOsClient().getResourceFactory().stub(ResourceKind.SERVICE, "serviceName", projectName); //$NON-NLS-1$
		// settings
		System.out.println(service);
		
		// 2
		InputStream input = NewResourceTestCase.class.getResourceAsStream("/deploy.json"); //$NON-NLS-1$
		IDeploymentConfig config = getOsClient().getResourceFactory().create(input);
		System.out.println(config);
		IOUtils.closeQuietly(input);
		
		// 3
		String json = null;
		input = NewResourceTestCase.class.getResourceAsStream("/deploy.json"); //$NON-NLS-1$
		try {
			json = IOUtils.toString(input);
			System.out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(input);
		}
		
		config = getOsClient().getResourceFactory().create(json);
		System.out.println(config);
		/*
		IResource resource = getOsClient().getResourceFactory().create(NewResourceTestCase.class.getResourceAsStream("/beta/autoscale.json"));
		System.out.println(resource.getClass().getName());
		System.out.println(resource);
		*/
	}

}
