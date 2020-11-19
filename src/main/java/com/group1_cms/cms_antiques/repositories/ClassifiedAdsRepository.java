package com.group1_cms.cms_antiques.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.group1_cms.cms_antiques.models.ClassifiedAd;

@Repository
public class ClassifiedAdsRepository {
	private NamedParameterJdbcTemplate jdbcTemplate;
	private ClassifiedAdRowMapper classifiedAdRowMapper;
	private ClassifiedAdTotalRowMapper totalRowMapper;
	
	//Number of Results Per Page
	//---------------------------------------------------------------------------------------------------------------------------------------------
	public static final int RESULTSPERPAGE = 5;
	
	//Return Results
	//---------------------------------------------------------------------------------------------------------------------------------------------
	private static final String getClassifiedAdsSql = "SELECT BIN_TO_UUID(ca.id) AS 'Classified Id',ca.title, ca.price, ca.description, "
			+ "BIN_TO_UUID(i.id) AS 'Item Id', i.name AS 'Item', c.name AS 'Category', "
			+ "BIN_TO_UUID(u.id) AS 'User Id', u.username, "
			+ "BIN_TO_UUID(im.id) AS 'Image Id', im.file_path\r\n" + 
			"FROM Classified ca\r\n" + 
			"	INNER JOIN Item i ON ca.item_id = i.id \r\n" + 
			"	INNER JOIN Category c ON i.category_id = c.id \r\n" + 
			"	INNER JOIN User u ON ca.user_id = u.id\r\n" + 
			"	LEFT JOIN Item_Image im on im.item_id = i.id\r\n" + 
			"WHERE (ca.title LIKE :search\r\n" + 
			"	OR ca.description LIKE :search\r\n" + 
			"	OR i.name LIKE :search	\r\n" + 
			"	OR u.username LIKE :search)\r\n" + 
			"	AND\r\n" + 
			"	(c.name = :category\r\n" + 
			"	OR :category = 'all')\r\n" + 
			"ORDER BY ca.created_on DESC\r\n"+ 
			" limit "+ RESULTSPERPAGE +" offset :offset;";
	
	private static final String getClassifiedAdById = "SELECT BIN_TO_UUID(ca.id) AS 'Classified Id',ca.title, ca.price, ca.description, \r\n" + 
			"			BIN_TO_UUID(i.id) AS 'Item Id', i.name AS 'Item', c.name AS 'Category', \r\n" + 
			"			BIN_TO_UUID(u.id) AS 'User Id', u.username, \r\n" + 
			"			BIN_TO_UUID(im.id) AS 'Image Id', im.file_path \r\n" + 
			"			FROM Classified ca\r\n" + 
			"				INNER JOIN Item i ON ca.item_id = i.id  \r\n" + 
			"				INNER JOIN Category c ON i.category_id = c.id \r\n" + 
			"				INNER JOIN User u ON ca.user_id = u.id \r\n" + 
			"				LEFT JOIN Item_Image im on im.item_id = i.id \r\n" + 
			"			WHERE ca.id = UUID_TO_BIN(:classifiedId); ";
	
	private static final String getTotalNumberOfClassifiedAds = "SELECT count(title) AS 'total'\r\n" + 
			"FROM Classified ca\r\n" + 
			"	INNER JOIN Item i ON ca.item_id = i.id \r\n" + 
			"	INNER JOIN Category c ON i.category_id = c.id \r\n" + 
			"	INNER JOIN User u ON ca.user_id = u.id\r\n" + 
			"	LEFT JOIN Item_Image im on im.item_id = i.id\r\n" + 
			"WHERE (ca.title LIKE :search\r\n" + 
			"	OR ca.description LIKE :search\r\n" + 
			"	OR i.name LIKE :search	\r\n" + 
			"	OR u.username LIKE :search)\r\n" + 
			"	AND\r\n" + 
			"	(:category = 'all'\r\n" + 
			"	OR c.name = :category);";
	
	private static final String getAllCategoryNames = "SELECT name FROM Category;";
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//Inserts
	//---------------------------------------------------------------------------------------------------------------------------------------------
	private static final String saveClassifiedAdSql = "INSERT INTO Classified(id, title, description, price, user_id, item_id)\r\n" + 
			"VALUE(UUID_TO_BIN(:classifiedAdId), :title, :description, :price, "
			+ "(Select id from User WHERE username = :username), UUID_TO_BIN(:itemId))";
	private static final String saveItemSql = "INSERT INTO Item (id, name, category_id)"+
			"VALUE(UUID_TO_BIN( :itemId ), :itemName, (SELECT id FROM Category WHERE name = :categoryName))";
	private static final String saveItemImgSql = "INSERT INTO Item_Image (id, file_path, item_id)\r\n" + 
			"VALUE(UUID_TO_BIN(:imageId), :imagePath, UUID_TO_BIN(:itemId))";
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//Update
	//---------------------------------------------------------------------------------------------------------------------------------------------
	private static final String onDuplicate = " ON DUPLICATE KEY ";
	
