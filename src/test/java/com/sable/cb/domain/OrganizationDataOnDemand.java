package com.sable.cb.domain;
import com.sable.cb.repository.OrganizationRepository;
import com.sable.cb.service.OrganizationService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class OrganizationDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Organization> data;

	@Autowired
    OrganizationService organizationService;

	@Autowired
    OrganizationRepository organizationRepository;

	public Organization getNewTransientOrganization(int index) {
        Organization obj = new Organization();
        setCity(obj, index);
        setCountry(obj, index);
        setCountryState(obj, index);
        setName(obj, index);
        setStreet(obj, index);
        setZipcode(obj, index);
        return obj;
    }

	public void setCity(Organization obj, int index) {
        String city = "city_" + index;
        obj.setCity(city);
    }

	public void setCountry(Organization obj, int index) {
        String country = "country_" + index;
        obj.setCountry(country);
    }

	public void setCountryState(Organization obj, int index) {
        String countryState = "countryState_" + index;
        obj.setCountryState(countryState);
    }

	public void setName(Organization obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

	public void setStreet(Organization obj, int index) {
        String street = "street_" + index;
        obj.setStreet(street);
    }

	public void setZipcode(Organization obj, int index) {
        String zipcode = "zipcode_" + index;
        obj.setZipcode(zipcode);
    }

	public Organization getSpecificOrganization(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Organization obj = data.get(index);
        Long id = obj.getId();
        return organizationService.findOrganization(id);
    }

	public Organization getRandomOrganization() {
        init();
        Organization obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return organizationService.findOrganization(id);
    }

	public boolean modifyOrganization(Organization obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = organizationService.findOrganizationEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Organization' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Organization>();
        for (int i = 0; i < 10; i++) {
            Organization obj = getNewTransientOrganization(i);
            try {
                organizationService.saveOrganization(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            organizationRepository.flush();
            data.add(obj);
        }
    }
}
