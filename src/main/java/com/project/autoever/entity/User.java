package com.project.autoever.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String account;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "resident_number", unique = true, nullable = false, length = 13)
    @Size(min = 13, max = 13, message = "주민등록번호는 13자리여야 합니다.")
    private String residentNumber;
    
    @Column(name = "phone_number", nullable = false, length = 11)
    @Size(min = 11, max = 11, message = "핸드폰 번호는 11자리여야 합니다.")
    private String phoneNumber;
    
    @Column(nullable = false)
    private String address;
}
