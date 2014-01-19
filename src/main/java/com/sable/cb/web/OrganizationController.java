package com.sable.cb.web;

import com.sable.cb.domain.Organization;
import com.sable.cb.domain.Post;
import com.sable.cb.service.OrganizationService;
import com.sable.cb.service.PostType;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.sable.cb.domain.Organization;
import com.sable.cb.service.OrganizationService;

@RequestMapping("/organizations")
@Controller
public class OrganizationController {

	@Autowired
    OrganizationService organizationService;
	

	@RequestMapping(value ="/getByLoc", method = RequestMethod.	POST)
	@ResponseBody()
	public List getAllOrganization()
	{
		return organizationService.findAllOrganizations();
	}
	

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Organization organization, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, organization);
            return "organizations/create";
        }
        uiModel.asMap().clear();
        organizationService.saveOrganization(organization);
        return "redirect:/organizations/" + encodeUrlPathSegment(organization.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Organization());
        return "organizations/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("organization", organizationService.findOrganization(id));
        uiModel.addAttribute("itemId", id);
        return "organizations/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("organizations", organizationService.findOrganizationEntries(firstResult, sizeNo));
            float nrOfPages = (float) organizationService.countAllOrganizations() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("organizations", organizationService.findAllOrganizations());
        }
        return "organizations/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Organization organization, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, organization);
            return "organizations/update";
        }
        uiModel.asMap().clear();
        organizationService.updateOrganization(organization);
        return "redirect:/organizations/" + encodeUrlPathSegment(organization.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, organizationService.findOrganization(id));
        return "organizations/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Organization organization = organizationService.findOrganization(id);
        organizationService.deleteOrganization(organization);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/organizations";
    }

	void populateEditForm(Model uiModel, Organization organization) {
        uiModel.addAttribute("organization", organization);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
