package es.scpsa.scm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;

import es.devpamplona.web.ScmWebSpringApplication;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScmWebSpringApplication.class)
@WebAppConfiguration
public class ScmWebSpringApplicationTests {

	@Test
	public void contextLoads() {
	}

}
