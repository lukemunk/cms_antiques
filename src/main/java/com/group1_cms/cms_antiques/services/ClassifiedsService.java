package com.group1_cms.cms_antiques.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group1_cms.cms_antiques.models.Classifieds;
import com.group1_cms.cms_antiques.repositories.ClassifiedsRepository;

@Service
public class ClassifiedsService {

private ClassifiedsRepository classifiedsRepository;
	
	@Autowired
	public ClassifiedsService(ClassifiedsRepository classifiedsRepository) {
		this.classifiedsRepository = classifiedsRepository;
		
	}
	
	public ArrayList<Classifieds> getClassifieds(){
		return classifiedsRepository.getClassifieds(10);
	}
	
	public ArrayList<Classifieds> getClassifiedsFromCategory(String category){
		return classifiedsRepository.getClassifiedsFromCategory(10, category);
	}

	public Classifieds findById(Long id)
	{
		Classifieds newAD = classifiedsRepository.getPostByID(id);
		return newAD;
	}
}
