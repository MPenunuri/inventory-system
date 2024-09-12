package com.mapera.inventory_system.infrastructure.adapter.inbound.web.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductGetController productGetController;
    @Autowired
    private ProductPostController productPostController;
    @Autowired
    private ProductPatchController productPatchController;
    @Autowired
    private ProductDeleteController productDeleteController;

    @RequestMapping("/get")
    public ProductGetController getProductController() {
        return productGetController;
    }

    @RequestMapping("/post")
    public ProductPostController postProductController() {
        return productPostController;
    }

    @RequestMapping("/patch")
    public ProductPatchController patchProductController() {
        return productPatchController;
    }

    @RequestMapping("/delete")
    public ProductDeleteController deleteProductController() {
        return productDeleteController;
    }
}
