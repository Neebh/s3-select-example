package com.example.tutorials.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Getter
@AllArgsConstructor
@Setter
@Entity
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String email;

    private String name;

    private String password;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] bloomFilter;



   
    
}
