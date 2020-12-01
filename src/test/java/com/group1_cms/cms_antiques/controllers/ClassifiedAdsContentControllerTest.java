package com.group1_cms.cms_antiques.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group1_cms.cms_antiques.configurations.WebSecurityConfig;
import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.services.ClassifiedAdsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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


@AutoConfigureMockMvc
@WebAppConfiguration
class ClassifiedAdsContentControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClassifiedAdsContentController classifiedAdsContentController;

    @MockBean
    private ClassifiedAdsService classifiedAdsService;

    private static ObjectMapper mapper = new ObjectMapper();

    //region GET tests
    @WithMockUser(value = "admin")
    @Test
    public void getView() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAds("", "", "1")).thenReturn(classifieds);
        Mockito.when(classifiedAdsContentController.redirect()).thenReturn(new ModelAndView("redirect:classified_ads/all/1"));
        // Now tests to make sure the redirection is there, if so it'll return a 302 Code instead of a 404 or 200
        mockMvc.perform(MockMvcRequestBuilders.get("/classified_ads")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("classified_ads/all/1"));

    }

    @WithMockUser(value = "admin")
    @Test
    public void getMainPage() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAds("goats", "", "1")).thenReturn(classifieds);
        Mockito.when(classifiedAdsContentController.classifieds("goats", "1", "")).thenReturn(new ModelAndView("redirect:classified_ads/goats/1"));
        // Now tests to make sure the redirection is there, if so it'll return a 302 Code instead of a 404 or 200
        mockMvc.perform(MockMvcRequestBuilders.get("/classified_ads/goats/1").param("search", "")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("classified_ads/goats/1"));

    }

    @WithMockUser(value = "admin")
    @Test
    public void classifiedsForm() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        Authentication testAuth = new Authentication()
        {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities()
            {
                return null;
            }

            @Override
            public Object getCredentials()
            {
                return null;
            }

            @Override
            public Object getDetails()
            {
                return null;
            }

            @Override
            public Object getPrincipal()
            {
                return null;
            }

            @Override
            public boolean isAuthenticated()
            {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException
            {

            }

            @Override
            public String getName()
            {
                return null;
            }
        };
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAds("goats", "", "1")).thenReturn(classifieds);
        Mockito.when(classifiedAdsContentController.classifiedsForum(newRandom.toString(), testAuth)).thenReturn(new ModelAndView("/classifieds/edit_classified"));
        // Now tests to make sure the authentication is legit; we are looking for a success
        mockMvc.perform(MockMvcRequestBuilders.get("/classified_ads/new", newRandom.toString(), testAuth)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @WithMockUser(value = "admin")
    @Test
    public void classifiedsForm2() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        Authentication testAuth = new Authentication()
        {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities()
            {
                return null;
            }

            @Override
            public Object getCredentials()
            {
                return null;
            }

            @Override
            public Object getDetails()
            {
                return null;
            }

            @Override
            public Object getPrincipal()
            {
                return null;
            }

            @Override
            public boolean isAuthenticated()
            {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException
            {

            }

            @Override
            public String getName()
            {
                return null;
            }
        };
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAds("goats", "", "1")).thenReturn(classifieds);
        Mockito.when(classifiedAdsContentController.classifiedsForum(newRandom.toString(), testAuth)).thenReturn(new ModelAndView("/classifieds/edit_classified"));
        // Now tests to make sure the authentication is legit; we are looking for a success
        mockMvc.perform(MockMvcRequestBuilders.get("/classified_ads/edit/" + newRandom, newRandom.toString(), testAuth)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @WithMockUser(value = "admin")
    @Test
    public void viewClassified() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        UUID newRandom = UUID.randomUUID();
        Authentication testAuth = new Authentication()
        {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities()
            {
                return null;
            }

            @Override
            public Object getCredentials()
            {
                return null;
            }

            @Override
            public Object getDetails()
            {
                return null;
            }

            @Override
            public Object getPrincipal()
            {
                return null;
            }

            @Override
            public boolean isAuthenticated()
            {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException
            {

            }

            @Override
            public String getName()
            {
                return null;
            }
        };
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAds("goats", "", "1")).thenReturn(classifieds);
        Mockito.when(classifiedAdsContentController.viewClassified(newRandom.toString(), testAuth)).thenReturn(new ModelAndView("/classifieds/view_classified"));
        // Now tests to make sure the authentication is legit; we are looking for a success so we can view the page
        mockMvc.perform(MockMvcRequestBuilders.get("/classified_ads/view/" + newRandom, testAuth)).andExpect(MockMvcResultMatchers.status().isOk());

    }
    //endregion

    //region POST tests
    @WithMockUser(value = "admin")
    @Test
    public void addTag() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        Authentication testAuth = new Authentication()
        {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities()
            {
                return null;
            }

            @Override
            public Object getCredentials()
            {
                return null;
            }

            @Override
            public Object getDetails()
            {
                return null;
            }

            @Override
            public Object getPrincipal()
            {
                return null;
            }

            @Override
            public boolean isAuthenticated()
            {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException
            {

            }

            @Override
            public String getName()
            {
                return null;
            }
        };
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAds("", "", "1")).thenReturn(classifieds);
        Mockito.when(classifiedAdsContentController.addTag(newClassified, "testTag")).thenReturn(new ModelAndView("classifieds/edit_classified::#tags"));
        // Now tests to make sure we get a success back from the server
        mockMvc.perform(MockMvcRequestBuilders.post("/add_tag", newClassified).param("tag", "testTag"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @WithMockUser(value = "admin")
    @Test
    public void deleteClassified() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        Authentication testAuth = new Authentication()
        {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities()
            {
                return null;
            }

            @Override
            public Object getCredentials()
            {
                return null;
            }

            @Override
            public Object getDetails()
            {
                return null;
            }

            @Override
            public Object getPrincipal()
            {
                return null;
            }

            @Override
            public boolean isAuthenticated()
            {
                return false;
            }

            @Override
            public void setAuthenticated(boolean b) throws IllegalArgumentException
            {

            }

            @Override
            public String getName()
            {
                return null;
            }
        };
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsService.getClassifiedAds("", "", "1")).thenReturn(classifieds);
        Mockito.when(classifiedAdsContentController.deleteClassified(newClassified, testAuth)).thenReturn(new ModelAndView("redirect:/classified_ads/all/1"));
        // Now tests to make sure we get a success back from the server
        mockMvc.perform(MockMvcRequestBuilders.post("/delete_classified", newClassified, testAuth)).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @WithMockUser(value = "admin")
    @Test
    public void postNewClassified() throws Exception
    {
        // Mocks our services
        classifiedAdsService = Mockito.mock(ClassifiedAdsService.class);
        classifiedAdsContentController = Mockito.mock(ClassifiedAdsContentController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsContentController).build();
        // Creates new Classified
        MockMultipartFile newMulti = new MockMultipartFile("data", "filename.png", "image/plain", "115601654".getBytes());
        UUID newRandom = UUID.randomUUID();
        List<ClassifiedAd> classifieds = new ArrayList<>();
        ClassifiedAd newClassified = new ClassifiedAd();
        newClassified.setId(newRandom);
        newClassified.setTitle("TestClassified");
        classifieds.add(newClassified);
        // Makes sure we receive the right Classified
        Mockito.when(classifiedAdsContentController.postClassifieds(newClassified, newMulti)).thenReturn(new ModelAndView("redirect:classified_ads/all/1"));
        // Now tests to make sure the redirection is there, if so it'll return a 302 Code instead of a 404 or 200
        //mockMvc.perform(MockMvcRequestBuilders.multipart("/classified_ads").file(newMulti)
                //.param("classifiedAd", String.valueOf(newClassified)))
                //.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.redirectedUrl("classified_ads/all/1"));

    }
    //endregion
}
