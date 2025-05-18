package com.example.demo.service.impl;

import com.example.demo.entity.BaseEntity;
import com.example.demo.service.TrashService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;


public class TrashServiceImpl<T extends BaseEntity> implements TrashService<T> {

    private final JpaRepository<T, Long> repository;

    public TrashServiceImpl(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    // ✅ Kiểm tra đối tượng theo ID có tồn tại không, nếu không có thì ném ngoại lệ
    private T checkEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đối tượng với ID: " + id));
    }

    @Override
    public void restoreFromTrash(Long id) {
        try {
            T entity = checkEntityById(id);
            entity.setIsActive(1);
            entity.setDeletedAt(null);
            repository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi khôi phục đối tượng từ thùng rác: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePermanently(Long id) {
        try {
            checkEntityById(id);
            repository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa vĩnh viễn đối tượng: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllInTrash() {
        try {
            List<T> trashItems = repository.findAll().stream()
                    .filter(e -> e.getIsActive() == 0)
                    .toList();
            repository.deleteAll(trashItems);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa tất cả đối tượng trong thùng rác: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteExpiredTrash() {
        try {
            //tính Mốc thời gian 30 ngày trước kể từ hiện tại
            //ví dụ hnay là 17/5 thì expiredTime = 17/4
            LocalDateTime expiredTime = LocalDateTime.now().minusDays(30);
            List<T> expired = repository.findAll().stream()
                    .filter(e -> e.getIsActive() == 0 && e.getDeletedAt() != null && e.getDeletedAt().isBefore(expiredTime))
                    .toList();
            repository.deleteAll(expired);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa các đối tượng quá hạn trong thùng rác: " + e.getMessage(), e);
        }
    }
}
