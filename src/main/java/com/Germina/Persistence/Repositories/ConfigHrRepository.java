package com.Germina.Persistence.Repositories;

import com.Germina.Persistence.Entities.ConfigHr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigHrRepository extends JpaRepository<ConfigHr, String> {
    Optional<ConfigHr> findById(String id);

}
