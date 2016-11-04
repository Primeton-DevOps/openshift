/**
 * 
 */
package com.primeton.devops.openshift.testcase;

/**
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class ExampleTestCase extends AbstractTestCase {

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		sleep(2);
		System.out.println("success");
	}

}
