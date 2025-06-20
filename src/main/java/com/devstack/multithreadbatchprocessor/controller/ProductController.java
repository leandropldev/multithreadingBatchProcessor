package com.devstack.multithreadbatchprocessor.controller;

import com.devstack.multithreadbatchprocessor.service.MultiThreadingProductService;
import com.devstack.multithreadbatchprocessor.service.SimpleProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {

    private final SimpleProductService simpleProductService;
    private final MultiThreadingProductService multiThreadingProductService;

    @GetMapping("reset")
    public ResponseEntity<String> resetProductTable() {
        return ResponseEntity.ok(simpleProductService.resetProductTable());
    }

    @GetMapping("ids")
    public ResponseEntity<List<Long>> getProductIds() {
        return ResponseEntity.ok(simpleProductService.getProductIds());
    }

    @PostMapping("simple-process")
    public ResponseEntity<String> simpleProcessProductIds(@RequestBody List<Long> productsIds) {
        simpleProductService.processProductIds(productsIds);
        return ResponseEntity.ok("Products processed successfully");
    }

    @PostMapping("multithreading-process")
    public ResponseEntity<String> multiThreadingProcessProductIds(@RequestBody List<Long> productsIds) {
        multiThreadingProductService.executeProductIdInBatch(productsIds);
        return ResponseEntity.ok("Products processed successfully");
    }
}
