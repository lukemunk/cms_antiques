package com.group1_cms.cms_antiques.services;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.repositories.ClassifiedAdsRepository;

@Service
public class ClassifiedAdsService {
	private ClassifiedAdsRepository classifiedAdsRepository;
	
	@Autowired
	public ClassifiedAdsService(ClassifiedAdsRepository classifiedAdsRepository) {
		this.classifiedAdsRepository= classifiedAdsRepository;
		
	}
	
	public List<ClassifiedAd> getClassifiedAds(String category, String search, String page){
		int offset;
	
		if(Integer.parseInt(page) >=0) {
			offset = Integer.parseInt(page);
			offset = (offset-1)*5;
		}else {
			offset = 0;
		}
		
		
		return classifiedAdsRepository.getClassifiedAds(category, search, offset);
	}
	
	public ClassifiedAd getClassifiedAdById(UUID id) {
		return classifiedAdsRepository.getClassifiedAdById(id);
	}
	
	public int getNumberOfClassifiedPages(String category, String search) {
		int results = classifiedAdsRepository.getTotalClassifiedAds(category, search);
		int pages = results/ClassifiedAdsRepository.RESULTSPERPAGE;
		if(results%5 != 0 || results <= 0)
			pages++;
		return pages;
	}
	
	public void saveClassifiedAd(ClassifiedAd classifiedAd) {
		
		if(classifiedAd.getId() == null)
			classifiedAd.setId(UUID.randomUUID());
		if(classifiedAd.getItem().getId() == null)
			classifiedAd.getItem().setId(UUID.randomUUID());
		if(classifiedAd.getItem().getItemImage().getId() == null)
			classifiedAd.getItem().getItemImage().setId(UUID.randomUUID());
		
		classifiedAdsRepository.saveClassifiedAd(classifiedAd);
	}
	
	public void deleteClassifiedAd(ClassifiedAd classifiedAd) {
		classifiedAdsRepository.deleteClassifiedAd(classifiedAd);
	}
	
	public List<String> getAllCategories(){
		return classifiedAdsRepository.getAllCategories();
	}
}
