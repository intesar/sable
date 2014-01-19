package com.sable.cb.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sable.cb.domain.Masjid;
import com.sable.cb.domain.Organization;
import com.sable.cb.service.MasjidService;
import com.sable.cb.service.OrganizationService;

@RequestMapping("/masjid")
@Controller
public class MasjidController {

    @Autowired
    MasjidService masjidService;

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public void create(@RequestBody Masjid masjid) {
    	masjidService.saveMasjid(masjid);
    }
}
