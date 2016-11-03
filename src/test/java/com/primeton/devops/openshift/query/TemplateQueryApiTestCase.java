/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.List;

import org.junit.Test;

import com.openshift.internal.restclient.model.template.Template;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class TemplateQueryApiTestCase {

	@Test
	public void test() {
		List<Template> templates = OpenshiftClient.getClient().list(ResourceKind.TEMPLATE, "default");
		if (null == templates || templates.isEmpty()) {
			System.err.println("None templates.");
			return;
		}
		for (Template template : templates) {
			System.out.println(new StringBuffer()
					.append(template.getCreationTimeStamp()).append(", ")
					.append(template.getName()).append(", ")
					.append(template.getNamespace()).append(", ")
					.append(template.getResourceVersion()).append(", ")
					.append(template.getLabels()).append(", ")
					.toString());
			System.out.println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
		}
	}
	
}
