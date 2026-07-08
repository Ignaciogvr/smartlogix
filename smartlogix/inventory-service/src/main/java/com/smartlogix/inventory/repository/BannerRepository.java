package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByActivoTrueOrderByOrdenAsc();
}
