package com.sable.cb.service;

import com.sable.cb.domain.Organization;
import com.sable.cb.repository.OrganizationRepository;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
    OrganizationRepository organizationRepository;

	public long countAllOrganizations() {
        return organizationRepository.count();
    }

	public void deleteOrganization(Organization organization) {
        organizationRepository.delete(organization);
    }

	public Organization findOrganization(Long id) {
        return organizationRepository.findOne(id);
    }

	public List<Organization> findAllOrganizations() {
        return organizationRepository.findAll();
    }

	public List<Organization> findOrganizationEntries(int firstResult, int maxResults) {
        return organizationRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveOrganization(Organization organization) {
		logger.info(organization.toString());
        organizationRepository.save(organization);
    }

	public Organization updateOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }
}
