package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mycompany.myapp.dao.CalendarUserDao;
import com.mycompany.myapp.dao.EventAttendeeDao;
import com.mycompany.myapp.dao.EventDao;
import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.domain.EventAttendee;
import com.mycompany.myapp.domain.EventLevel;

@Service
public class DefaultCalendarService implements CalendarService {
	public static final int MIN_NUMLIKE_FOR_HOT = 10;

	@Autowired
	private EventDao eventDao;

	@Autowired
	private CalendarUserDao userDao;

	@Autowired
	private EventAttendeeDao eventAttendeeDao;

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	
	public void setEventDao(EventDao eventDao) {
		this.eventDao = eventDao;
	}
	
	/* CalendarUser */
	@Override
	public CalendarUser getUser(int id) {
		// TODO Assignment 3
		return userDao.findUser(id);
	}

	@Override
	public CalendarUser getUserByEmail(String email) {
		// TODO Assignment 3
		return userDao.findUserByEmail(email);
	}

	@Override
	public List<CalendarUser> getUsersByEmail(String partialEmail) {
		// TODO Assignment 3
		return userDao.findUsersByEmail(partialEmail);
	}

	@Override
	public int createUser(CalendarUser user) {
		// TODO Assignment 3
		return userDao.createUser(user);
	}

	@Override
	public void deleteAllUsers() {
		// TODO Assignment 3
		userDao.deleteAll();
	}

	/* Event */
	@Override
	public Event getEvent(int eventId) {
		// TODO Assignment 3
		return eventDao.findEvent(eventId);
	}

	@Override
	public List<Event> getEventForOwner(int ownerUserId) {
		// TODO Assignment 3
		return eventDao.findForOwner(ownerUserId);
	}

	@Override
	public List<Event> getAllEvents() {
		// TODO Assignment 3
		return eventDao.findAllEvents();
	}

	@Override
	public int createEvent(Event event) {
		// TODO Assignment 3
		if (event.getEventLevel() == null) {
			event.setEventLevel(EventLevel.NORMAL);
		}
		return eventDao.createEvent(event);
	}

	@Override
	public void deleteAllEvents() {
		// TODO Assignment 3
		eventDao.deleteAll();
	}

	/* EventAttendee */
	@Override
	public List<EventAttendee> getEventAttendeeByEventId(int eventId) {
		// TODO Assignment 3
		return eventAttendeeDao.findEventAttendeeByEventId(eventId);
	}

	@Override
	public List<EventAttendee> getEventAttendeeByAttendeeId(int attendeeId) {
		// TODO Assignment 3
		return eventAttendeeDao.findEventAttendeeByAttendeeId(attendeeId);
	}

	@Override
	public int createEventAttendee(EventAttendee eventAttendee) {
		// TODO Assignment 3
		return eventAttendeeDao.createEventAttendee(eventAttendee);
	}

	@Override
	public void deleteEventAttendee(int id) {
		// TODO Assignment 3
		eventAttendeeDao.deleteEventAttendee(id);
	}

	@Override
	public void deleteAllEventAttendees() {
		// TODO Assignment 3
		eventAttendeeDao.deleteAll();
	}

	/* upgradeEventLevels */
	@Override
	public void upgradeEventLevels() throws Exception {
		// TODO Assignment 3
		// 트랜잭션 관련 코딩 필요함
		TransactionStatus status = this.transactionManager
				.getTransaction(new DefaultTransactionDefinition());
		try {
			List<Event> events = eventDao.findAllEvents();
			for (Event event : events) {
				if (canUpgradeEventLevel(event))
					upgradeEventLevel(event);
			}
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			this.transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public boolean canUpgradeEventLevel(Event event) {
		// TODO Assignment 3
		EventLevel currentLevel = event.getEventLevel();
		switch (currentLevel) {
		case NORMAL:
			return (event.getNumLikes() >= MIN_NUMLIKE_FOR_HOT);
		case HOT:
			return false;
		default:
			throw new IllegalArgumentException("Unknown Level: " + currentLevel);
		}
	}

	@Override
	public void upgradeEventLevel(Event event) {
		event.upgradeLevel();
		eventDao.updateEvent(event);
	}
}