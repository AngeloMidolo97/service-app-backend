package it.epicode.jwt.controller;

import it.epicode.jwt.config.JwtUtils;
import it.epicode.jwt.config.UserDetailsImpl;
import it.epicode.jwt.entity.Role;
import it.epicode.jwt.entity.User;
import it.epicode.jwt.login.LoginRequest;
import it.epicode.jwt.login.LoginResponse;
import it.epicode.jwt.repo.RoleRepo;
import it.epicode.jwt.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class AuthController {

    @Autowired(required = true)
    AuthenticationManager authManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder pe;

    @Autowired
    RoleRepo rp;

    @Autowired
    UserRepo ur;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtUtils.generateJwtToken(auth);

        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();

        List<String> roles = user.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new LoginResponse(jwt,user.getUsername(), roles, user.getExpirationTime()));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(rp.findById(2).get());

        user.setRoleList(roles);
        user.setActive(true);
        user.setDataRegistrazione(LocalDate.now());
        user.setPassword(pe.encode(user.getPassword()));
        User u = ur.save(user);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return ur.findAll();
    }

    @GetMapping("/user2")
    public List<User> getAllUsers2() {
        return ur.findAll();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/ciao")
    public String ciao() {
        return "Ciao World";
    }


}
