package com.advanceit.bankservice.client;

import com.advanceit.bankservice.async.dto.ClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", url = "http://localhost:8081")
public interface ClientClient {

    @GetMapping("/clients/{id}")
    ClientDTO getClientById(@PathVariable("id") Long id);
}
