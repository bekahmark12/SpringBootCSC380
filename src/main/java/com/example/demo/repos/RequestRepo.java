package com.example.demo.repos;

import com.example.demo.models.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.invoke.SerializedLambda;
import java.security.Provider;
import java.util.List;

public interface RequestRepo extends JpaRepository<ServiceRequest, Integer> {
    ServiceRequest findOneById(int id);

    ServiceRequest findByAddressIsContaining(String addressSegment);

    @Query("SELECT r FROM ServiceRequest r WHERE r.isAccepted = true AND r.isCompleted=false ")
    List<ServiceRequest> queryByAcceptedAndNotCompleted();
}
