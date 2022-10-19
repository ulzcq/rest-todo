package com.example.resttodo.security;

import com.example.resttodo.model.UserEntity;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5NMA8JPctFuna59f5";
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public String create(UserEntity userEntity) {
        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS));

        // JWT Token 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(key, SignatureAlgorithm.HS512) //알고리즘
                // payload에 들어갈 내용
                .setSubject(userEntity.getId()) // sub 토큰 주인
                .setIssuer("todo app") // iss
                .setIssuedAt(new Date()) // iat 현재시간으로 생성
                .setExpiration(expiryDate) // exp 만료시간
                .compact();
    }
}
