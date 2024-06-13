package com.example.blog.services;

import com.example.blog.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {
    public CategoryDto createCategory(CategoryDto categoryDto);
    public CategoryDto updateCategory(CategoryDto categoryDto,Integer categoryId);
    public void deleteCategory(Integer categoryId);
    public CategoryDto getCategory(Integer categoryId);
    public List<CategoryDto> getCatergories();
}
