package com.gogo.psy.user.service.impl;

import com.gogo.psy.user.pojo.model.Product;
import com.gogo.psy.user.repository.ProductRepository;
import com.gogo.psy.user.service.InventoryService;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Resource
    private ProductRepository  productRepository;

    private static final Integer MAX_RETRY = 10;
    @Override
    public void safedecreaseStock(Long productId, Integer count) {
        for (int i = 0; i < MAX_RETRY; i++){
            try{
                decreaseStock(productId, count);
                return;
            }catch (Exception e){
                if (i == MAX_RETRY - 1){
                    throw new RuntimeException("库存扣减失败");
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {

                }
            }
        }
    }

    @Transactional
    public void decreaseStock(Long productId, Integer count) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        if (product.getStock() < count){
            throw new RuntimeException("库存不足");
        }
        product.setStock(product.getStock() - count);
        //update version会+1
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void decreasePessimistic(Long productId, Integer count) {
        try{
            Product product = productRepository.findByIdWithLock(productId);
            if (product.getStock() > 0){
                product.setStock(product.getStock() - count);
                productRepository.save(product);
            }else{
                throw new RuntimeException("库存不足");
            }
        }catch (Exception e){
            throw new RuntimeException("库存扣减失败");
        }
    }

}
