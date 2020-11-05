package com.group1_cms.cms_antiques.repositories;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Classifieds;
import com.group1_cms.cms_antiques.models.User;

@Repository
public class ClassifiedsRepository {
	private ArrayList<Classifieds> classifieds;

	private static final String GET_POST_BY_ID = "SELECT * FROM Classified WHERE classifieds.ID = c.ID";

	public ClassifiedsRepository() {
		User user1 = new User();
		User user2 = new User();
		User user3= new User();
		user1.setUsername("sdfhg");
		user2.setUsername("wetry");
		user3.setUsername("wyui");
		
		classifieds = new ArrayList<Classifieds>();
		classifieds.add(new Classifieds("Ad Title", 
				"chocolate", "9.99",
				new Item("mold", "Collectible"),
				user1));
		classifieds.add(new Classifieds("Ad Title", "Milk\r\n" + 
				"\r\n" + 
				"chocolate.\r\n" +  
				"© 2015 — 2020", "19.99",
				new Item("Marble Table", "Furniture"),
				user2));
		classifieds.add(new Classifieds("Ad Title", "Tempering Table", "299.99", new Item("Tempering Table", "Furniture"),user1));
		classifieds.add(new Classifieds("Ad Title", "White Chocolate\r\n" + 
				"\r\n" + 
				"Not really chocolate\r\n" + 
				"\r\n" + 
				"But tastes good anyways\r\n" + 
				"\r\n" + 
				"Blah blah blah\r\n" + 
				"\r\n" + 
				"more blah blah blah\r\n" + 
				"\r\n" + 
				"....\r\n", "9.99",
				new Item("Molecular Gastronomy", "Art"),
				user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("abc", "Jewelry"), user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("def", "Jewelry"), user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("ghi", "Collectible"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("jkl", "Clothing"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("mno", "Clothing"),user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("pqr", "Clothing"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("stu", "Clothing"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("vwx", "Furniture"),user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("yz", "Furniture"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a1", "Art"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a2", "Clothing"),user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a3", "Jewelry"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a4", "Art"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a5", "Furniture"),user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a6", "Furniture"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a7", "Collectible"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a8", "Art"),user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a9", "Art"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a10", "Art"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a11", "Art"),user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a12", "Art"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a13", "Art"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a14", "Art"),user2));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a15", "Art"),user3));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a16", "Art"),user1));
		classifieds.add(new Classifieds("Ad Title", "", "9.99", new Item("a17", "Art"),user2));	
	}
	
	public ArrayList<Classifieds> getClassifieds(int numberOfClassifieds){
		ArrayList<Classifieds> classifieds = new ArrayList<Classifieds>();
		this.classifieds.stream()
		.limit(numberOfClassifieds)
		.forEachOrdered(classifieds::add);
		return classifieds;
	}
	
public ArrayList<Classifieds> getClassifiedsFromCategory(int numberOfClassifieds, String category){
		
		ArrayList<Classifieds> classifieds = new ArrayList<Classifieds>();
		this.classifieds.stream()
		.filter(classified -> classified.getItem().getCategory().equalsIgnoreCase(category))
		.limit(numberOfClassifieds)
		.forEachOrdered(classifieds::add);
		
		return classifieds;
	}

    public Classifieds getPostByID(Long id)
    {
    	Classifieds newAD;
		this.classifieds.stream().filter(classified -> classified.getId().equalsIgnoreCase(id.toString()));

    	return null;
    }

}
