/**
 * 
 */
package com.primeton.devops.openshift.user;

//import com.openshift.restclient.ResourceKind;
//import com.openshift.restclient.model.authorization.IRole;
//import com.openshift.restclient.model.authorization.IRoleBinding;
//import com.openshift.restclient.model.secret.ISecret;
//import com.openshift.restclient.model.user.IUser;
import com.primeton.devops.openshift.testcase.AbstractTestCase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class UserApiTestCase extends AbstractTestCase {
	
//	private String username = "user-" + uid; //$NON-NLS-1$
//	private String rolename = "role-" + uid; //$NON-NLS-1$
//	private String secretname = "secret-" + uid; //$NON-NLS-1$

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		/*
		IUser user = getOsClient().getResourceFactory().stub(ResourceKind.USER, username);
		
		ISecret secret = getOsClient().getResourceFactory().stub(ResourceKind.SECRET, secretname);
		
		IRole role = getOsClient().getResourceFactory().stub(ResourceKind.ROLE, rolename);
		role.addLabel("name", rolename);
		
		IRoleBinding roleBinding = getOsClient().getResourceFactory().stub(ResourceKind.ROLE_BINDING, rolename + "-binding");
		roleBinding.addUserName(username);
		roleBinding.addLabel("name", rolename);
		sleep(300);
		*/
	}

}
