package com.msil.evaluation.repository;



import com.msil.evaluation.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetail, Long> {

    Optional<UserDetail> findByUserName(String userName); //retrieve userinfo details

    Optional<UserDetail> findByEmail(String email);
    Optional<UserDetail> findByUserNameOrEmail(String username, String email);
}