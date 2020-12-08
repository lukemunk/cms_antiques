package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.ItemImage;
import com.group1_cms.cms_antiques.models.User;
import com.group1_cms.cms_antiques.services.ClassifiedAdsService;

import org.junit.Assert;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.security.core.Authentication;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.InputStream;
import java.util.*;



@RunWith(MockitoJUnitRunner.class)
class ClassifiedAdsContentControllerTest
{
    private ClassifiedAdsContentController classifiedAdsContentController;
    private ClassifiedAdsService classifiedAdsService;


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
        ModelAndView newView = classifiedAdsContentController.viewClassified(newRandom.toString(), newAuth);
        
        Assert.assertEquals(newView.getViewName(), "classifieds/view_classified");
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
        ModelAndView newView = classifiedAdsContentController.viewClassified(newRandom.toString(), newAuth);
 
        Assert.assertEquals(newView.getViewName(), "classifieds/view_classified");
    }

    @WithMockUser(value = "admin")
    @Test
    public void getMainPage() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        //Authentication newAuth = Mockito.mock(Authentication.class);
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
        ModelAndView newView = classifiedAdsContentController.classifieds(null, "1", null);
        
        Assert.assertEquals(newView.getViewName(), "classifieds/classified_ads");
        Assert.assertEquals(newView.getModel().get("category"), "All");
        Assert.assertEquals(newView.getModel().get("search"), "");
    }

    @WithMockUser(value = "admin")
    @Test
    public void getMainPage2() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        //Authentication newAuth = Mockito.mock(Authentication.class);
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
        ModelAndView newView = classifiedAdsContentController.redirect();
        
        Assert.assertEquals(newView.getViewName(), "redirect:classified_ads/all/1");
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
        ModelAndView newView = classifiedAdsContentController.classifiedsForm(newRandom.toString(), newAuth);
    
        assertEquals(newView.getViewName(), "classifieds/edit_classified");
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
        ModelAndView newView = classifiedAdsContentController.classifiedsForm(newRandom.toString(), newAuth);
    
        Assert.assertEquals(newView.getViewName(), "redirect:/classified_ads/view/{id}");
    }
    
    @WithMockUser(value = "admin")
    @Test
    public void classifiedsFormNewClassified() throws Exception
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
        ModelAndView newView = classifiedAdsContentController.classifiedsForm(null, newAuth);
    
        Assert.assertEquals(newView.getViewName(), "classifieds/edit_classified");
        Assert.assertEquals(newView.getModel().get("newClassified"), true);
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
        //Authentication newAuth = Mockito.mock(Authentication.class);
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

        ModelAndView newView = classifiedAdsContentController.addTag(newClassified, "Cars");
    
        Assert.assertEquals(newView.getViewName(), "classifieds/edit_classified::#tags");
    }
    
    @WithMockUser(value = "admin")
    @Test
    public void removeTag() throws Exception{
    	// Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        //Authentication newAuth = Mockito.mock(Authentication.class);
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        newClassified.setTags(new ArrayList<String>());
        newClassified.getTags().add("Tag");
        
        User newUser = new User();
        newUser.setUsername("Tron");
        newClassified.setCreator(newUser);
        classifieds.add(newClassified);

        ModelAndView newView = classifiedAdsContentController.removeTag(newClassified, 0);
    
        Assert.assertEquals(newView.getViewName(), "classifieds/edit_classified::#tags");
        Assert.assertEquals(newClassified.getTags().size(), 0);
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
        Mockito.when(newAuth.getName()).thenReturn("Tron");
        Mockito.when(newAuth.getAuthorities()).thenReturn(new ArrayList<>());
        Mockito.when(classifiedAdsService.getClassifiedAdById(Mockito.any(UUID.class))).thenReturn(newClassified);
        
        ModelAndView newView = classifiedAdsContentController.deleteClassified(newClassified, newAuth);
        
        Assert.assertEquals(newView.getViewName(), "redirect:classified_ads/all/1");
        Mockito.verify(classifiedAdsService).deleteClassifiedAd(newClassified);
    }

    @WithMockUser(value = "admin")
    @Test
    public void postNewClassified() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = new ClassifiedAdsContentController(classifiedAdsService);
        //Authentication newAuth = Mockito.mock(Authentication.class);
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
        InputStream stream = Mockito.mock(InputStream.class);
        Mockito.when(newImage.getFileName()).thenReturn("whoops");
        Mockito.when(newFile.getOriginalFilename()).thenReturn("whoops");
        Mockito.when(newFile.getInputStream()).thenReturn(stream);
        Item newItem = new Item();
        newItem.setItemImage(newImage);
        newClassified.setItem(newItem);

        ModelAndView newView = classifiedAdsContentController.postClassifieds(newClassified, newFile);
        
        Assert.assertEquals(newView.getViewName(), "redirect:classified_ads/view/"+newClassified.getId());
        Mockito.verify(classifiedAdsService).saveClassifiedAd(newClassified);
    }
    //endregion
}
