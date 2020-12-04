package com.group1_cms.cms_antiques.services;

import com.group1_cms.cms_antiques.models.Category;
import com.group1_cms.cms_antiques.models.CategoryDto;
import com.group1_cms.cms_antiques.repositories.CategoryRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class CategoryServiceTest
{
    private CategoryRepository categoryRepository;

    @Test
    void getAllCategories()
    {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(categoryRepository);
        List<Category> newList = new ArrayList<>();
        Mockito.when(categoryRepository.getCategories()).thenReturn(newList);
        Assert.assertEquals(newList, categoryService.getAllCategories());
    }

    @Test
    void getCategoryById()
    {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        CategoryDto newDTO = Mockito.mock(CategoryDto.class);
        CategoryService categoryService = new CategoryService(categoryRepository);
        Category newCat = new Category();
        newCat.setId(UUID.randomUUID());
        newCat.setCreatedOn(ZonedDateTime.now());
        newCat.setName("Tron");
        newCat.setModifiedOn(ZonedDateTime.now());
        Mockito.when(categoryRepository.getCategoryById(newCat.getId().toString())).thenReturn(newCat);
        categoryService.getCategoryById(newCat.getId().toString());
        //Assert.assertEquals(new CategoryDto(newCat.getId(), newCat.getName(), newCat.getCreatedOn(), newCat.getModifiedOn()), categoryService.getCategoryById(newCat.getId().toString()));
    }

    @Test
    void saveCategory()
    {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(categoryRepository);
        Category newCat = new Category();
        newCat.setId(UUID.randomUUID());
        Mockito.when(categoryRepository.save(newCat)).thenReturn(newCat);
        Assert.assertEquals(newCat, categoryService.saveCategory(newCat));
    }

    @Test
    void deleteCategoryFromDbById()
    {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(categoryRepository);
        Category newCat = new Category();
        Mockito.when(categoryRepository.deleteCategoryById("10")).thenReturn(0);
        Assert.assertEquals(false, categoryService.deleteCategoryFromDbById("10"));
    }

    @Test
    void testSaveCategory()
    {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(categoryRepository);
        CategoryDto newCatDTO = new CategoryDto();
        UUID newUUID = UUID.randomUUID();
        newCatDTO.setId(newUUID.toString());
        Category newCat = new Category();
        newCat.setId(newUUID);
        Mockito.when(categoryRepository.save(newCat)).thenReturn(newCat);
        Mockito.when(categoryRepository.getCategoryById(newUUID.toString())).thenReturn(newCat);
        Assert.assertEquals(newCat, categoryService.saveCategory(newCatDTO));
    }

    @Test
    void testSaveCategory2()
    {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        CategoryService categoryService = new CategoryService(categoryRepository);
        CategoryDto newCatDTO = new CategoryDto();
        UUID newUUID = UUID.randomUUID();
        //newCatDTO.setId(newUUID.toString());
        Category newCat = new Category();
        newCat.setId(newUUID);
        Mockito.when(categoryRepository.save(newCat)).thenReturn(newCat);
        //Mockito.when(categoryRepository.getCategoryById(newUUID.toString())).thenReturn(newCat);
        Assert.assertEquals(null, categoryService.saveCategory(newCatDTO));
    }
}