/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.List;

import org.junit.Test;

import com.openshift.internal.restclient.model.user.OpenShiftUser;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class UserQueryApiTestCase {
	
	@Test
	public void test() {
		List<OpenShiftUser> users = OpenshiftClient.getClient().list(ResourceKind.USER, "default");
		if (null == users || users.isEmpty()) {
			System.err.println("None users.");
			return;
		}
		for (OpenShiftUser user : users) {
			System.out.println(new StringBuffer()
					.append(user.getCreationTimeStamp()).append(", ")
					.append(user.getFullName()).append(", ")
					.append(user.getName()).append(", ")
					.append(user.getNamespace()).append(", ")
					.append(user.getResourceVersion()).append(", ")
					.append(user.getLabels()).append(", ")
					.toString());
			System.out.println("\n$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$\n");
		}
	}

}
