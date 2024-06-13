package com.example.blog.controller;

import com.example.blog.payloads.ApiResponse;
import com.example.blog.payloads.CategoryDto;
import com.example.blog.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @PostMapping("/add")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDt) {
        CategoryDto createCategory = categoryService.createCategory(categoryDt);
        return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable Integer categoryId) {
        CategoryDto updateCategory = categoryService.updateCategory(categoryDto,categoryId);
        return new ResponseEntity<>(updateCategory,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{catid}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catid) {
        categoryService.deleteCategory(catid);
        return new ResponseEntity<>(new ApiResponse("category is deleted successfully",true),HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CategoryDto>> getAllCategory() {
        List<CategoryDto> categoryDtos=categoryService.getCatergories();
        return new ResponseEntity<>(categoryDtos,HttpStatus.OK);
    }

    @GetMapping("/get/{catid}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Integer catid) {

        CategoryDto categoryDto = categoryService.getCategory(catid);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }

}
