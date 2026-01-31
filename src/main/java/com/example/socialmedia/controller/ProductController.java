package com.example.socialmedia.controller;

import com.example.socialmedia.config.AppConstants;
import com.example.socialmedia.model.Product;
import com.example.socialmedia.payload.ProductDTO;
import com.example.socialmedia.payload.ProductResponse;
import com.example.socialmedia.service.ProductService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId, @RequestBody ProductDTO productDTO) {
        ProductDTO addProduct=productService.addProduct(categoryId,productDTO);
        return new ResponseEntity<>(addProduct, HttpStatus.CREATED);
    }
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY)String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_ORDER)String sortOrder
    ){
        ProductResponse response=productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public  ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId){
        ProductResponse response=productService.searchByCategory(categoryId );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
    public  ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword){
        ProductResponse response=productService.searchProductByKeyword(keyword );
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO>updateProduct(@PathVariable Long productId,@RequestBody ProductDTO productDTO){
        ProductDTO savedDTO=productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(savedDTO,HttpStatus.OK);
    }
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO>deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO=productService.deleteProduct(productId);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO>updateProductImage(@PathVariable Long productId,
                                                        @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO productDTO=productService.updateProductImage(productId,image);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }


}
