package com.example.socialmedia.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "role_name")
    @Enumerated(EnumType.STRING)
    @ToString.Exclude
    private AppRole roleName;

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }
}
