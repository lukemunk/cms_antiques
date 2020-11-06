package com.group1_cms.cms_antiques.repositories;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.group1_cms.cms_antiques.models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostRowMapper implements RowMapper<Post>
{
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException
    {
        Post currentPost = new Post();
        currentPost.setId( UUID.fromString(rs.getString("Post Id")) );

        currentPost.setTitle(rs.getString("title"));
        currentPost.setStory(rs.getString("story"));

        Item item = new Item();
        item.setId(UUID.fromString(rs.getString("Item Id")));
        item.setName(rs.getString("item"));
        item.setCategory(rs.getString("category"));

        ItemImage image = new ItemImage();
        if(rs.getString("Image Id") != null)
            image.setId(UUID.fromString(rs.getString("Image Id")));
        image.setFileName(rs.getString("file_path"));
        item.setItemImage(image);
        currentPost.setItem(item);

        User user = new User();
        user.setId(UUID.fromString(rs.getString("User Id")));
        user.setUsername(rs.getString("username"));
        currentPost.setCreator(user);
        return currentPost;
    }

}
