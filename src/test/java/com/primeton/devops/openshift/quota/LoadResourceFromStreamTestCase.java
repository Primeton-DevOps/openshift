/**
 * 
 */
package com.primeton.devops.openshift.quota;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.openshift.restclient.model.IResourceQuota;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class LoadResourceFromStreamTestCase extends AbstractTestCase {
	
	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		InputStream in = LoadResourceFromStreamTestCase.class.getResourceAsStream("/quota.json");
		
		IResourceQuota resourceQuota = getOsClient().getResourceFactory().create(in);
		
		System.out.println(resourceQuota.getKind());
		System.out.println(resourceQuota.toJson());
		
		IOUtils.closeQuietly(in);
	}

}
