package com.group1_cms.cms_antiques.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group1_cms.cms_antiques.configurations.FileUploadUtil;
import com.group1_cms.cms_antiques.configurations.WebSecurityConfig;
import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.ItemImage;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.ClassifiedAdsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
class ClassifiedAdsContentControllerTest
{
    private ClassifiedAdsContentController classifiedAdsContentController;
    private ClassifiedAdsService classifiedAdsService;

    private static ObjectMapper mapper = new ObjectMapper();

    //region GET tests
    @WithMockUser(value = "admin")
    @Test
    public void getView() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAdById(newRandom)).thenReturn(newClassified);
        Mockito.when(newAuth.getName()).thenReturn("Tron");
        classifiedAdsContentController.viewClassified(newRandom.toString(), newAuth);

    }

    @WithMockUser(value = "admin")
    @Test
    public void getViewNonUser() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAdById(newRandom)).thenReturn(newClassified);
        Mockito.when(newAuth.getName()).thenReturn("Dave");
        classifiedAdsContentController.viewClassified(newRandom.toString(), newAuth);

    }

    @WithMockUser(value = "admin")
    @Test
    public void getMainPage() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);
        Mockito.when(classifiedAdsService.getClassifiedAds("", "", "1")).thenReturn(classifieds);
        classifiedAdsContentController.classifieds(null, "1", null);
    }

    @WithMockUser(value = "admin")
    @Test
    public void getMainPage2() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);
        classifiedAdsContentController.redirect();
    }

    @WithMockUser(value = "admin")
    @Test
    public void classifiedsForm() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);
        Mockito.when(newAuth.getName()).thenReturn("Tron");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        Mockito.when(classifiedAdsService.getClassifiedAdById(newRandom)).thenReturn(newClassified);
        classifiedAdsContentController.classifiedsForm(newRandom.toString(), newAuth);
    }

    @WithMockUser(value = "admin")
    @Test
    public void classifiedsFormNotCreator() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);
        Mockito.when(newAuth.getName()).thenReturn("Dave");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        Mockito.when(classifiedAdsService.getClassifiedAdById(newRandom)).thenReturn(newClassified);
        classifiedAdsContentController.classifiedsForm(newRandom.toString(), newAuth);
    }

    //endregion

    //region POST tests
    @WithMockUser(value = "admin")
    @Test
    public void addTag() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);

        classifiedAdsContentController.addTag(newClassified, "Cars");
    }

    @WithMockUser(value = "admin")
    @Test
    public void deleteClassified() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);

        //classifiedAdsContentController.deleteClassified(newClassified, newAuth);
    }

    @WithMockUser(value = "admin")
    @Test
    public void postNewClassified() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);
        MultipartFile newFile = Mockito.mock(MultipartFile.class);
        ItemImage newImage = Mockito.mock(ItemImage.class);
        Mockito.when(newImage.getFileName()).thenReturn("whoops");
        Mockito.when(newFile.getOriginalFilename()).thenReturn("whoops");
        Item newItem = new Item();
        newItem.setItemImage(newImage);
        newClassified.setItem(newItem);

        //classifiedAdsContentController.postClassifieds(newClassified, newFile);
    }
    //endregion
}
