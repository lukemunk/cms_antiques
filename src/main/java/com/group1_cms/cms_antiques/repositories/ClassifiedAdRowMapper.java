package com.group1_cms.cms_antiques.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.group1_cms.cms_antiques.models.ClassifiedAd;
import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.ItemImage;
import com.group1_cms.cms_antiques.models.User;

public class ClassifiedAdRowMapper implements RowMapper<ClassifiedAd>{

	@Override
	public ClassifiedAd mapRow(ResultSet rs, int rowNum) throws SQLException {
		ClassifiedAd classifiedAd = new ClassifiedAd();
		classifiedAd.setId(UUID.fromString(rs.getString("Classified Id")));
		
		classifiedAd.setTitle(rs.getString("title"));
		classifiedAd.setPrice(rs.getString("price"));
		classifiedAd.setDescription(rs.getString("description"));
		
		Item item = new Item();
		item.setId(UUID.fromString(rs.getString("Item Id")));
		item.setName(rs.getString("item"));
		item.setCategory(rs.getString("category"));
		
		ItemImage image = new ItemImage();
		if(rs.getString("Image Id") != null)
			image.setId(UUID.fromString(rs.getString("Image Id")));
		image.setFileName(rs.getString("file_path"));
		item.setItemImage(image);
		classifiedAd.setItem(item);
		
		User user = new User();
		user.setId(UUID.fromString(rs.getString("User Id")));
		user.setUsername(rs.getString("username"));
		classifiedAd.setCreator(user);
		return classifiedAd;
	}

}
