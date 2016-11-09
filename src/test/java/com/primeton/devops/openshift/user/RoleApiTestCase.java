/**
 * 
 */
package com.primeton.devops.openshift.user;

import java.util.List;

import org.junit.Assert;

import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.authorization.IRole;
import com.openshift.restclient.model.user.IUser;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class RoleApiTestCase extends AbstractTestCase {
	
	private String roleName = "role-" + uid;
	private String userName = "user-" + uid;

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		List<IRole> roles = getOsClient().list(ResourceKind.ROLE, projectName);
		if (null == roles || roles.isEmpty()) {
			System.err.println(String.format("No role exists in namespace '%s'.", projectName));
		} else {
			for (IRole role : roles) {
				System.out.println(role);
			}
		}
		
		roles = getOsClient().list(ResourceKind.ROLE, "default");
		if (null == roles || roles.isEmpty()) {
			System.err.println(String.format("No role exists in namespace '%s'.", "default"));
		} else {
			for (IRole role : roles) {
				System.out.println(role);
			}
		}
		
		IRole role = getOsClient().getResourceFactory().stub(ResourceKind.ROLE, roleName, projectName);
		role = getOsClient().create(role);
		Assert.assertNotNull(role);
		System.out.println(role);
		
		IUser user = getOsClient().getResourceFactory().stub(ResourceKind.USER, userName);
		user = getOsClient().create(user);
		System.out.println(user);
		
		List<IUser> users = getOsClient().list(ResourceKind.USER);
		if (null == users || users.isEmpty()) {
			System.err.println("No user exists.");
		} else {
			for (IUser u : users) {
				System.out.println(u);
			}
		}
		
		sleep(30);
		
		getOsClient().delete(user);
	}

}
