package com.gogo.psy.user.service;

import com.gogo.psy.user.pojo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryService{
    void safedecreaseStock(Long productId, Integer count);

    void decreasePessimistic(Long productId, Integer count);
}
