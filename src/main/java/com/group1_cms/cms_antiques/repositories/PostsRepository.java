package com.group1_cms.cms_antiques.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.group1_cms.cms_antiques.models.Item;
import com.group1_cms.cms_antiques.models.Post;
import com.group1_cms.cms_antiques.models.User;

@Repository
public class PostsRepository
{
	private PostRowMapper postRowMapper;
	private PostsTotalRowMapper totalPostRowMapper;
	private NamedParameterJdbcTemplate jdbcTemplate;

	// Pagination limit
	private static final String MAXRESULTS = "10";

	//region SQL GETPOSTS
	private static final String GETPOSTS = "SELECT BIN_TO_UUID(ca.id) AS 'Post Id',ca.title, ca.story, "
			+ "BIN_TO_UUID(i.id) AS 'Item Id', i.name AS 'Item', c.name AS 'Category', "
			+ "BIN_TO_UUID(u.id) AS 'User Id', u.username\r\n " +
			"FROM Post ca\r\n" +
			"	INNER JOIN Item i ON ca.item_id = i.id \r\n" +
			"	INNER JOIN Category c ON i.category_id = c.id \r\n" +
			"	INNER JOIN User u ON ca.user_id = u.id\r\n" +
			"WHERE (ca.title LIKE :search\r\n" +
			"	OR ca.story LIKE :search\r\n" +
			"	OR i.name LIKE :search	\r\n" +
			"	OR u.username LIKE :search)\r\n" +
			"	AND\r\n" +
			"	(c.name = :category\r\n" +
			"	OR :category = 'all')\r\n" +
			"ORDER BY ca.created_on DESC\r\n"+
			" limit "+ MAXRESULTS +" offset :offset;";
	//endregion

	//region SQL GETPOSTBYID
	private static final String GETPOSTSBYID = "SELECT BIN_TO_UUID(ca.id) AS 'Post Id',ca.title, ca.story, \r\n" +
			"			BIN_TO_UUID(i.id) AS 'Item Id', i.name AS 'Item', c.name AS 'Category', \r\n" +
			"			BIN_TO_UUID(u.id) AS 'User Id', u.username \r\n" +
			"			FROM Post ca\r\n" +
			"				INNER JOIN Item i ON ca.item_id = i.id  \r\n" +
			"				INNER JOIN Category c ON i.category_id = c.id \r\n" +
			"				INNER JOIN User u ON ca.user_id = u.id \r\n" +
			"			WHERE ca.id = UUID_TO_BIN(:postID); ";
	
	//endregion

	//region SQL GETTOTALPOSTCOUNT
	private static final String GETTOTALPOSTCOUNT = "SELECT count(title) AS 'total'\r\n" +
			"FROM Post ca\r\n" +
			"	INNER JOIN Item i ON ca.item_id = i.id \r\n" +
			"	INNER JOIN Category c ON i.category_id = c.id \r\n" +
			"	INNER JOIN User u ON ca.user_id = u.id\r\n" +
			"WHERE (ca.title LIKE :search\r\n" +
			"	OR ca.story LIKE :search\r\n" +
			"	OR i.name LIKE :search	\r\n" +
			"	OR u.username LIKE :search)\r\n" +
			"	AND\r\n" +
			"	(:category = 'all'\r\n" +
			"	OR c.name = :category);";
	//endregion

	//region SQL GETALLCATEGORYNAMES
	private static final String GETALLCATEGORYNAMES = "SELECT name FROM Category;";
	//endregion

	//region SQL Insert Statements
	private static final String SAVEPOST = "INSERT INTO Post(id, title, story, user_id, item_id)\r\n" +
			"VALUE(UUID_TO_BIN(:postId), :title, :story, :userId, UUID_TO_BIN(:itemId))";
	private static final String SAVEITEM = "INSERT INTO Item (id, name, category_id)"+
			"VALUE(UUID_TO_BIN(:itemId ), :itemName, (SELECT id FROM Category WHERE name = :categoryName))";
	//endregion

	//region SQL Update Statements
	private static final String DUPLICATE = " ON DUPLICATE KEY ";

	private static final String UPDATEPOST = "UPDATE\r\n"
			+ "	title = :title,\r\n"
			+ "	story = :story;";

	private static final String UPDATEITEM = "UPDATE\r\n"
			+ "name = :itemName,\r\n"
			+ "category_id = (SELECT id FROM Category WHERE name = :categoryName);";
	//endregion

	//region SQL Deletion statements
	private static final String DELETEPOST = "DELETE FROM Post WHERE id = UUID_TO_BIN(:postId)";
	private static final String DELETEITEM = "DELETE FROM Item WHERE id = UUID_TO_BIN(:itemId)";
	//endregion

	// Set up
	public PostsRepository(NamedParameterJdbcTemplate jdbcTemplate)
	{
		this.jdbcTemplate = jdbcTemplate;
		this.postRowMapper = new PostRowMapper();
		this.totalPostRowMapper = new PostsTotalRowMapper();
	}

	//region GET posts and get by ID as well as count and categories
	public List<Post> getPosts(String categoryIN, String search, int page, int numberOfPosts)
	{
        int offset = (page - 1) * 10;
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("category", categoryIN);
		parameters.put("search", "%"+search+"%");
		parameters.put("offset", offset);
		return jdbcTemplate.query(GETPOSTS, parameters, postRowMapper);
	}

	public Post getPostByID(String id)
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("postID", id.toString());
		return jdbcTemplate.queryForObject(GETPOSTSBYID, parameters, postRowMapper);
	}

	public int getAllPostsCount(String category, String search)
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("category", category);
		parameters.put("search", "%"+search+"%");
		return jdbcTemplate.queryForObject(GETTOTALPOSTCOUNT, parameters, totalPostRowMapper).intValue();
	}

	public List<String> getAllCategories()
	{
		return jdbcTemplate.query(GETALLCATEGORYNAMES, new CategoryNameRowMapper());
	}
	//endregion

	//region Save/Update/Delete Posts
	public void updatePost(Post postIN)
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("postId", postIN.getId().toString());
		parameters.put("title", postIN.getTitle());
		parameters.put("story", postIN.getStory());
		parameters.put("userId",2);
		parameters.put("itemId", postIN.getItem().getId().toString());
		parameters.put("itemName", postIN.getItem().getName());
		parameters.put("categoryName", postIN.getItem().getCategory());

		jdbcTemplate.update(SAVEITEM+DUPLICATE+UPDATEITEM, parameters);
		jdbcTemplate.update(SAVEPOST+DUPLICATE+UPDATEPOST, parameters);
	}

	public void deletePost(Post post)
	{
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("classifiedAdId", post.getId().toString());
		parameters.put("itemId", post.getItem().getId().toString());
		jdbcTemplate.update(DELETEITEM, parameters);
		jdbcTemplate.update(DELETEPOST, parameters);
	}
	//endregion


	// Row mappers
	private class PostsTotalRowMapper implements RowMapper<Integer>{

		@Override
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException
		{
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
