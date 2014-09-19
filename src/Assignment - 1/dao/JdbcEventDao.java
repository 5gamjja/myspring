package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.validator.util.privilegedactions.SetAccessibility;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

@Repository
public class JdbcEventDao implements EventDao {
    private DataSource dataSource;
    private List<Event> eventList;

    // --- constructors ---
    public JdbcEventDao() {
    }

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

    // --- EventService ---
    @Override
    public Event getEvent(int eventId) throws ClassNotFoundException, SQLException {
    	Connection c = dataSource.getConnection();
    	Calendar cal = Calendar.getInstance();
    	Event event = new Event();
		CalendarUser user1 = new CalendarUser();
		CalendarUser user2 = new CalendarUser();
		
		PreparedStatement ps = c.prepareStatement("select * from events where id = ?");
		ps.setInt(1, eventId);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		event.setId(rs.getInt("id"));
		cal.setTime(rs.getTimestamp("when"));
		event.setWhen(cal);
		event.setSummary(rs.getString("summary"));
		event.setDescription(rs.getString("description"));
		
		PreparedStatement ps1 = c.prepareStatement("select * from calendar_users where id = ?");
		ps1.setInt(1, rs.getInt("owner"));
		//ps1.setInt(1, test);
		
		ResultSet rs1 = ps1.executeQuery();
		rs1.next();
		
		user1.setId(rs1.getInt("id"));
		user1.setName(rs1.getString("name"));
		user1.setPassword(rs1.getString("password"));
		user1.setEmail(rs1.getString("email"));
				
		PreparedStatement ps2 = c.prepareStatement("select * from calendar_users where id = ?");
		ps2.setInt(1, rs.getInt("attendee"));
		ResultSet rs2 = ps2.executeQuery();
		rs2.next();
		
		user2.setId(rs2.getInt("id"));
		user2.setName(rs2.getString("name"));
		user2.setPassword(rs2.getString("password"));
		user2.setEmail(rs2.getString("email"));
		
		event.setOwner(user1);
		event.setAttendee(user2);
		
		rs.close();
		ps.close();
		rs1.close();
		ps1.close();
		rs2.close();
		ps2.close();
		c.close();
		
		return event;
    }

    @Override
    public int createEvent(final Event event) throws ClassNotFoundException, SQLException {
    	Connection c = dataSource.getConnection();
    	
    	PreparedStatement ps = c.prepareStatement("insert into events(when, summary, description, owner, attendee) values(?,?,?,?,?)");
    	Timestamp ts = new Timestamp( event.getWhen().getTimeInMillis() );
    	
    	ps.setTimestamp(1, ts);
    	ps.setString(2, event.getSummary());
		ps.setString(3, event.getDescription());
		ps.setInt(4,event.getOwner().getId());
		ps.setInt(5,event.getAttendee().getId());

		ps.close();
		c.close();	
		
    	return 0;
    }

    @Override
    public List<Event> findForUser(int userId) throws ClassNotFoundException, SQLException {
    	Connection c = dataSource.getConnection();
    	Calendar cal = Calendar.getInstance();
    	CalendarUser user1 = new CalendarUser();
		CalendarUser user2 = new CalendarUser();
		
    	
    	PreparedStatement ps = c.prepareStatement("select * from events where owner = ?");
    	ps.setInt(1, userId);

    	ResultSet rs = ps.executeQuery();
		
    	eventList = null;
		Event event = new Event();
		
		while(rs.next())
		{
			event.setId(rs.getInt("id"));
			cal.setTime(rs.getTimestamp("when"));
			event.setWhen(cal);
			event.setSummary(rs.getString("summary"));
			event.setDescription(rs.getString("description"));
			
			PreparedStatement ps1 = c.prepareStatement("select * from calendar_users where id = ?");
			ps1.setInt(1, rs.getInt("owner"));
			ResultSet rs1 = ps.executeQuery();
			rs1.next();
			
			user1.setId(rs1.getInt("id"));
			user1.setName(rs1.getString("name"));
			user1.setPassword(rs1.getString("password"));
			user1.setEmail(rs1.getString("email"));
			
			PreparedStatement ps2 = c.prepareStatement("select * from calendar_users where id = ?");
			ps2.setInt(1, rs.getInt("attendee"));
			ResultSet rs2 = ps.executeQuery();
			rs2.next();
			
			user2.setId(rs2.getInt("id"));
			user2.setName(rs2.getString("name"));
			user2.setPassword(rs2.getString("password"));
			user2.setEmail(rs2.getString("email"));
			
			event.setOwner(user1);
			event.setAttendee(user2);
			
			eventList.add(event);
			rs1.close();
			ps1.close();
			rs2.close();
			ps2.close();
		}
		
		rs.close();
		ps.close();
		c.close();
    	return eventList;
    }

    @Override
    public List<Event> getEvents() throws ClassNotFoundException, SQLException {
    	Connection c = dataSource.getConnection();
    	Calendar cal = Calendar.getInstance();
    	CalendarUser user1 = new CalendarUser();
		CalendarUser user2 = new CalendarUser();
		
    	PreparedStatement ps = c.prepareStatement("select * from events");
    	
    	ResultSet rs = ps.executeQuery();
		
    	eventList = null;
		Event event = new Event();
		
		while(rs.next())
		{
			event.setId(rs.getInt("id"));
			cal.setTime(rs.getTimestamp("when"));
			event.setWhen(cal);
			event.setSummary(rs.getString("summary"));
			
			event.setDescription(rs.getString("description"));

			PreparedStatement ps1 = c.prepareStatement("select * from calendar_users where id = ?");
			ps1.setInt(1, rs.getInt("owner"));
			ResultSet rs1 = ps.executeQuery();
			rs1.next();
			
			user1.setId(rs1.getInt("id"));
			user1.setName(rs1.getString("name"));
			user1.setPassword(rs1.getString("password"));
			user1.setEmail(rs1.getString("email"));
			
			PreparedStatement ps2 = c.prepareStatement("select * from calendar_users where id = ?");
			ps2.setInt(1, rs.getInt("attendee"));
			ResultSet rs2 = ps.executeQuery();
			rs2.next();
			
			user2.setId(rs2.getInt("id"));
			user2.setName(rs2.getString("name"));
			user2.setPassword(rs2.getString("password"));
			user2.setEmail(rs2.getString("email"));
			
			event.setOwner(user1);
			event.setAttendee(user2);
			
			eventList.add(event);
			rs1.close();
			ps1.close();
			rs2.close();
			ps2.close();
		}
		
		rs.close();
		ps.close();
		c.close();
    	return eventList;
    }

    /*
    private static final String EVENT_QUERY = "select e.id, e.summary, e.description, e.when, " +
            "owner.id as owner_id, owner.email as owner_email, owner.password as owner_password, owner.name as owner_name, " +
            "attendee.id as attendee_id, attendee.email as attendee_email, attendee.password as attendee_password, attendee.name as attendee_name " +
            "from events as e, calendar_users as owner, calendar_users as attendee " +
            "where e.owner = owner.id and e.attendee = attendee.id";
     */
}
