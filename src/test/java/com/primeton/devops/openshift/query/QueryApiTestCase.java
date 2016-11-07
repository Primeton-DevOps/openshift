/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import com.openshift.internal.restclient.model.user.OpenShiftUser;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IService;
import com.primeton.devops.openshift.testcase.AbstractTestCase;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class QueryApiTestCase extends /*TemplateTestCase*/ AbstractTestCase {
	
	private String serviceName = "s-nginx-" + uid;

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		// 使用同一个Client创建项目但查询不到该项目
		// 使用新的Client可以查询到所创建的项目
		// 不知Client是不是有缓存
		// 1) 使用新的Client进行查询
//		List<IProject> projects = getOsClient().list(ResourceKind.PROJECT);
//		for (IProject project : projects) {
//			System.out.println(project);
//		}
//		System.out.println();
		// 2) 使用老的Client进行查询
//		projects = getNewOsClient().list(ResourceKind.PROJECT);
//		for (IProject project : projects) {
//			System.out.println(project);
//		}
		// 1) 2) 查询方法执行顺序对调就都正常了
		
		// 服务的查询正常
		IService service1 = getOsClient().getResourceFactory().stub(ResourceKind.SERVICE, serviceName, projectName);
		service1.setSelector("name", "nginx-" + uid); // bind target pod
		service1.addPort(80, 80); // port : targetPort (container)
		service1.addLabel("label", "label-nginx-" + uid);
		service1 = getOsClient().create(service1);
		Assert.assertNotNull(service1);
		System.out.println(String.format("Create service %s success.", service1));
		System.out.println();
		
		IService service2 = getOsClient().getResourceFactory().stub(ResourceKind.SERVICE, "s-mysql-" + uid, projectName);
		service2.setSelector("name", "mysql-" + uid); // bind target pod
		service2.addPort(80, 80); // port : targetPort (container)
		service2.addLabel("label", "label-mysql-" + uid);
		service2 = getOsClient().create(service2);
		Assert.assertNotNull(service2);
		System.out.println(String.format("Create service %s success.", service2));
		System.out.println();
		
		List<IService> services = getOsClient().list(ResourceKind.SERVICE, projectName);
		System.out.println(services);
		System.out.println();
		services = getNewOsClient().list(ResourceKind.SERVICE, projectName);
		System.out.println(services);
		
		Map<String, String> labels = new HashMap<>();
		labels.put("label", "label-nginx-" + uid);
		services = getOsClient().list(ResourceKind.SERVICE, projectName, labels);
		Assert.assertNotNull(services);
		Assert.assertTrue(services.size() == 1);
		Assert.assertTrue(serviceName.equals(services.get(0).getName()));
		
		getOsClient().delete(service1);
		
		List<OpenShiftUser> users = OpenshiftClient.getClient().list(ResourceKind.USER, "default");
		if (null == users || users.isEmpty()) {
			System.err.println("None users.");
			return;
		}
		System.out.println();
		for (OpenShiftUser user : users) {
			System.out.println(user);
		}
		
	}

}
