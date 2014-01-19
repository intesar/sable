package com.sable.cb.service;
import com.sable.cb.domain.Masjid;
import java.util.List;

public interface MasjidService {

    public abstract long countAllMasjids();


    public abstract void deleteMasjid(Masjid organization);


    public abstract Masjid findMasjid(Long id);


    public abstract List<Masjid> findAllMasjids();


    public abstract List<Masjid> findMasjidEntries(int firstResult, int maxResults);


    public abstract void saveMasjid(Masjid organization);


    public abstract Masjid updateMasjid(Masjid organization);

}
