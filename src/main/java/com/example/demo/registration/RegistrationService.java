package com.example.demo.registration;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.Models.User;
import com.example.demo.Models.UserRole;
import com.example.demo.Models.UserService;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;

import jakarta.transaction.Transactional;

@Service
public class RegistrationService {
    
        private final EmailValidator emailValidator;
        private final UserService userService;
        private final ConfirmationTokenService confirmationTokenService;

        public RegistrationService(EmailValidator emailValidator,UserService userService, ConfirmationTokenService confirmationTokenService){
            this.emailValidator = emailValidator;
            this.userService = userService;
            this.confirmationTokenService = confirmationTokenService;
        }


        public String register(RegistrationRequest request) {
            boolean isValidEmail = emailValidator.test(request.getEmail());
            if(!isValidEmail){
                throw new IllegalStateException("email not valid");
            }
            return userService.signUpUser(new User(request.getFirstName(),request.getLastName(),request.getEmail(),request.getPassword(),UserRole.USER));
        }
        @Transactional
        public String confirmToken(String token){
            ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(()->
                                                                                            new IllegalStateException("token not found"));
             if(confirmationToken.getComfirmedAt() != null){
                throw new IllegalStateException("email already confirmed");
             }        
             
             LocalDateTime expiredAt = confirmationToken.getExpiredAt();

             if (expiredAt.isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("token expired");
            }

             confirmationTokenService.setConfirmedAt(token);
             userService.enableUser(confirmationToken.getUser().getEmail());
            return "confirmed";
             
        }


        
}
