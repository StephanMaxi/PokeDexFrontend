package com.example.demo.Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@CrossOrigin
@RestController
//makes a custom path
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    //this is using dependecy injection 
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


//these will be our api methods GET/POST/PUT/DELETE
    @GetMapping()
	public List<User> getUsers(){
		return userService.getUsers();
	}

    @PostMapping()
    /* The body of the request is passed through an HttpMessageConverter
     to resolve the method argument depending on the content type of the request. */
    public void registerNewUser(@RequestBody User user){
        userService.addNewUser(user);
    }

    @DeleteMapping(path = "{userID}")
    public void deleteUser(@PathVariable("userID") Long userId){
        userService.deleteUser(userId);
    }
    //@PathVAriable Annotation which indicates that a method parameter should be bound to a URI template variable. 
    @PutMapping(path = "{userID}")
    public void updateStudent(
                                @PathVariable("userID") Long userId,
                                //this means that it is not reqiured
                                @RequestParam(required = false) String name,
                                @RequestParam(required = false) String email){
        userService.updateUser(userId,name,email);
    }
}
