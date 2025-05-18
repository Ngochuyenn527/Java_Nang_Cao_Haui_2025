package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    //sử dụng cascade  → Khi thêm, sửa, xóa RoleEntity, Hibernate sẽ tự động thao tác với UserEntity.
    //orphanRemoval = true →  Nếu một UserEntity bị xóa khỏi danh sách userEntities, nó sẽ bị xóa khỏi database
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserEntity> userEntities = new ArrayList<UserEntity>();
}

