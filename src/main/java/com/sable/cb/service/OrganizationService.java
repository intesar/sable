package com.sable.cb.service;
import com.sable.cb.domain.Organization;
import java.util.List;

public interface OrganizationService {

	public abstract long countAllOrganizations();


	public abstract void deleteOrganization(Organization organization);


	public abstract Organization findOrganization(Long id);


	public abstract List<Organization> findAllOrganizations();


	public abstract List<Organization> findOrganizationEntries(int firstResult, int maxResults);


	public abstract void saveOrganization(Organization organization);


	public abstract Organization updateOrganization(Organization organization);

}
