package com.sable.cb.repository;
import java.util.List;
import java.util.Set;

import com.sable.cb.domain.Organization;
import com.sable.cb.domain.Post;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
	
	@Query("select p from Post p join p.approvedOrgs o where o in :orgs and p.status like 'APPROVED'")
	public List<Post> findEligiblePosts(@Param("orgs") Set<Organization> orgs);//, Pageable pageable
}
