package com.project.boardgames.repositories;

import com.project.boardgames.entities.JwtToken;
public class JwtTokenService22 {
    private JwtTokenRepository jwtTokenRepository;
    public JwtTokenService22(JwtTokenRepository jwtTokenRepository){
        this.jwtTokenRepository = jwtTokenRepository;
    }
    public JwtToken retrieveToken(String name){
        return jwtTokenRepository.findByToken(name);
    }
}
