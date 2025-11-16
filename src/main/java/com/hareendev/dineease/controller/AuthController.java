package com.hareendev.dineease.controller;

import com.hareendev.dineease.config.JwtProvider;
import com.hareendev.dineease.model.Cart;
import com.hareendev.dineease.model.USER_ROLE;
import com.hareendev.dineease.model.User;
import com.hareendev.dineease.repository.CartRepository;
import com.hareendev.dineease.repository.UserRepository;
import com.hareendev.dineease.dto.request.LoginRequest;
import com.hareendev.dineease.dto.response.AuthResponse;
import com.hareendev.dineease.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    public AuthController(
            UserRepository userRepository,
            CartRepository cartRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            CustomerUserDetailsService customerUserDetailsService) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if(isEmailExist != null) {
            throw new Exception("Email is already used with another account");
        }

        User createduser = new User();
        createduser.setEmail(user.getEmail());
        createduser.setFullName(user.getFullName());
        createduser.setRole(user.getRole());
        createduser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createduser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        // authenticate the user
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate JWT token
        String jwt = jwtProvider.generateToken(authentication);

        // prepare the response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register Success");
        authResponse.setRole(savedUser.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = authenticate(username, password); // authentication is finished

        // set the authentication in the security context
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // get the role
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();


        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login Success");
        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    // method to authenticate user
    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if(userDetails == null) {
            throw new BadCredentialsException("Invalid Username ...");
        }

        // check the password
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password ...");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
