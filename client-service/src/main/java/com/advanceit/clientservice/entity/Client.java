package com.advanceit.clientservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("CLIENT")
public class Client extends Person {

    private String clientId;
    private String password;
    private boolean status;



}
