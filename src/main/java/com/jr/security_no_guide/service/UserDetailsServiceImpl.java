package com.jr.security_no_guide.service;

import com.jr.security_no_guide.dto.AuthLoginRequest;
import com.jr.security_no_guide.dto.AuthResponse;
import com.jr.security_no_guide.dto.AuthSignUpRequest;
import com.jr.security_no_guide.persistence.entity.RoleEntity;
import com.jr.security_no_guide.persistence.entity.RoleEnum;
import com.jr.security_no_guide.persistence.entity.UserEntity;
import com.jr.security_no_guide.persistence.repository.RoleRepository;
import com.jr.security_no_guide.persistence.repository.UserRepository;
import com.jr.security_no_guide.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user=userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("usuario no existe"));
        List<SimpleGrantedAuthority> authoritiyList=new ArrayList<>();
        user.getRoles()
                .forEach(role->authoritiyList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName().name()))));

        user.getRoles()
                .stream()
                .flatMap(role->role.getPermissions().stream())
                .forEach(permission->authoritiyList.add(new SimpleGrantedAuthority(permission.getName())));
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getIsEnabled(),
                user.getAccountNoExpired(),
                user.getCredentialNoExpired(),
                user.getAccountNoLocked(),
                authoritiyList
        );
    }

    public AuthResponse loginUser(AuthLoginRequest request){
        String username=request.username();
        String password=request.password();

        Authentication authentication=authenticate(username,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken= jwtUtil.createToken(authentication);
        AuthResponse authResponse=new AuthResponse(username,"logged",jwtToken,true);
        return authResponse;

    }

    public AuthResponse signUpUser(AuthSignUpRequest request){
        String username=request.username();
        String password=request.password();
        if (userRepository.existsByUsername(username)){
            throw new RuntimeException("user already exists");
        }
        //create user
        UserEntity user= new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setAccountNoLocked(true);
        user.setAccountNoExpired(true);
        user.setCredentialNoExpired(true);
        user.setIsEnabled(true);

        //Create roles
        RoleEntity defaultRole = roleRepository.findByRoleName(RoleEnum.USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRoles(Set.of(defaultRole));

        userRepository.save(user);
        //create jwt
        Authentication authentication=new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());
        String jwtToken= jwtUtil.createToken(authentication);

        AuthResponse authResponse=new AuthResponse(username,"signed-up",jwtToken,true);
        return authResponse;

    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails=this.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("PASSWORD OR USER INVALID");

        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("PASSWORD INVALID");
        }

        return new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());
    }

    }