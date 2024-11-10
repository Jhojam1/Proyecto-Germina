package com.Germina.Persistence.Repositories;

import com.Germina.Persistence.Entities.UserTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTempRepository extends JpaRepository<UserTemp, Long> {
}
