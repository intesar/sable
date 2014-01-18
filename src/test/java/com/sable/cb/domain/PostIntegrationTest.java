package com.sable.cb.domain;
import com.sable.cb.repository.PostRepository;
import com.sable.cb.service.PostService;
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
public class PostIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    PostDataOnDemand dod;

	@Autowired
    PostService postService;

	@Autowired
    PostRepository postRepository;

	@Test
    public void testCountAllPosts() {
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", dod.getRandomPost());
        long count = postService.countAllPosts();
        Assert.assertTrue("Counter for 'Post' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindPost() {
        Post obj = dod.getRandomPost();
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Post' failed to provide an identifier", id);
        obj = postService.findPost(id);
        Assert.assertNotNull("Find method for 'Post' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Post' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllPosts() {
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", dod.getRandomPost());
        long count = postService.countAllPosts();
        Assert.assertTrue("Too expensive to perform a find all test for 'Post', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Post> result = postService.findAllPosts();
        Assert.assertNotNull("Find all method for 'Post' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Post' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindPostEntries() {
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", dod.getRandomPost());
        long count = postService.countAllPosts();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Post> result = postService.findPostEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Post' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Post' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Post obj = dod.getRandomPost();
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Post' failed to provide an identifier", id);
        obj = postService.findPost(id);
        Assert.assertNotNull("Find method for 'Post' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPost(obj);
        Integer currentVersion = obj.getVersion();
        postRepository.flush();
        Assert.assertTrue("Version for 'Post' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdatePostUpdate() {
        Post obj = dod.getRandomPost();
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Post' failed to provide an identifier", id);
        obj = postService.findPost(id);
        boolean modified =  dod.modifyPost(obj);
        Integer currentVersion = obj.getVersion();
        Post merged = postService.updatePost(obj);
        postRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Post' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSavePost() {
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", dod.getRandomPost());
        Post obj = dod.getNewTransientPost(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Post' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Post' identifier to be null", obj.getId());
        postService.savePost(obj);
        postRepository.flush();
        Assert.assertNotNull("Expected 'Post' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeletePost() {
        Post obj = dod.getRandomPost();
        Assert.assertNotNull("Data on demand for 'Post' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Post' failed to provide an identifier", id);
        obj = postService.findPost(id);
        postService.deletePost(obj);
        postRepository.flush();
        Assert.assertNull("Failed to remove 'Post' with identifier '" + id + "'", postService.findPost(id));
    }
}
