package com.sable.cb.domain;
import com.sable.cb.repository.UsersRepository;
import com.sable.cb.service.UsersService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
public class UsersIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    UsersDataOnDemand dod;

	@Autowired
    UsersService usersService;

	@Autowired
    UsersRepository usersRepository;

	@Test
    public void testCountAllUserses() {
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", dod.getRandomUsers());
        long count = usersService.countAllUserses();
        Assert.assertTrue("Counter for 'Users' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindUsers() {
        Users obj = dod.getRandomUsers();
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Users' failed to provide an identifier", id);
        obj = usersService.findUsers(id);
        Assert.assertNotNull("Find method for 'Users' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Users' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllUserses() {
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", dod.getRandomUsers());
        long count = usersService.countAllUserses();
        Assert.assertTrue("Too expensive to perform a find all test for 'Users', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Users> result = usersService.findAllUserses();
        Assert.assertNotNull("Find all method for 'Users' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Users' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindUsersEntries() {
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", dod.getRandomUsers());
        long count = usersService.countAllUserses();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Users> result = usersService.findUsersEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Users' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Users' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Users obj = dod.getRandomUsers();
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Users' failed to provide an identifier", id);
        obj = usersService.findUsers(id);
        Assert.assertNotNull("Find method for 'Users' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUsers(obj);
        Integer currentVersion = obj.getVersion();
        usersRepository.flush();
        Assert.assertTrue("Version for 'Users' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateUsersUpdate() {
        Users obj = dod.getRandomUsers();
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Users' failed to provide an identifier", id);
        obj = usersService.findUsers(id);
        boolean modified =  dod.modifyUsers(obj);
        Integer currentVersion = obj.getVersion();
        Users merged = usersService.updateUsers(obj);
        usersRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Users' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveUsers() {
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", dod.getRandomUsers());
        Users obj = dod.getNewTransientUsers(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Users' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Users' identifier to be null", obj.getId());
        usersService.saveUsers(obj);
        usersRepository.flush();
        Assert.assertNotNull("Expected 'Users' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteUsers() {
        Users obj = dod.getRandomUsers();
        Assert.assertNotNull("Data on demand for 'Users' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Users' failed to provide an identifier", id);
        obj = usersService.findUsers(id);
        usersService.deleteUsers(obj);
        usersRepository.flush();
        Assert.assertNull("Failed to remove 'Users' with identifier '" + id + "'", usersService.findUsers(id));
    }
}
