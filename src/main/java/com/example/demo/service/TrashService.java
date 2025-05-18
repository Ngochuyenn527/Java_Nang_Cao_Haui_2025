package com.example.demo.service;

import com.example.demo.entity.BaseEntity;

public interface TrashService<T extends BaseEntity> {

    void restoreFromTrash(Long id);                  // khôi phục

    void deletePermanently(Long id);                 // xóa vĩnh viễn 1 item

    void deleteAllInTrash();          // xóa vĩnh viễn tất cả

    void deleteExpiredTrash();        // xóa nếu quá 30 ngày
}

