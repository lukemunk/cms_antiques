package com.group1_cms.cms_antiques.services;



import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.group1_cms.cms_antiques.configurations.FileUploadUtil;
import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.ItemImage;
import com.group1_cms.cms_antiques.repositories.ClassifiedAdsRepository;
import com.group1_cms.cms_antiques.configurations.FileUploadUtil;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

@RunWith(MockitoJUnitRunner.class)
class ClassifiedAdsServiceTest {
		
	
	private ClassifiedAdsService classifiedAdsService;
	
	private ClassifiedAdsRepository classifiedAdsRepository;
	
	private List<ClassifiedAd> classifieds;
	
	@BeforeEach
	public void setup() {
		classifieds = new ArrayList<ClassifiedAd>();
		classifiedAdsRepository = Mockito.mock(ClassifiedAdsRepository.class);
		classifiedAdsService = new ClassifiedAdsService(classifiedAdsRepository);
        
		for(int i=1; i<6; i++) {
			ClassifiedAd classified = new ClassifiedAd();
//			classified.setTitle("TestClassified "+i);
//			classified.setItem(new Item());
			classifieds.add(classified);
		}
		
		
		
	}
	
	
	//Test getClassifiedAds()
	@Test
	public void testGetClassifiedAds() {
		Mockito.when(classifiedAdsRepository.getClassifiedAds("all", "", 0)).thenReturn(classifieds.subList(0, 4));
		Assert.assertEquals(classifieds.subList(0, 4),classifiedAdsService.getClassifiedAds("all", "", "1"));
	}
	
	@Test
	public void testGetClassifiedAdsPageLessThan0() {
		Mockito.when(classifiedAdsRepository.getClassifiedAds("all", "", 0)).thenReturn(classifieds.subList(0, 4));
		Assert.assertEquals(classifieds.subList(0, 4),classifiedAdsService.getClassifiedAds("all", "", "0"));
	}
	//Test getClassifiedAdById()
	@Test
	public void testGetClassifiedAdById() {
		Mockito.when(classifiedAdsRepository.getClassifiedAdById(Mockito.any(UUID.class))).thenReturn(classifieds.get(0));
		Assert.assertEquals(classifieds.get(0), classifiedAdsService.getClassifiedAdById(UUID.randomUUID()));
	}
	
	//Test getNumberOfClassifiedPages()
	@Test
	public void testGetNumberOfClassifiedPages() {
		Mockito.when(classifiedAdsRepository.getTotalClassifiedAds("all", "")).thenReturn(10);
		Assert.assertEquals(10/classifiedAdsRepository.RESULTSPERPAGE, classifiedAdsService.getNumberOfClassifiedPages("all", ""));
	}
	
	@Test
	public void testGetNumberOfClassifiedPagesNotEvenDivisible() {
		Mockito.when(classifiedAdsRepository.getTotalClassifiedAds("all", "")).thenReturn(12);
		Assert.assertEquals(12/classifiedAdsRepository.RESULTSPERPAGE+1, classifiedAdsService.getNumberOfClassifiedPages("all", ""));
	}
	
	@Test
	public void testGetNumberOfClassifiedPagesNoResults() {
		Mockito.when(classifiedAdsRepository.getTotalClassifiedAds(Mockito.anyString(), Mockito.anyString())).thenReturn(0);
		Assert.assertEquals(1, classifiedAdsService.getNumberOfClassifiedPages("all", ""));
	}

	//Test saveClassifiedAd()
	private ClassifiedAd setUpAd() {
		ClassifiedAd ad = new ClassifiedAd();
		ad.setItem(new Item());
		ad.getItem().setItemImage(new ItemImage());
		return ad;
	}
	
	@Test
	public void testSaveClassifiedAd() {
		ClassifiedAd ad = setUpAd();
		ad.setId(UUID.randomUUID());
		ad.getItem().setId(UUID.randomUUID());
		ad.getItem().getItemImage().setId(UUID.randomUUID());
		classifiedAdsService.saveClassifiedAd(ad);
		Mockito.verify(classifiedAdsRepository).saveClassifiedAd(ad);
	}
	
	@Test
	public void testSaveClassifiedAdNewAd() {
		ClassifiedAd ad = setUpAd();
		classifiedAdsService.saveClassifiedAd(ad);
		Mockito.verify(classifiedAdsRepository).saveClassifiedAd(ad);
	}
	
	//Test deleteClassifiedAd()
	@Test
	public void testDeleteClassifiedAdNoImage() {
		ClassifiedAd ad = setUpAd();
		ad.getItem().getItemImage().setFileName("");
		classifiedAdsService.deleteClassifiedAd(ad);
		Mockito.verify(classifiedAdsRepository).deleteClassifiedAd(ad);
	}
	
	@Test
	public void testDeleteClassifiedAdWithImage() {
		ClassifiedAd ad = setUpAd();
		ad.getItem().getItemImage().setFileName("fakeImage");
		classifiedAdsService.deleteClassifiedAd(ad);
		Mockito.verify(classifiedAdsRepository).deleteClassifiedAd(ad);

	}
	
	//Test getAllCategories()
	@Test
	public void testGetAllCategories() {
		List<String> categories = new ArrayList<String>();
		categories.add("all");
		
		Mockito.when(classifiedAdsRepository.getAllCategories()).thenReturn(categories);
		Assert.assertEquals(categories, classifiedAdsService.getAllCategories());
	}
	//Test getAllTags()
	@Test
	public void testGetAllTags() {
		List<String> tags = new ArrayList<String>();
		tags.add("tag");
		Mockito.when(classifiedAdsRepository.getAllTags()).thenReturn(tags);
		Assert.assertEquals(tags, classifiedAdsService.getAllTags());
	}
}
