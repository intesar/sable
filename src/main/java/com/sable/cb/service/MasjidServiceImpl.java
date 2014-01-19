package com.sable.cb.service;

import com.sable.cb.domain.Masjid;
import com.sable.cb.domain.Users;
import com.sable.cb.repository.MasjidRepository;
import com.sable.cb.repository.UsersRepository;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MasjidServiceImpl implements MasjidService {

    @Autowired
    MasjidRepository masjidRepository;
    
    @Autowired
    UsersRepository usersRepository;
    
    @Autowired
    EmailService emailService;

    public long countAllMasjids() {
        return masjidRepository.count();
    }

    public void deleteMasjid(Masjid masjid) {
        masjidRepository.delete(masjid);
    }

    public Masjid findMasjid(Long id) {
        return masjidRepository.findOne(id);
    }

    public List<Masjid> findAllMasjids() {
        return masjidRepository.findAll();
    }

    public List<Masjid> findMasjidEntries(int firstResult, int maxResults) {
        return masjidRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void saveMasjid(Masjid masjid) {
        
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Users users = usersRepository.findByEmail(user);
        if(users == null) {
            throw new RuntimeException("No Admin found. Masjid must have an admin. Masjid not added.");
        }
        masjid.setAdmin(users);
        masjidRepository.save(masjid);
        
        String link = String.format("\n Masjid "+masjid.getName()+" has been created successfully, and you are the admin.");
        emailService.sendMessage(user, "Masjid "+masjid.getName()+" created successfully", link);
        
    }

    public Masjid updateMasjid(Masjid masjid) {
        return masjidRepository.save(masjid);
    }
}
