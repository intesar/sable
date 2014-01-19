package com.sable.cb.service;

import com.sable.cb.domain.Masjid;
import com.sable.cb.repository.MasjidRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MasjidServiceImpl implements MasjidService {

    @Autowired
    MasjidRepository masjidRepository;

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
        masjidRepository.save(masjid);
    }

    public Masjid updateMasjid(Masjid masjid) {
        return masjidRepository.save(masjid);
    }
}
