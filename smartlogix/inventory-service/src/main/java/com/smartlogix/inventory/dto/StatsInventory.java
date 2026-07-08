package com.smartlogix.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsInventory {
    private long total;
    private long bajoStock;
    private long sinStock;
}
