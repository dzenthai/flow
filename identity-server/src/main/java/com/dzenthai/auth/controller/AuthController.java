package com.dzenthai.auth.controller;

import com.dzenthai.auth.model.dto.RefreshRequest;
import com.dzenthai.auth.model.dto.TokenRequest;
import com.dzenthai.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody TokenRequest tokenRequest) {
        return new ResponseEntity<>(authService.token(tokenRequest),
                    HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest refreshRequest) {
        return new ResponseEntity<>(authService.refresh(refreshRequest),
                HttpStatus.OK);
    }

    @DeleteMapping("/revoke/{token}")
    public ResponseEntity<?> revoke(@PathVariable String token) {
        authService.revoke(token);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @DeleteMapping("/revoke-all/{token}")
    public ResponseEntity<?> revokeAll(@PathVariable String token) {
        authService.revokeAll(token);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
