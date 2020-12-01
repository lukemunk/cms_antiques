package com.group1_cms.cms_antiques.controllers;

import java.io.IOException;
import java.util.UUID;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.group1_cms.cms_antiques.configurations.FileUploadUtil;
import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.ItemImage;
import com.group1_cms.cms_antiques.services.ClassifiedAdsService;

@Controller
public class ClassifiedAdsContentController {


	private ClassifiedAdsService classifiedAdsService;

	@Autowired
	public ClassifiedAdsContentController(ClassifiedAdsService classifiedAdsService) {
		this.classifiedAdsService = classifiedAdsService;
	}

	
	//Redirects you to the first page of all classifieds
	@RequestMapping(value = "/classified_ads", method = RequestMethod.GET)
	   public ModelAndView redirect() {
	      return new ModelAndView("redirect:classified_ads/all/1");
	   }
	
	@RequestMapping(value="/delete_classified", method = RequestMethod.POST)
	public ModelAndView deleteClassified(@ModelAttribute("classifiedAd") ClassifiedAd classifiedAd, Authentication authentication) {
		
		classifiedAd = classifiedAdsService.getClassifiedAdById(classifiedAd.getId());
		
		//If you are the creator of the content or you are an admin you can delete the classified ad
		if(authentication.getName().equals(classifiedAd.getCreator().getUsername())
				|| authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("Admin_Permissions")))
			classifiedAdsService.deleteClassifiedAd(classifiedAd);
		
		
		
		return new ModelAndView("redirect:classified_ads/all/1");
	}
	
	
	
	@RequestMapping(value="/classified_ads", method = RequestMethod.POST)
	public ModelAndView postClassifieds(@ModelAttribute("classifiedAd") ClassifiedAd classifiedAd, 
			@RequestParam("image") MultipartFile multipartFile) throws IOException{
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		ItemImage image = classifiedAd.getItem().getItemImage();
		if(image.getId() == null) {
			image.setId(UUID.randomUUID());
			image.setFileName("");
		}
		
		if(!fileName.equals("")) {
			if(!image.getFileName().contentEquals(""))
				FileUploadUtil.deleteFile(null, "item_images/"+image.getId()+"/"+image.getFileName());
			
			image.setFileName(fileName);
			
			
			
			String uploadDir = "item_images/" + image.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
		
		classifiedAdsService.saveClassifiedAd(classifiedAd);
		
		ModelAndView newView = new ModelAndView("redirect:classified_ads/view/"+classifiedAd.getId());

		
		
		return newView;
		
	}
	
	@RequestMapping(value={"/classified_ads/{category}/{page}"}, method = RequestMethod.GET)
	public ModelAndView classifieds(@PathVariable(required=false) String category, @PathVariable(required=false) String page, @RequestParam(required=false) String search) {
		ModelAndView newView = new ModelAndView("classifieds/classified_ads");
		if(search == null) 
			search = "";
		
		if(category == null) 
			category = "all";
		try {
			if(page == null || Integer.parseInt(page)<=0) 
				page = "1";
		} catch (NumberFormatException nfe) {
	        page = "1";
	    }
		
		
		newView.addObject("classifiedAds", classifiedAdsService.getClassifiedAds(category, search, page));
		newView.addObject("page", Integer.parseInt(page));
		newView.addObject("totalPages", classifiedAdsService.getNumberOfClassifiedPages(category, search));
		newView.addObject("search", search);
		newView.addObject("category", category.substring(0, 1).toUpperCase() + category.substring(1));
		newView.addObject("categories", classifiedAdsService.getAllCategories());
		
		return newView;
	}
	
	@RequestMapping(value="/add_tag", method = RequestMethod.POST)
	public ModelAndView addTag(@ModelAttribute("classifiedAd") ClassifiedAd classifiedAd, @ModelAttribute("tag") String tag) {
		ModelAndView newView = new ModelAndView("classifieds/edit_classified::#tags");
		classifiedAd.getTags().add(tag);
		newView.addObject(classifiedAd);
		
		return newView;
	}
	
	@RequestMapping(value="/remove_tag", method = RequestMethod.POST)
	public ModelAndView removeTag(@ModelAttribute("classifiedAd") ClassifiedAd classifiedAd, @RequestParam("removeTag") int index) {
		ModelAndView newView = new ModelAndView("classifieds/edit_classified::#tags");
		classifiedAd.getTags().remove(index);
		newView.addObject(classifiedAd);
		return newView;
	}
	
	
	@RequestMapping(value={"classified_ads/new", "classified_ads/edit/{id}"}, method = RequestMethod.GET )
	public ModelAndView classifiedsForum(@PathVariable(required=false) String id, Authentication authentication) {
		ModelAndView newView = new ModelAndView("classifieds/edit_classified");
		if(id == null) {
			ClassifiedAd classifiedAd = new ClassifiedAd();
			Item item = new Item();
			classifiedAd.setItem(item);
			newView.addObject("classifiedAd", classifiedAd);
			newView.addObject("newClassified", true);
		}else {
			
			
			//If the user doesn't have permission to edit the page redirect them
			ClassifiedAd classifiedAd = classifiedAdsService.getClassifiedAdById(UUID.fromString(id));
			if(authentication.getName().equals(classifiedAd.getCreator().getUsername())
					|| authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("Admin_Permissions")))
				newView.addObject("classifiedAd", classifiedAd);
			else
				return new ModelAndView("redirect:/classified_ads/view/{id}");
			
			
			newView.addObject("newClassified", false);
		}
		newView.addObject("categories", classifiedAdsService.getAllCategories());
		newView.addObject("allTags", classifiedAdsService.getAllTags());
		
		return newView;
	}
	
	@RequestMapping(value={"classified_ads/view/{id}"}, method = RequestMethod.GET )
	public ModelAndView viewClassified(@PathVariable(required=true) String id, Authentication authentication) {
		ModelAndView newView = new ModelAndView("classifieds/view_classified");
     
		ClassifiedAd classifiedAd = classifiedAdsService.getClassifiedAdById(UUID.fromString(id));
		
		if(classifiedAd == null)
			return new ModelAndView("redirect:/classified_ads/all/1");
		
		newView.addObject("ad", classifiedAd);
		newView.addObject("username", authentication.getName());
		newView.addObject("isAdmin", authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("Admin_Permissions")));
		newView.addObject("categories",  classifiedAdsService.getAllCategories());
		return newView;
	}
	
}
