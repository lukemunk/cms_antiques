package com.group1_cms.cms_antiques.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class TagsResultSetExtractor implements ResultSetExtractor<List<String>>{

	@Override
	public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		List<String> tags = new ArrayList<String>();
		while(rs.next()) {
			
			tags.add(rs.getString("tag"));
		}
		return tags;
	}

}
