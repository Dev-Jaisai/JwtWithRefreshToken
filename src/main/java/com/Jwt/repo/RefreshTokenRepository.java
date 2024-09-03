package com.Jwt.repo;

import com.Jwt.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Jaisai
 * Created By Zeeshan on 20-05-2023
 * @project oauth-jwt
 */
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);
}