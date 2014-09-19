package com.mycompany.myapp.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.mycompany.myapp.domain.CalendarUser;

public interface CalendarUserDao {
	
    CalendarUser getUser(int id) throws ClassNotFoundException, SQLException;

    CalendarUser findUserByEmail(String email) throws ClassNotFoundException, SQLException;

    List<CalendarUser> findUsersByEmail(String partialEmail) throws ClassNotFoundException, SQLException;

    int createUser(CalendarUser user) throws ClassNotFoundException, SQLException;
}
