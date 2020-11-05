package com.group1_cms.cms_antiques.controllers;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.group1_cms.cms_antiques.configurations.FileUploadUtil;
import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.ItemImage;
import com.group1_cms.cms_antiques.services.ClassifiedAdsService;
import com.group1_cms.cms_antiques.services.PostsService;
import com.group1_cms.cms_antiques.services.ClassifiedsService;

@Controller
public class PostsAndClassifiedsController {
	
	private PostsService postsService;

	private ClassifiedAdsService classifiedAdsService;

	@Autowired
	public PostsAndClassifiedsController(PostsService postsService, ClassifiedAdsService classifiedAdsService) {
		this.postsService = postsService;
		this.classifiedAdsService = classifiedAdsService;
	}
	
	@RequestMapping(value="/posts")
	public String posts(Model model){
		model.addAttribute("posts", postsService.getPosts());
		return "public/posts.html";
	}
	
	@RequestMapping(value="/posts/{category}")
	public String category(Model model, @PathVariable String category) {
		model.addAttribute("posts", postsService.getPostsFromCategory(category));
		return "public/posts.html";
	}
	
	@RequestMapping(value="/public/postForum")
	public String postForum() {
		return "public/postForum.html";
	}
	
	@RequestMapping(value = "/classified_ads", method = RequestMethod.GET)
	   public String redirect() {
	      return "redirect:classified_ads/all/1";
	   }
	
	@RequestMapping(value="/delete_classified", method = RequestMethod.POST)
	public String deleteClassified(Model model, @ModelAttribute("classifiedAd") ClassifiedAd classifiedAd) {
		classifiedAd = classifiedAdsService.getClassifiedAdById(classifiedAd.getId());
		
		classifiedAdsService.deleteClassifiedAd(classifiedAd);
		ItemImage image = classifiedAd.getItem().getItemImage();
		
		FileUploadUtil.deleteFile("item_images/"+image.getId()+"/", "item_images/"+image.getId()+"/"+image.getFileName());
		return "redirect:classified_ads/all/1";
	}
	
	@RequestMapping(value="/classified_ads", method = RequestMethod.POST)
	public String postClassifieds(Model model, @ModelAttribute("classifiedAd") ClassifiedAd classifiedAd, 
			@RequestParam("image") MultipartFile multipartFile) throws IOException{
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		if(!fileName.equals("")) {
			
			ItemImage image = classifiedAd.getItem().getItemImage();
			if(image.getId() == null) {
				image.setId(UUID.randomUUID());
			}
			image.setFileName(fileName);
			
			
			
			String uploadDir = "item_images/" + image.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}else {
			classifiedAd.getItem().getItemImage().setFileName("");
		}
		classifiedAdsService.saveClassifiedAd(classifiedAd);
		

		
		
		return "redirect:classified_ads/all/1";
		
	}
	
	@RequestMapping(value={"/classified_ads/{category}/{page}"}, method = RequestMethod.GET)
	public String classifieds(Model model,@PathVariable(required=false) String category, @PathVariable(required=false) String page, @RequestParam(required=false) String search) {
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
		
		
		//TODO:Clean up how page is handled
		model.addAttribute("classifiedAds", classifiedAdsService.getClassifiedAds(category, search, page));
		model.addAttribute("page", Integer.parseInt(page));
		model.addAttribute("totalPages", classifiedAdsService.getNumberOfClassifiedPages(category, search));
		model.addAttribute("search", search);
		model.addAttribute("category", category.substring(0, 1).toUpperCase() + category.substring(1));
		model.addAttribute("categories", classifiedAdsService.getAllCategories());
		
		return "classifieds/classified_ads.html";
	}
	
	
	
	@RequestMapping(value={"/classifiedsForm", "/classifiedsForm/{id}"}, method = RequestMethod.GET )
	public String classifiedsForum(Model model, @PathVariable(required=false) String id) {
		
		if(id == null) {
			ClassifiedAd classifiedAd = new ClassifiedAd();
			Item item = new Item();
			classifiedAd.setItem(item);
			model.addAttribute("classifiedAd", classifiedAd);
		}else {
			//TODO: get classified with the id and add it to the model
			
			ClassifiedAd classifiedAd = classifiedAdsService.getClassifiedAdById(UUID.fromString(id));
			model.addAttribute("classifiedAd", classifiedAd);
			
			
		}
		
		
		return "classifieds/postToClassifieds.html";
	}
	
}
