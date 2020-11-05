package com.group1_cms.cms_antiques.controllers;

import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Classifieds;
import com.group1_cms.cms_antiques.services.ClassifiedsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/classifieds/")
public class ClassifiedsContentController {
	
	 private ClassifiedsService classifiedsService;

	    @Autowired
	    public ClassifiedsContentController(ClassifiedsService classifiedsService)
	    {
	        this.classifiedsService = classifiedsService;
	    }

	    // Returns a classifieds Page
	    @RequestMapping("/view/{id}")
	    public String view(@PathVariable("id") Long id, Model model) {
	        Classifieds classifieds = classifiedsService.findById(id);
	        if (classifieds == null) {
	            // Handle no classified ad found
	            return "redirect:/";
	        }
	        model.addAttribute("classifieds", classifieds);
	        return "classifieds/view";
	    }

	    //region classifieds List
	    @GetMapping(path = "/{Category}",
	            produces = "application/json")
	    public String getClassifiedsCategory(Model model, @PathVariable String Category)
	    {
	        model.addAttribute("classifieds", classifiedsService.getClassifiedsFromCategory(Category));
	        return "public/classifieds.html";
	    }

	    @GetMapping(path = "/",
	            produces = "application/json")
	    public String getAllClassifieds(Model model)
	    {
	        // Gets classifieds in limited order
	        List<Classifieds> tenClassifieds = classifiedsService.getClassifieds().stream().limit(10).collect(Collectors.toList());
	        model.addAttribute("tenClassifieds", tenClassifieds);
	        return "index";
	    }
	    //endregion


}
