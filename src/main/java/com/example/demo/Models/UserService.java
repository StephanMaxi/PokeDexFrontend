package com.example.demo.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;

import jakarta.transaction.Transactional;

@Service

public class UserService implements UserDetailsService{

    private final static String   USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        //uses the userrepo interface that we made to implment the find by email fucntion
        return userRepository.findUserByEmail(email)
        .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }
    //sign up user fucntion
    public String signUpUser(User user){
        //boolean to see if user exists
        boolean userExist = userRepository.findUserByEmail(user.getEmail())
                            .isPresent();

        if(userExist){
            //TODO check of attributes are the same and
            //TODO if email not confirmed send confirmation email
            throw new IllegalStateException("email already taken");
        }
        //else
        //salts and hases the password
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        /*CrudRepository.save(S entity) : 
Saves a given entity. Use the returned instance for further operations as the 
save operation might have changed the entity instance completely. */
//saves the user using the CRUD repo interface
        userRepository.save(user);
        //generates the registration token
        String token = UUID.randomUUID().toString();
        //creates a new token object/table that joins to a user 
        ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            user
        );
        //uses the service to save the token
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        // TODO: SEND EMAIL
        return token;
    }
        //enables user
        public int enableUser(String email){
            return userRepository.enableUser(email);
        }


    @Autowired
    public UserService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,ConfirmationTokenService confirmationTokenService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }
    //gets users
    public List<User> getUsers(){
		return userRepository.findAll();
	}


    //addes users
    public void addNewUser(User user) {
       Optional<User> userOptional =  userRepository.findUserByEmail(user.getEmail());
       if(userOptional.isPresent()){
        throw new IllegalStateException("email taken");
       }
        userRepository.save(user);
    }



    public void deleteUser(Long userId) {
        
        boolean exists = userRepository.existsById(userId);
        if(!exists){
            throw new IllegalStateException("user with id " + userId + " does not exists");
        }
        userRepository.deleteById(userId);
    }

/*The jakarta.transaction.Transactional annotation provides the application the ability to declaratively control transaction boundaries on CDI managed beans,
 as well as classes defined as managed beans by the Jakarta EE specification,
 at both the class and method level where method level annotations override those at the class level. */
    @Transactional
    public void updateUser(Long userId, String name, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalStateException("user with id " + userId + " does not exists"));

        if(name != null && name.length() > 0 && !Objects.equals(user.getFirstname(),name)){
            user.setFirstname(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(user.getEmail(),email)){
            //A container object which may or may not contain a non-null value. If a value is present, 
            //isPresent() returns true. If no value is present, the object is considered empty and isPresent() returns false.
            Optional<User> userOptional =  userRepository.findUserByEmail(email);
            if(userOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }

    }

    
}
