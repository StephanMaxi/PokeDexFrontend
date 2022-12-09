package com.example.demo.Models;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service

public class UserService implements UserDetailsService{

    private final static String   USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        //uses the userrepo interface that we made to implment the find by email fucntion
        return userRepository.findUserByEmail(email)
        .orElseThrow(()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
		return userRepository.findAll();
	}



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
    public void updateStudent(Long userId, String name, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalStateException("user with id " + userId + " does not exists"));

        if(name != null && name.length() > 0 && !Objects.equals(user.getName(),name)){
            user.setName(name);
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
