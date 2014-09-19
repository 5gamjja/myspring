package com.mycompany.myapp.dao;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.myapp.domain.Event;

public interface EventDao {

    Event getEvent(int eventId) throws ClassNotFoundException, SQLException;

    int createEvent(Event event) throws ClassNotFoundException, SQLException;

    List<Event> findForUser(int userId) throws ClassNotFoundException, SQLException;

    List<Event> getEvents() throws ClassNotFoundException, SQLException;
}