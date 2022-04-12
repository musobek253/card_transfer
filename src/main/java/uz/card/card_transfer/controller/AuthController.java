package uz.card.card_transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import uz.card.card_transfer.payload.LoginDto;
import uz.card.card_transfer.security.JwtProvider;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
     JwtProvider jwtProvider;
    @Autowired
     AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<?> loginTo(@RequestBody LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            String jwtToken = jwtProvider.generateToken(loginDto.getUsername());
            return ResponseEntity.ok(jwtToken);
        }catch (BadCredentialsException e){
            return ResponseEntity.status(401).body("Login yoki Paroliz noto'g'ri!");
        }

    }
}
