package com.a406.horsebit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.a406.horsebit.domain.Token;
import com.a406.horsebit.dto.TokenDTO;

public interface TokenRepository extends JpaRepository<Token, Long> {

	@Query("select NEW com.a406.horsebit.dto.TokenDTO(t.tokenNo, t.name, t.code) from Token t")
	List<TokenDTO> findAllTokens();
}