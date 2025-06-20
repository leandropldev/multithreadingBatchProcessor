package com.devstack.multithreadbatchprocessor.service;

import com.devstack.multithreadbatchprocessor.entity.Product;
import com.devstack.multithreadbatchprocessor.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class MultiThreadingProductService {

    public final ProductRepository productRepository;
    public final SimpleProductService  simpleProductService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(6);

    public void executeProductIdInBatch(List<Long> productIds){
        List<List<Long>> batches = splitIntoBatches(productIds);
        List<CompletableFuture<Void>> futures = batches
                .stream()
                .map( batch -> CompletableFuture.runAsync(() -> processProductIds(batch), executorService))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void processProductIds(List<Long> batchProductIds) {
        System.out.println("Processing batch " + batchProductIds + " by thread " + Thread.currentThread().getName());
        batchProductIds.parallelStream()
                .forEach(this::fetchUpdateAndPublish);
    }

    private void fetchUpdateAndPublish(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        simpleProductService.updateDiscountPrice(product);
    }

    private List<List<Long>> splitIntoBatches(List<Long> productIds) {
        int totalSize = productIds.size();
        int batchNums = (totalSize + 100 - 1) / 100; // Calculate number of batches
        List<List<Long>> batches = new ArrayList<>();
        for(int i = 0; i < batchNums; i++){
            int start = i * 100;
            int end = Math.min(totalSize, (i+1) * 100);
            batches.add(productIds.subList(start, end));
        }
        return batches;
    }



}
