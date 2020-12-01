package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Category;
import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories(){
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for(Category category: categoryRepository.getCategories()){
            categoryDtoList.add(new CategoryDto(category.getId(), category.getName(), category.getCreatedOn(), category.getModifiedOn()));
        }
        return categoryDtoList;
    }

    public CategoryDto getCategoryById(String id){
        Category categoryFromDb = categoryRepository.getCategoryById(id);
        if(categoryFromDb.getId() != null){
            return new CategoryDto(categoryFromDb.getId(), categoryFromDb.getName(), categoryFromDb.getCreatedOn(), categoryFromDb.getModifiedOn());
        }
        return null;
    }

    public Category saveCategory(CategoryDto category){
        Category categoryFromDb;
        if(category.getId().equals("0")){
            categoryFromDb = new Category();
            categoryFromDb.setId(UUID.randomUUID());
            categoryFromDb.setName(category.getCategoryName());
            categoryFromDb.setCreatedOn(ZonedDateTime.now());
            categoryFromDb.setModifiedOn(ZonedDateTime.now());
        }
        else{
            categoryFromDb = categoryRepository.getCategoryById(category.getId());
            categoryFromDb.setName(category.getCategoryName());
            categoryFromDb.setModifiedOn(ZonedDateTime.now());
        }

        return categoryRepository.save(categoryFromDb);
    }

    public boolean deleteCategoryFromDbById(String id){
        if(categoryRepository.deleteCategoryById(id) > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public Category saveCategory(Category category){
        Category categoryFromDb = categoryRepository.getCategoryById(category.getId().toString());
        if(categoryFromDb == null){
            category.setId(UUID.randomUUID());
            category.setCreatedOn(ZonedDateTime.now());
            category.setModifiedOn(ZonedDateTime.now());
        }
        else{
            category.setId(categoryFromDb.getId());
            category.setModifiedOn(ZonedDateTime.now());
        }

        return categoryRepository.save(category);
    }
}
