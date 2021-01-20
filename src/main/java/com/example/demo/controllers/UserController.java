package com.example.demo.controllers;

import com.example.demo.Services.NotificationService;
import com.example.demo.models.Intent;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repos.RequestRepo;
import com.example.demo.repos.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private UserRepo repo;

    @Autowired
    private RequestRepo requests;

    public void setLinks(User u) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(UserController.class).slash(u.getId()).withSelfRel());
        u.add(links);
    }
    //CREATE
//    @PreAuthorize("hasAnyRole()")
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody User u) {
        setLinks(u);
        Intent intent = u.getIntent();
        if (intent.ordinal() == 0) {
            u.authorities.add(Role.USER);
        } else {
            u.authorities.add(Role.PROVIDER);
        }
        if(! u.getPassword().startsWith("$2a")) {
            u.setPassword(passwordEncoder.encode(u.getPassword()));
        }
        repo.save(u);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }
    //DELETE
    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteUser(@PathVariable int id) {
        repo.deleteById(id);
        return HttpStatus.ACCEPTED;
    }
    //FIND ALL
    @GetMapping("")
    public List<ResponseEntity<Object>> getAllUsers() {
        List<ResponseEntity<Object>> responses = new ArrayList<>();
        List<User> users = repo.findAll();
        for(User u : users) {
            setLinks(u);
            responses.add(new ResponseEntity<>(u, HttpStatus.FOUND));
        }
        return responses;
    }
    //FIND ONE
    @GetMapping("/{id}")
    public ResponseEntity<Object> findUserById(@PathVariable int id) {
        User u = repo.findOneById(id);
        setLinks(u);
        return new ResponseEntity<>(u, HttpStatus.FOUND);
    }
    //FIND ALL CARE PROVIDERS
    @GetMapping("/providers")
    public List<ResponseEntity<Object>> getAllProviders() {
        List<ResponseEntity<Object>> responses = new ArrayList<>();
        List<User> users = repo.findAll();
        for(User u : users) {
            if(u.getIntent() == Intent.PROVIDING) {
                setLinks(u);
                responses.add(new ResponseEntity<>(u, HttpStatus.FOUND));
            }
        }
        return responses;
    }
    //FIND ALL CARE REQUESTERS
    @GetMapping("/requesters")
    public List<ResponseEntity<Object>> getAllRequesters() {
        List<ResponseEntity<Object>> responses = new ArrayList<>();
        List<User> users = repo.findAll();
        for(User u : users) {
            if(u.getIntent() == Intent.SEEKING) {
                setLinks(u);
                responses.add(new ResponseEntity<>(u, HttpStatus.FOUND));
            }
        }
        return responses;
    }
    //CHANGE NAME
    @PatchMapping("/change-name/{id}/{name}")
    public ResponseEntity<Object> changeName(@PathVariable int id, @PathVariable String name) {
        User toupdate = repo.findOneById(id);
        toupdate.setName(name);
        repo.save(toupdate);
        setLinks(toupdate);
        return new ResponseEntity<>(toupdate, HttpStatus.OK);
    }
    //CHANGE ADDRESS
    @PatchMapping("/change-name/{id}/{address}")
    public ResponseEntity<Object> changeAddress(@PathVariable int id, @PathVariable String address) {
        User toupdate = repo.findOneById(id);
        toupdate.setAddress(address);
        repo.save(toupdate);
        setLinks(toupdate);
        return new ResponseEntity<>(toupdate, HttpStatus.OK);
    }

    //CHANGE TO TEMPORARY PASSWORD
    @PatchMapping("/temporary-password/{id}")
    public ResponseEntity<Object> getTemporaryPassword(@PathVariable int id){
        User u = repo.findOneById(id);
        String newTempPassword = RandomStringUtils.randomAlphanumeric(10);
        u.setPassword(passwordEncoder.encode(newTempPassword));
        repo.save(u);
        String message = "Your new temporary password is: " + newTempPassword;
        notificationService.sendNotification(u, message);
        return new ResponseEntity<>(u, HttpStatus.OK);
    }
}
