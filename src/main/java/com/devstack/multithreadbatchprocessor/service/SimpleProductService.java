package com.devstack.multithreadbatchprocessor.service;

import com.devstack.multithreadbatchprocessor.entity.Product;
import com.devstack.multithreadbatchprocessor.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleProductService {

    private final ProductRepository productRepository;

    public String resetProductTable(){
        productRepository.findAll()
                .forEach(product -> {
                    product.setOfferApplied(false);
                    product.setPriceAfterDiscount(product.getPrice());
                    product.setDiscountPercentage(0);
                    productRepository.save(product);
                });
        return "Product table reset successfully";
    }

    public List<Long> getProductIds() {
        return productRepository.findAll()
                .stream()
                .map(Product::getId)
                .toList();
    }

    @Transactional
    public void processProductIds(List<Long> productIds) {
        productIds.parallelStream()
                .forEach(this::fetchAndUpdate);
    }

    private void fetchAndUpdate(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        updateDiscountPrice(product);
        productRepository.save(product);
    }

    public void updateDiscountPrice(Product product) {
        double price = product.getPrice();
        int discountPercentage = (price >= 1000) ? 10 : (price >= 500) ? 5 : 0;
        double priceAfterDiscount = price - (price * discountPercentage / 100);
        priceAfterDiscount = Math.round(priceAfterDiscount * 100.0) / 100.0; // Round to 2 decimal places
        if(discountPercentage > 0) {
            product.setOfferApplied(true);
        }
        product.setDiscountPercentage(discountPercentage);
        product.setPriceAfterDiscount(priceAfterDiscount);
    }
}
