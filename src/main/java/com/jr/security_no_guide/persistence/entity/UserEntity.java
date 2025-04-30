package com.jr.security_no_guide.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    private String username;
    private String email;
    private String password;
    @Column(name = "is_Enabled")
    private Boolean isEnabled;
    @Column(name = "account_No_Locked")
    private Boolean accountNoLocked;
    @Column(name = "account_No_Expired")
    private Boolean accountNoExpired;
    @Column(name = "credential_No_Expired")
    private Boolean credentialNoExpired;

    @Builder.Default
    @ManyToMany (fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "id_user"),inverseJoinColumns = @JoinColumn(name = "id_role"))
    Set<RoleEntity> roles=new HashSet<>();
}
