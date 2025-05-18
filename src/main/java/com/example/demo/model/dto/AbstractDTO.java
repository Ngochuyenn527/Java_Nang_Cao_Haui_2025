package com.example.demo.model.dto;


import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.io.Serializable;
import java.time.LocalDateTime;

@javax.persistence.MappedSuperclass
@javax.persistence.EntityListeners(AuditingEntityListener.class)
@Data
public class AbstractDTO<T> implements Serializable {
    private Long id;
    private Integer isActive;

    private LocalDateTime deletedAt;
}

