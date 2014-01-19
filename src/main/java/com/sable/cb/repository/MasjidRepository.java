package com.sable.cb.repository;
import com.sable.cb.domain.Masjid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MasjidRepository extends JpaRepository<Masjid, Long>, JpaSpecificationExecutor<Masjid> {
}
