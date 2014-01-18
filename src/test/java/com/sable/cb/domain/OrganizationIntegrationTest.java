package com.sable.cb.domain;
import com.sable.cb.repository.OrganizationRepository;
import com.sable.cb.service.OrganizationService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class OrganizationIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    OrganizationDataOnDemand dod;

	@Autowired
    OrganizationService organizationService;

	@Autowired
    OrganizationRepository organizationRepository;

	@Test
    public void testCountAllOrganizations() {
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", dod.getRandomOrganization());
        long count = organizationService.countAllOrganizations();
        Assert.assertTrue("Counter for 'Organization' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindOrganization() {
        Organization obj = dod.getRandomOrganization();
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Organization' failed to provide an identifier", id);
        obj = organizationService.findOrganization(id);
        Assert.assertNotNull("Find method for 'Organization' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Organization' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllOrganizations() {
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", dod.getRandomOrganization());
        long count = organizationService.countAllOrganizations();
        Assert.assertTrue("Too expensive to perform a find all test for 'Organization', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Organization> result = organizationService.findAllOrganizations();
        Assert.assertNotNull("Find all method for 'Organization' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Organization' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindOrganizationEntries() {
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", dod.getRandomOrganization());
        long count = organizationService.countAllOrganizations();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Organization> result = organizationService.findOrganizationEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Organization' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Organization' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Organization obj = dod.getRandomOrganization();
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Organization' failed to provide an identifier", id);
        obj = organizationService.findOrganization(id);
        Assert.assertNotNull("Find method for 'Organization' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyOrganization(obj);
        Integer currentVersion = obj.getVersion();
        organizationRepository.flush();
        Assert.assertTrue("Version for 'Organization' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateOrganizationUpdate() {
        Organization obj = dod.getRandomOrganization();
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Organization' failed to provide an identifier", id);
        obj = organizationService.findOrganization(id);
        boolean modified =  dod.modifyOrganization(obj);
        Integer currentVersion = obj.getVersion();
        Organization merged = organizationService.updateOrganization(obj);
        organizationRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Organization' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveOrganization() {
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", dod.getRandomOrganization());
        Organization obj = dod.getNewTransientOrganization(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Organization' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Organization' identifier to be null", obj.getId());
        organizationService.saveOrganization(obj);
        organizationRepository.flush();
        Assert.assertNotNull("Expected 'Organization' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteOrganization() {
        Organization obj = dod.getRandomOrganization();
        Assert.assertNotNull("Data on demand for 'Organization' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Organization' failed to provide an identifier", id);
        obj = organizationService.findOrganization(id);
        organizationService.deleteOrganization(obj);
        organizationRepository.flush();
        Assert.assertNull("Failed to remove 'Organization' with identifier '" + id + "'", organizationService.findOrganization(id));
    }
}
