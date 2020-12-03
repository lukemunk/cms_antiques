package com.group1_cms.cms_antiques.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.ItemImage;
import com.group1_cms.cms_antiques.repositories.ClassifiedAdsRepository.CategoryNameRowMapper;
import com.group1_cms.cms_antiques.repositories.ClassifiedAdsRepository.ClassifiedAdTotalRowMapper;
import com.group1_cms.cms_antiques.repositories.ClassifiedAdsRepository.TagRowMapper;

import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
class ClassifiedAdsRepositoryTest {
	private NamedParameterJdbcTemplate jdbcTemplate;
	private ClassifiedAdsRepository classifiedAdRepository;
	private List<ClassifiedAd> classifieds;
	
	@BeforeEach
	public void mocks() {
		jdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
		classifiedAdRepository = new ClassifiedAdsRepository(jdbcTemplate);
		
		classifieds = new ArrayList<ClassifiedAd>();
		ClassifiedAd ad = new ClassifiedAd();
		ad.setId(UUID.randomUUID());
		ad.setItem(new Item());
		ad.getItem().setId(UUID.randomUUID());
		ad.getItem().setItemImage(new ItemImage());
		ad.getItem().getItemImage().setId(UUID.randomUUID());
		ad.setTitle("Test");
		ad.setDescription("Description");
		ad.setPrice("$4.00");
		classifieds.add(ad);
		for(int i=1; i<6; i++) {
			ClassifiedAd classified = new ClassifiedAd();
			classified.setId(UUID.randomUUID());
			classifieds.add(classified);
		}
		
		
		List<String> tags = new ArrayList<String>();
		tags.add("tag");
		Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.anyMap(), Mockito.any(TagsResultSetExtractor.class)))
			.thenReturn(tags);
	}
	
	//Tests getClassifiedAds()
	@Test
	public void testGetClassifiedAds() {
		
		Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.anyMap(), Mockito.any(ClassifiedAdRowMapper.class)))
			.thenReturn(classifieds.subList(0, 4));
		
		
		
		Assert.assertEquals(classifieds.subList(0, 4), classifiedAdRepository.getClassifiedAds("all", "", 0));
	}
	
	//Tests getClassifiedAdById()
	@Test
	public void testGetClassifiedAdById() {
		Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(ClassifiedAdRowMapper.class)))
			.thenReturn(classifieds.get(0));
		Assert.assertEquals(classifieds.get(0), classifiedAdRepository.getClassifiedAdById(UUID.randomUUID()));
	}
	
	//Tests getTotalClassifiedAds()
	@Test
	public void testGetTotalClassifiedAds() {
		Mockito.when(jdbcTemplate.queryForObject(Mockito.anyString(), Mockito.anyMap(), Mockito.any(ClassifiedAdTotalRowMapper.class)))
			.thenReturn(Integer.valueOf(6));
		Assert.assertEquals(6, classifiedAdRepository.getTotalClassifiedAds("all", ""));
		
	}
	
	//Tests saveClassifiedAd()
	@WithMockUser(value = "admin")
	@Test
	public void testSaveClassifiedAdNoImage() {
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getName()).thenReturn("admin");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		
		ClassifiedAd ad = classifieds.get(0);
		ad.getItem().getItemImage().setFileName(null);
		List<String> tags = new ArrayList<String>();
		tags.add("tag");
		tags.add("tag2");
		tags.add("tag3");
		ad.setTags(tags);
		classifiedAdRepository.saveClassifiedAd(ad);
		Mockito.verify(jdbcTemplate, Mockito.times(6)).update(Mockito.anyString(), Mockito.anyMap());
	}
	
	@WithMockUser(value = "admin")
	@Test
	public void testSaveClassifiedAdWithImage() {
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(authentication.getName()).thenReturn("admin");
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		ClassifiedAd ad = classifieds.get(0);
		
		ad.getItem().getItemImage().setFileName("test.png");
		classifiedAdRepository.saveClassifiedAd(ad);
		Mockito.verify(jdbcTemplate, Mockito.times(4)).update(Mockito.anyString(), Mockito.anyMap());
	}
	
	//Tests deleteClassifiedAd()
	@Test
	public void testDeleteClassifiedAd() {
		ClassifiedAd ad = classifieds.get(0);
		classifiedAdRepository.deleteClassifiedAd(ad);
	}
	
	//Tests getAllCategories()
	@Test
	public void testGetAllCategories() {
		List<String> categories = new ArrayList<String>();
		categories.add("Furniture");
		
		Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(CategoryNameRowMapper.class)))
			.thenReturn(categories);
		Assert.assertEquals(categories, classifiedAdRepository.getAllCategories());
	}
	
	//Tests getAllTags()
	@Test
	public void testGetAllTags() {
		List<String> tags = new ArrayList<String>();
		tags.add("tag");
		
		Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.any(TagRowMapper.class)))
			.thenReturn(tags);
		Assert.assertEquals(tags, classifiedAdRepository.getAllTags());
	}
	
	
	

}
