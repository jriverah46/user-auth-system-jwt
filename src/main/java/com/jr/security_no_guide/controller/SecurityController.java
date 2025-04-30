package com.jr.security_no_guide.controller;

import com.jr.security_no_guide.dto.AuthLoginRequest;
import com.jr.security_no_guide.dto.AuthResponse;
import com.jr.security_no_guide.dto.AuthSignUpRequest;
import com.jr.security_no_guide.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/security")
public class SecurityController {
   @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse>login(@RequestBody @Valid AuthLoginRequest authLoginRequest){
        return new ResponseEntity<>(userDetailsService.loginUser(authLoginRequest), HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse>signUp(@RequestBody @Valid AuthSignUpRequest request){
        return new ResponseEntity<>(userDetailsService.signUpUser(request),HttpStatus.OK);
    }

}