	private static final String updateClassifiedAdSql = "UPDATE\r\n"
			+ "	title = :title,\r\n"
			+ "	description = :description,\r\n"
			+ "	price = :price;";
			
	private static final String updateItemSql = "UPDATE\r\n"
			+ "name = :itemName,\r\n"
			+ "category_id = (SELECT id FROM Category WHERE name = :categoryName);";
			
	private static final String updateItemImgSql = "UPDATE\r\n"
			+ "file_path = :imagePath;";
			
	//---------------------------------------------------------------------------------------------------------------------------------------------
	//Delete
	//---------------------------------------------------------------------------------------------------------------------------------------------
	private static final String deleteClassifiedAdWithIdSql = "DELETE FROM Classified WHERE id = UUID_TO_BIN(:classifiedAdId)";
	private static final String deleteItemWithIdSql = "DELETE FROM Item WHERE id = UUID_TO_BIN(:itemId)";
	private static final String deleteItemImageWithIdSql = "DELETE FROM Item_Image WHERE id = UUID_TO_BIN(:imageId)";
	

	
	public ClassifiedAdsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.classifiedAdRowMapper = new ClassifiedAdRowMapper();
		this.totalRowMapper = new ClassifiedAdTotalRowMapper();
	}
	
	public List<ClassifiedAd> getClassifiedAds(String category, String search, int offset){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("category", category);
		parameters.put("search", "%"+search+"%");
		parameters.put("offset", offset);
		return jdbcTemplate.query(getClassifiedAdsSql, parameters, classifiedAdRowMapper);
	}
	
	public ClassifiedAd getClassifiedAdById(UUID id) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("classifiedId", id.toString());
		return jdbcTemplate.queryForObject(getClassifiedAdById, parameters, classifiedAdRowMapper);
	}
	
	public int getTotalClassifiedAds(String category, String search) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("category", category);
		parameters.put("search", "%"+search+"%");
		return jdbcTemplate.queryForObject(getTotalNumberOfClassifiedAds, parameters, totalRowMapper).intValue();
	}
	
	public void saveClassifiedAd(ClassifiedAd classifiedAd) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if(principal instanceof UserDetails) {
			username = ((UserDetails)principal).getUsername();
		} else {
			username = principal.toString();
		}
		 Map<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("classifiedAdId", classifiedAd.getId().toString());
		 parameters.put("title", classifiedAd.getTitle());
		 parameters.put("description", classifiedAd.getDescription());
		 parameters.put("price", classifiedAd.getPrice()); 
		 parameters.put("username", username); 
		 parameters.put("itemId", classifiedAd.getItem().getId().toString());
		 parameters.put("itemName", classifiedAd.getItem().getName());
		 parameters.put("imagePath",
				 classifiedAd.getItem().getItemImage().getFileName());
		 parameters.put("imageId",
				 classifiedAd.getItem().getItemImage().getId().toString());
		 parameters.put("categoryName", classifiedAd.getItem().getCategory());
		 
		 
		 
		 jdbcTemplate.update(saveItemSql+onDuplicate+updateItemSql, parameters);
		 if(classifiedAd.getItem().getItemImage().getFileName() != null)
			 jdbcTemplate.update(saveItemImgSql+onDuplicate+updateItemImgSql, parameters);
		 jdbcTemplate.update(saveClassifiedAdSql+onDuplicate+updateClassifiedAdSql, parameters);
	}
	
	public void deleteClassifiedAd(ClassifiedAd classifiedAd) {
		
		 Map<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("classifiedAdId", classifiedAd.getId().toString());
		 parameters.put("itemId", classifiedAd.getItem().getId().toString());
		 parameters.put("imageId",
				 classifiedAd.getItem().getItemImage().getId().toString());
		 jdbcTemplate.update(deleteItemImageWithIdSql, parameters);
		 jdbcTemplate.update(deleteClassifiedAdWithIdSql, parameters);
		 jdbcTemplate.update(deleteItemWithIdSql, parameters);
	}
	
	public List<String> getAllCategories(){
		return jdbcTemplate.query(getAllCategoryNames, new CategoryNameRowMapper());
	}
	
	private class ClassifiedAdTotalRowMapper implements RowMapper<Integer>{

		@Override
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			@SuppressWarnings("deprecation")
			Integer integer = new Integer(rs.getInt("total"));
			
			return integer;
		}
		
	}
	
	private class CategoryNameRowMapper implements RowMapper<String>{

		@Override
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String category = rs.getString("name");
			return category;
		}
		
	}
	
	
}
