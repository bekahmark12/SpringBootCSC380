package com.example.demo.controllers;

import com.example.demo.models.Intent;
import com.example.demo.models.ServiceRequest;
import com.example.demo.models.User;
import com.example.demo.repos.RequestRepo;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/request")

public class ServiceRequestController {
    @Autowired
    private RequestRepo repo;

    @Autowired
    private UserRepo users;

    public void setLinks(ServiceRequest r) {
        List<Link> links = new ArrayList<>();
        links.add(WebMvcLinkBuilder.linkTo(ServiceRequestController.class).slash(r.getId()).withSelfRel());
        r.add(links);
    }

    //CREATE
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody ServiceRequest r){
        setLinks(r);
        repo.save(r);
        return new ResponseEntity<>(r, HttpStatus.CREATED);
    }
    //DELETE
    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteRequest(@PathVariable int id) {
        repo.deleteById(id);
        return HttpStatus.ACCEPTED;
    }
    //FIND ONE
    @GetMapping("/{id}")
    public ResponseEntity<Object> findRequestById(@PathVariable int id) {
        ServiceRequest r = repo.findOneById(id);
        setLinks(r);
        return new ResponseEntity<>(r, HttpStatus.FOUND);
    }
    //VIEW ALL
    @GetMapping("")
    public List<ResponseEntity<Object>> getAllRequests() {
        List<ResponseEntity<Object>> responses = new ArrayList<>();
        List<ServiceRequest> requests = repo.findAll();
        for(ServiceRequest r : requests) {
            setLinks(r);
            responses.add(new ResponseEntity<>(r, HttpStatus.FOUND));
        }
        return responses;
    }
    //SET AS COMPLETED
    @PatchMapping("/completed/{userId}/{id}")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public ResponseEntity<Object> markCompleted(@PathVariable int id, @PathVariable int userId) throws IllegalAccessException {
        User u = users.findOneById(userId);
        if(u.getIntent() == Intent.PROVIDING) {
            ServiceRequest r = repo.findOneById(id);
            r.setCompleted(true);
            repo.save(r);
            setLinks(r);
            return new ResponseEntity<>(r, HttpStatus.ACCEPTED);
        } else {
            //Throw a 403 error here?
            throw new IllegalAccessException();
        }
    }
    //SET AS ACCEPTED
    @PatchMapping("/accepted/{userId}/{id}")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public ResponseEntity<Object> markAccepted(@PathVariable int id, @PathVariable int userId) throws IllegalAccessException {
        User u = users.findOneById(userId);
        if(u.getIntent() == Intent.PROVIDING) {
            ServiceRequest r = repo.findOneById(id);
            r.setAccepted(true);
            repo.save(r);
            setLinks(r);
            return new ResponseEntity<>(r, HttpStatus.ACCEPTED);
        } else {
            //Throw a 403 error here?
            throw new IllegalAccessException();
        }
    }
    //ADD REQUEST TO PROVIDER
    @PutMapping("/add-to-provider/{providerId}/{id}")
    @PreAuthorize("hasAuthority('PROVIDER')")
    public ResponseEntity<Object> addRequestToProvider(@PathVariable int id, @PathVariable int providerId) throws IllegalAccessException {
        User u = users.findOneById(providerId);
        if(u.getIntent() == Intent.PROVIDING) {
            ServiceRequest r = repo.findOneById(id);
            List<ServiceRequest> requests = u.getServiceRequests();
            requests.add(r);
            r.setProviderId(providerId);
            users.save(u);
            repo.save(r);
            setLinks(r);
            return new ResponseEntity<>(r, HttpStatus.ACCEPTED);
        } else {
            //Throw a 403 error here?
            throw new IllegalAccessException();
        }
    }
}
