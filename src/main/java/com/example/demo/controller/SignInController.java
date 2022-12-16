package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.example.demo.Models.JwtResponse;
import com.example.demo.Models.SignInRequest;
import com.example.demo.Models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
//TODO move all of this to a service
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = "api/v1/auth")
public class SignInController {
    private  AuthenticationManager authenticationManager;
    private  JwtUtil jwtUtil;

   
    public SignInController(UserRepository userRepository, AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SignInRequest signInRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);
        User user = (User) authentication.getPrincipal();
        List<String> roles = user.getAuthorities().stream()
                        .map(item ->item.getAuthority())
                        .collect(Collectors.toList());
        JwtResponse res = new JwtResponse();
        res.setToken(jwt);
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setRoles(roles);
        return ResponseEntity.ok(res);
    }

}

