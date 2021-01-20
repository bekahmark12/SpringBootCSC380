package com.example.demo.models;

import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Entity
@Table
public class ServiceRequest extends RepresentationModel<ServiceRequest> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    @Column
    String requestDetails;
    @Column
    boolean isAccepted = false;
    @Column
    boolean isCompleted = false;
    @Column
    String dateNeeded;
    @Column
    String timeNeeded;
    @Column
    int RequesterId;
    @Column
    String address;
    @Column
    int providerId = 0;

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getRequesterId() {
        return RequesterId;
    }

    public void setRequesterId(int requesterId) {
        RequesterId = requesterId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean open) {
        isAccepted = open;
    }

    public String getDateNeeded() {
        return dateNeeded;
    }

    public void setDateNeeded(String dateNeeded) {
        this.dateNeeded = dateNeeded;
    }

    public String getTimeNeeded() {
        return timeNeeded;
    }

    public void setTimeNeeded(String timeNeeded) {
        this.timeNeeded = timeNeeded;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "id=" + id +
                ", requestDetails='" + requestDetails + '\'' +
                ", isAccepted=" + isAccepted +
                ", isCompleted=" + isCompleted +
                ", dateNeeded='" + dateNeeded + '\'' +
                ", timeNeeded='" + timeNeeded + '\'' +
                ", RequesterId=" + RequesterId +
                ", address='" + address + '\'' +
                ", providerId=" + providerId +
                '}';
    }
}
