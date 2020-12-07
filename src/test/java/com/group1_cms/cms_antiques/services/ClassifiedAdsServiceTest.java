package com.group1_cms.cms_antiques.services;



import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.repositories.ClassifiedAdsRepository;

@AutoConfigureMockMvc
@WebAppConfiguration
class ClassifiedAdsServiceTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ClassifiedAdsService classifiedAdsService;
	@MockBean
	private ClassifiedAdsRepository classifiedAdsRepository;
	
	List<ClassifiedAd> classifieds;
	
	@Before
	public void before() {
		System.out.println("Before");
		classifieds = new ArrayList<ClassifiedAd>();
		classifiedAdsRepository = Mockito.mock(ClassifiedAdsRepository.class);
		classifiedAdsService = new ClassifiedAdsService(classifiedAdsRepository);
		mockMvc = MockMvcBuilders.standaloneSetup(classifiedAdsService).build();
        
		for(int i=1; i<20; i++) {
			ClassifiedAd classified = new ClassifiedAd();
			classified.setTitle("TestClassified "+i);
			classifieds.add(classified);
		}
		
		Mockito.when(classifiedAdsRepository.getClassifiedAds("all", "", 0)).thenReturn(classifieds.subList(0, 4));
	}
	
	
	//Test getClassifiedAds()
	/*@Test
	public void testGetClassifiedAds() {
		
		Assert.assertEquals(classifieds.subList(0, 4),classifiedAdsService.getClassifiedAds("all", "", "1"));
	}*/
	//Test getClassifiedAdById()

	//Test saveClassifiedAd()
	
	//Test deleteClassifiedAd()
	
	//Test getAllCategories()
	
	//Test getAllTags()
}
