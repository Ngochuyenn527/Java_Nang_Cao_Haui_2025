package com.example.demo.service.impl;


import com.example.demo.entity.ApartmentEntity;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.entity.SectorEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// một scheduler (bộ lập lịch) trong Spring Boot có chức năng tự động dọn "thùng rác" theo lịch định kỳ.
@EnableScheduling //Bật tính năng lập lịch (scheduling) trong Spring Boot
@Component
@Service
public class AutoDeleteScheduler {

    private final TrashServiceImpl<BuildingEntity> buildingTrashService;
    private final TrashServiceImpl<SectorEntity> sectorTrashService;
    private final TrashServiceImpl<ApartmentEntity> apartmentTrashService;

    public AutoDeleteScheduler(TrashServiceImpl<BuildingEntity> buildingTrashService,
                               TrashServiceImpl<SectorEntity> sectorTrashService,
                               TrashServiceImpl<ApartmentEntity> apartmentTrashService) {
        this.buildingTrashService = buildingTrashService;
        this.sectorTrashService = sectorTrashService;
        this.apartmentTrashService = apartmentTrashService;
    }

    //Mỗi phút một lần, hàm runAutoCleanup() sẽ Chạy ngầm tự động trong ứng dụng,
    //Hàm này (Xóa vĩnh viễn các bản ghi quá hạn trong thùng rác có status = 0 và deletedAt < LocalDateTime.now().minusDays(30).)
    @Scheduled(cron = "0 * * * * ?") //được lập lịch chạy mỗi phút một lần.
    public void runAutoCleanup() {
        buildingTrashService.deleteExpiredTrash();
        sectorTrashService.deleteExpiredTrash();
        apartmentTrashService.deleteExpiredTrash();
        System.out.println("✅ Auto cleanup completed at " + java.time.LocalDateTime.now());
    }
}

