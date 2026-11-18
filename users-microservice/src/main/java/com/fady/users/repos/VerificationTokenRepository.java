package com.fady.users.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fady.users.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	VerificationToken findByToken(String token);
}

