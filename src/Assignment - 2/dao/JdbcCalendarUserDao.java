package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

@Repository
public class JdbcCalendarUserDao implements CalendarUserDao {

	private JdbcTemplate jdbcTemplate;
	// --- constructors ---
	public JdbcCalendarUserDao() {

	}

	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate =  new JdbcTemplate(dataSource);
	}

	// --- CalendarUserDao methods ---
	@Override
	public CalendarUser getUser(int id){
		return this.jdbcTemplate.queryForObject("select * from calendar_users where id = ?", 
				new BeanPropertyRowMapper<CalendarUser>(CalendarUser.class), id);	
	}

	@Override
	public CalendarUser findUserByEmail(String email) {
		return this.jdbcTemplate.queryForObject("select * from calendar_users where email = ?", 
				new BeanPropertyRowMapper<CalendarUser>(CalendarUser.class), email);
	}

	@Override
	public List<CalendarUser> findUsersByEmail(String email) {
		String sql_query;
		sql_query = "select * from calendar_users where email like '%"+email+"%'";
		return jdbcTemplate.query(sql_query, new ResultSetExtractor<List<CalendarUser>>() {
			@Override  
			public List<CalendarUser> extractData(ResultSet rs) throws SQLException, DataAccessException  {
				List<CalendarUser> list = new ArrayList<CalendarUser>();
		       	while(rs.next()) {
		       		CalendarUser user = new CalendarUser();
					user.setId(Integer.parseInt(rs.getString("id")) );
					user.setEmail(rs.getString("email"));
					user.setPassword(rs.getString("password"));
					user.setName(rs.getString("name"));

					list.add(user);
		       	}		       	
		        return list;
			}
		});
	}

	@Override
	public int createUser(final CalendarUser userToAdd){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("insert into calendar_users(email, password, name) values(?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, userToAdd.getEmail());
				ps.setString(2, userToAdd.getPassword());
				ps.setString(3, userToAdd.getName());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	@Override
	public void deleteAll() {
		// Assignment 2
		this.jdbcTemplate.update("delete from calendar_users");
	}
}