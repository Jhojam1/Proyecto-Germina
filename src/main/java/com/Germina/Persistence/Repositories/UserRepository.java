package com.Germina.Persistence.Repositories;

import com.Germina.Persistence.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByMail(String mail);

    User findByMail(String mail);

    boolean existsByMail(String mail);
    boolean existsByNumberIdentification(Long numberIdentification);
}
