/**
 * 
 */
package com.primeton.devops.openshift.yaml2json;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.yaml.snakeyaml.Yaml;

import com.primeton.devops.openshift.testcase.TemplateTestCase;

/**
 * Yaml -&gt; Java Object -&gt; JSON. <br>
 * 
 * <a href="https://bitbucket.org/asomov/snakeyaml/wiki/Documentation">snakeyaml</a>
 * 
 * @author ZhongWen (mailto:lizhongwen1989@gmail.com)
 *
 */
public class Yaml2JsonTestCase extends TemplateTestCase {

	/* (non-Javadoc)
	 * @see com.primeton.devops.openshift.testcase.AbstractTestCase#test()
	 */
	@Override
	public void test() {
		test1();
		test2();
		test3();
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void test1() {
		Yaml yaml = new Yaml();
		String document = "\n- Hesperiidae\n- Papilionidae\n- Apatelodidae\n- Epiplemidae";
		List<String> list = (List<String>) yaml.load(document);
		System.out.println(list);
		
		// array to json
	}
	
	@SuppressWarnings("rawtypes")
	private void test2() {
	    Yaml yaml = new Yaml();
	    String document = "hello: 25";
	    Map map = (Map) yaml.load(document);
	    Assert.assertEquals("{hello=25}", map.toString());
	    Assert.assertEquals(new Integer(25), map.get("hello"));
	    
	    // map to json
	}
	
	@SuppressWarnings("rawtypes")
	private void test3() {
	    InputStream input = Yaml.class.getResourceAsStream("/deploy.yaml");
	    Yaml yaml = new Yaml();
	    Map map = (Map) yaml.load(input);
	    Assert.assertEquals("DeploymentConfig", map.get("kind"));
	    // map to json
	}

}
