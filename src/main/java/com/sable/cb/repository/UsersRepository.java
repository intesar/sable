package com.sable.cb.repository;
import com.sable.cb.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaSpecificationExecutor<Users>, JpaRepository<Users, Long> {
}
