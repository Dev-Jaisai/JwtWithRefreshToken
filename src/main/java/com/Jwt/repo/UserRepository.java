package com.Jwt.repo;

import com.Jwt.entity.User;
import com.Jwt.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserInfo, Long> {

        public UserInfo findByUsername(String username);
        UserInfo findFirstById(Long id);

    }

