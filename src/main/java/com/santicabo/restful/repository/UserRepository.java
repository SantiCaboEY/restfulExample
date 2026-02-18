package com.santicabo.restful.repository;

import com.santicabo.restful.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID>{

    boolean existsByEmail(String email);
}
