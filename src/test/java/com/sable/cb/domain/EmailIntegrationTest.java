package com.sable.cb.domain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sable.cb.service.EmailService;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
public class EmailIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	
	@Autowired
    EmailService emailService;

	
	@Test
    public void testHelloEmail() {
        Assert.assertNotNull(emailService);
        emailService.sendMessage("mdshannan@gmail.com", "test from sable", "test body");
    }

	
}
