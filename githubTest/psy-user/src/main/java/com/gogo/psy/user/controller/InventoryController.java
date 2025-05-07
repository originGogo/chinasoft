package com.gogo.psy.user.controller;

import com.gogo.psy.common.Result.Result;
import com.gogo.psy.user.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Resource
    private InventoryService inventoryService;

    @PostMapping("/decreaseOptimistic")
    @Operation(summary = "乐观锁实现")
    public Result decrease(Long productId, Integer count) {
        inventoryService.safedecreaseStock(productId, count);
        return Result.ok("购买成功");
    }

    @PostMapping("/decreasePessimistic")
    @Operation(summary = "悲观锁实现")
    public Result decreasePessimistic(Long productId, Integer count) {
        inventoryService.decreasePessimistic(productId, count);
        return Result.ok("购买成功");
    }


}
