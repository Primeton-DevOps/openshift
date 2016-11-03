/**
 * 
 */
package com.primeton.devops.openshift.query;

import java.util.List;

import org.junit.Test;

import com.openshift.internal.restclient.model.Pod;
import com.openshift.restclient.ResourceKind;
import com.primeton.devops.openshift.util.OpenshiftClient;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class PodQueryApiTestCase {
	
	@Test
	public void test() {
		List<Pod> resources = OpenshiftClient.getClient().list(ResourceKind.POD, "default");
		if (null == resources || resources.isEmpty()) {
			System.err.println("None pods.");
			return;
		}
		for (Pod pod : resources) {
			System.out.println(new StringBuffer()
					.append(pod.getCreationTimeStamp()).append(", ")
					.append(pod.getHost()).append(", ")
					.append(pod.getIP()).append(",")
					.append(pod.getName()).append(", ")
					.append(pod.getStatus())
					.toString());
		}
	}

}
