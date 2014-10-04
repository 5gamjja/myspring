package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.DataAccessException;
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
public class JdbcEventDao implements EventDao {
	//private DataSource dataSource;
	@Autowired
	private CalendarUserDao calendarUserDao;
	
	private JdbcTemplate jdbcTemplate;
	
   	
	// --- constructors ---
	public JdbcEventDao() { 
	}

	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate =  new JdbcTemplate(dataSource);
	}

	// --- EventService ---
	@Override
	public Event getEvent(int eventId) {
		return jdbcTemplate.query("select * from events where id = ?", new ResultSetExtractor<Event>() {
			@Override  
			public Event extractData(ResultSet rs) throws SQLException, DataAccessException  {
				Event event = new Event();
				rs.next();
		        		
		       	event.setId(Integer.parseInt(rs.getString("id")));
		   		Calendar when = Calendar.getInstance();
		    	when.setTimeInMillis(rs.getTimestamp("when").getTime());
		    	event.setWhen(when);
		    	event.setSummary(rs.getString("summary"));
		    	event.setDescription(rs.getString("description"));
		    		
		    	event.setOwner(calendarUserDao.getUser(rs.getInt("owner")));
		    	event.setAttendee(calendarUserDao.getUser(rs.getInt("attendee")));
		    			    	
		        return event; 
		        }
		 }, eventId);
	}

	@Override
	public int createEvent(final Event event) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("insert into events(`when`, summary, description, owner, attendee) values(?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				Timestamp timestamp = new Timestamp(event.getWhen().getTimeInMillis()); 

				ps.setTimestamp(1, timestamp);
				ps.setString(2, event.getSummary());
				ps.setString(3, event.getDescription());
				ps.setInt(4, event.getOwner().getId());
				ps.setInt(5, event.getAttendee().getId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<Event> findForOwner(int ownerUserId) {
		// Assignment 2
		return jdbcTemplate.query("select * from events where owner = ?", new ResultSetExtractor<List<Event>>() {
			@Override  
			public List<Event> extractData(ResultSet rs) throws SQLException, DataAccessException  {
				List<Event> list = new ArrayList<Event>();

				while(rs.next()) {
					Event event = new Event();
					event.setId(Integer.parseInt(rs.getString("id")));
			   		Calendar when = Calendar.getInstance();
			    	when.setTimeInMillis(rs.getTimestamp("when").getTime());
			    	event.setWhen(when);
			    	event.setSummary(rs.getString("summary"));
			    	event.setDescription(rs.getString("description"));
			    		
			    	event.setOwner(calendarUserDao.getUser(rs.getInt("owner")));
			    	event.setAttendee(calendarUserDao.getUser(rs.getInt("attendee")));
			    	
			    	list.add(event);
		       	}		       	
		        return list; 
		        }
		 }, ownerUserId);
	}
	
	@Override
	public List<Event> getEvents(){
		return jdbcTemplate.query("select * from events", new ResultSetExtractor<List<Event>>() {
			@Override  
			public List<Event> extractData(ResultSet rs) throws SQLException, DataAccessException  {
				List<Event> list = new ArrayList<Event>();
				
				while(rs.next()) {
					Event event = new Event();
					event.setId(Integer.parseInt(rs.getString("id")));
			   		Calendar when = Calendar.getInstance();
			    	when.setTimeInMillis(rs.getTimestamp("when").getTime());
			    	event.setWhen(when);
			    	event.setSummary(rs.getString("summary"));
			    	event.setDescription(rs.getString("description"));
			    		
			    	event.setOwner(calendarUserDao.getUser(rs.getInt("owner")));
			    	event.setAttendee(calendarUserDao.getUser(rs.getInt("attendee")));
			    			    	
		       		list.add(event);
		       	}		       	
		        return list;
		        }
		});
	}
	
	@Override
	public void deleteAll() {
		// Assignment 2
		this.jdbcTemplate.update("delete from events");
	}
}
