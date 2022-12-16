package com.example.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.RegistrationRequest;
import com.example.demo.service.RegistrationService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/registration")
public class RegistrationController {
    
   
    private RegistrationService registrationService;
    //constructor
    public RegistrationController(RegistrationService registrationService ){
        this.registrationService = registrationService;
    }
     //refernce to serface
    @PostMapping()
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token ){
        return registrationService.confirmToken(token);
    }

}
