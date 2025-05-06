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
public class RoleEntity extends BaseEntity {

    private static final long serialVersionUID = -6525302831793188081L;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    private List<UserEntity> user = new ArrayList<>();

    //update => sử dụng cascade  → Khi thêm, sửa, xóa BuildingEntity, Hibernate sẽ tự động thao tác với RentAreaEntity.
    //orphanRemoval = true → Nếu một RentAreaEntity bị loại khỏi danh sách rentAreaEntities, nó cũng bị xóa khỏi database.
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UserEntity> userEntities = new ArrayList<UserEntity>();
}

