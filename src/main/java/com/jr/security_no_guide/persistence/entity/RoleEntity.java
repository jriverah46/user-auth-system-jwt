package com.jr.security_no_guide.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_role;

    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;


    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "role_permissions",joinColumns = @JoinColumn(name = "id_role"),inverseJoinColumns = @JoinColumn(name = "id_permission"))
    Set<PermissionEntity>permissions=new HashSet<>();

}
