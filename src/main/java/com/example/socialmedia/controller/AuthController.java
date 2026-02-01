package com.example.socialmedia.controller;

import com.example.socialmedia.model.AppRole;
import com.example.socialmedia.model.Role;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.RoleRepository;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.security.jwt.JwtUtils;
import com.example.socialmedia.security.request.LoginRequest;
import com.example.socialmedia.security.request.SignupRequest;
import com.example.socialmedia.security.response.MessageResponse;
import com.example.socialmedia.security.response.UserInfoResponse;
import com.example.socialmedia.security.service.UserDetailsImpl;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = authentication.getAuthorities()
                .stream().map(item -> item.getAuthority())
                .toList();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), roles, userDetails.getUsername());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest) {

        if (userRepository.existsByUserName(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("username is already taken"));
        }
        if (userRepository.existsByEmail(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("email is already taken"));
        }
        User user = new User(signupRequest.getUsername()
                , signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles != null) {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("admin Role not found"));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("seller Role not found"));
                        roles.add(sellerRole);

                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new UsernameNotFoundException("Role not found"));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        if (authentication != null)
            return authentication.getName();
        else
            return "";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = authentication.getAuthorities()
                .stream().map(item -> item.getAuthority())
                .toList();
//        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), roles, userDetails.getUsername());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                cookie.toString()).body(new MessageResponse("You have been signed out"));
    }

}
