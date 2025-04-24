package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends AbstractDTO<RoleDTO> {

    private static final long serialVersionUID = 5830885581031027382L;

    private String name;
    private String code;
}

