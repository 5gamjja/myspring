package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;




import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

public class DaoTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("com/mycompany/myapp/applicationContext.xml"); //genericxmlapplicationcontext //xml 만들고 xml 참조
		
		//1. 디폴트로 등록된 CalendarUser 3명 출력 (패스워드 제외한 모든 내용 출력)
		JdbcCalendarUserDao jdbcCalendarUserDao1 = context.getBean("jdbcCalendarUserDao", JdbcCalendarUserDao.class);
		CalendarUser user1 = new CalendarUser();
		
		System.out.println("문제 1");
		user1 = jdbcCalendarUserDao1.getUser(1);		
		System.out.println("Id : " + user1.getId() + " Email : " + user1.getEmail() + " Name : " + user1.getName());
		user1 = jdbcCalendarUserDao1.getUser(2);
		System.out.println("Id : " + user1.getId() + " Email : " + user1.getEmail() + " Name : " + user1.getName());
		user1 = jdbcCalendarUserDao1.getUser(3);
		System.out.println("Id : " + user1.getId() + " Email : " + user1.getEmail() + " Name : " + user1.getName());
		
		//2. 디폴트로 등록된 Event 3개 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력) 
		JdbcEventDao jdbcEventDao2 = context.getBean("jdbcEventDao", JdbcEventDao.class);
		Event event2 = new Event();
		
		System.out.println("문제 2");
		event2 = jdbcEventDao2.getEvent(100);
		System.out.println("Id : " + event2.getId() + " When : " + event2.getWhen() + " Summary : " + event2.getSummary());
		System.out.println("Owner Name : " + event2.getOwner().getName() + " Owner Email : " + event2.getOwner().getEmail());
		System.out.println("Attendee Name : " + event2.getAttendee().getName() + " Attendee Email : " + event2.getAttendee().getEmail());
		event2 = jdbcEventDao2.getEvent(101);
		System.out.println("Id : " + event2.getId() + " When : " + event2.getWhen() + " Summary : " + event2.getSummary());
		System.out.println("Owner Name : " + event2.getOwner().getName() + " Owner Email : " + event2.getOwner().getEmail());
		System.out.println("Attendee Name : " + event2.getAttendee().getName() + " Attendee Email : " + event2.getAttendee().getEmail());
		event2 = jdbcEventDao2.getEvent(102);
		System.out.println("Id : " + event2.getId() + " When : " + event2.getWhen() + " Summary : " + event2.getSummary());
		System.out.println("Owner Name : " + event2.getOwner().getName() + " Owner Email : " + event2.getOwner().getEmail());
		System.out.println("Attendee Name : " + event2.getAttendee().getName() + " Attendee Email : " + event2.getAttendee().getEmail());
		
		//아이디는 생성시 입력부분은 아니고 디비가 알아서 생성
		//아이디 추출이란 이메일, 이름등으로 추출하는거로

		//3. 새로운 CalendarUser 2명 등록 및 각각 id 추출
		JdbcCalendarUserDao jdbcCalendarUserDao3_1 = context.getBean("jdbcCalendarUserDao", JdbcCalendarUserDao.class);
		CalendarUser user3_1 = new CalendarUser();
		user3_1.setName("Kyeong Min");
		user3_1.setEmail("5gamjja@kut.ac.kr");
		user3_1.setPassword("Link");
		jdbcCalendarUserDao3_1.createUser(user3_1);
		
		JdbcCalendarUserDao jdbcCalendarUserDao3_2 = context.getBean("jdbcCalendarUserDao", JdbcCalendarUserDao.class);
		CalendarUser user3_2 = new CalendarUser();
		user3_2.setName("Kyeong Min Lee");
		user3_2.setEmail("5gamjja@koreatech.ac.kr");
		user3_2.setPassword("Lab");
		jdbcCalendarUserDao3_2.createUser(user3_2);
		
		user3_1 = jdbcCalendarUserDao3_1.findUserByEmail("5gamjja@kut.ac.kr");
		user3_2 = jdbcCalendarUserDao3_2.findUserByEmail("5gamjja@koreatech.ac.kr");
		
		System.out.println("문제 3");		
		System.out.println("Id : " + user3_1.getId() + " Email : " + user3_1.getEmail() + " Name : " + user3_1.getName());
		System.out.println("Id : " + user3_2.getId() + " Email : " + user3_2.getEmail() + " Name : " + user3_2.getName());
		
		//4. 추출된 id와 함께 새로운 CalendarUser 2명을 DB에서 가져와 (getUser 메소드 사용) 방금 등록된 2명의 사용자와 내용 (이메일, 이름, 패스워드)이 일치하는 지 비교
		JdbcCalendarUserDao jdbcCalendarUserDao4_1 = context.getBean("jdbcCalendarUserDao", JdbcCalendarUserDao.class);
		CalendarUser user4_1 = new CalendarUser();
		JdbcCalendarUserDao jdbcCalendarUserDao4_2 = context.getBean("jdbcCalendarUserDao", JdbcCalendarUserDao.class);
		CalendarUser user4_2 = new CalendarUser();
		
		user4_1 = jdbcCalendarUserDao4_1.getUser(user3_1.getId());
		user4_2 = jdbcCalendarUserDao4_2.getUser(user3_2.getId());
		
		System.out.println("문제 4");
		System.out.println("Id : " + user4_1.getId() + " Email : " + user4_1.getEmail() + " Name : " + user4_1.getName());
		System.out.println("Id : " + user4_2.getId() + " Email : " + user4_2.getEmail() + " Name : " + user4_2.getName());

		//5. 5명의 CalendarUser 모두 출력 (패스워드 제외한 모든 내용 출력)
		JdbcCalendarUserDao jdbcCalendarUserDao5 = context.getBean("jdbcCalendarUserDao", JdbcCalendarUserDao.class);
		CalendarUser user5 = new CalendarUser();
		
		System.out.println("문제 5");
		user5 = jdbcCalendarUserDao5.getUser(1);		
		System.out.println("Id : " + user5.getId() + " Email : " + user5.getEmail() + " Name : " + user5.getName());
		user5 = jdbcCalendarUserDao5.getUser(2);		
		System.out.println("Id : " + user5.getId() + " Email : " + user5.getEmail() + " Name : " + user5.getName());
		user5 = jdbcCalendarUserDao5.getUser(3);		
		System.out.println("Id : " + user5.getId() + " Email : " + user5.getEmail() + " Name : " + user5.getName());
		user5 = jdbcCalendarUserDao5.getUser(4);		
		System.out.println("Id : " + user5.getId() + " Email : " + user5.getEmail() + " Name : " + user5.getName());
		user5 = jdbcCalendarUserDao5.getUser(5);		
		System.out.println("Id : " + user5.getId() + " Email : " + user5.getEmail() + " Name : " + user5.getName());
		
		//6. 새로운 Event 2개 등록 및 각각 id 추출		
		JdbcEventDao jdbcEventDao6_1 = context.getBean("jdbcEventDao", JdbcEventDao.class);
		Event event6_1 = new Event();
		JdbcEventDao jdbcEventDao6_2 = context.getBean("jdbcEventDao", JdbcEventDao.class);
		Event event6_2 = new Event();
		
		Calendar cal6_1 = Calendar.getInstance();
		Calendar cal6_2 = Calendar.getInstance();
		
		event6_1.setWhen(cal6_1);
		event6_1.setSummary("Exam1");
		event6_1.setDescription("Mid term exam");
		event6_1.setOwner(user4_1);
		event6_1.setAttendee(user4_2);
		
		event6_2.setWhen(cal6_2);
		event6_2.setSummary("Exam2");
		event6_2.setDescription("Final exam");
		event6_2.setOwner(user4_2);
		event6_2.setAttendee(user4_1);
		
		jdbcEventDao6_1.createEvent(event6_1);
		jdbcEventDao6_1.createEvent(event6_2);
		
		System.out.println("문제 6");
		event6_1 = (Event) jdbcEventDao6_1.findForUser(user4_1.getId());
		System.out.println("Id : " + event6_1.getId() + " When : " + event6_1.getWhen() + " Summary : " + event6_1.getSummary());
		System.out.println("Owner Name : " + event6_1.getOwner().getName() + " Owner Email : " + event6_1.getOwner().getEmail());
		System.out.println("Attendee Name : " + event6_1.getAttendee().getName() + " Attendee Email : " + event6_1.getAttendee().getEmail());
		
		event6_2 = (Event) jdbcEventDao6_1.findForUser(user4_2.getId());
		System.out.println("Id : " + event6_2.getId() + " When : " + event6_2.getWhen() + " Summary : " + event6_2.getSummary());
		System.out.println("Owner Name : " + event6_2.getOwner().getName() + " Owner Email : " + event6_2.getOwner().getEmail());
		System.out.println("Attendee Name : " + event6_2.getAttendee().getName() + " Attendee Email : " + event6_2.getAttendee().getEmail());
		
		//7. 추출된 id와 함께 새로운 Event 2개를 DB에서 가져와 (getEvent 메소드 사용) 방금 추가한 2개의 이벤트와 내용 (when, summary, description, owner, attendee)이 일치하는 지 비교
		JdbcEventDao jdbcEventDao7_1 = context.getBean("jdbcEventDao", JdbcEventDao.class);
		Event event7_1 = new Event();
		JdbcEventDao jdbcEventDao7_2 = context.getBean("jdbcEventDao", JdbcEventDao.class);
		Event event7_2 = new Event();
	
		event7_1 = jdbcEventDao7_1.getEvent(103);
		event7_2 = jdbcEventDao7_2.getEvent(104);
		
		System.out.println("문제 7");
		
		System.out.println("Id : " + event7_1.getId() + " When : " + event7_1.getWhen() + " Summary : " + event7_1.getSummary());
		System.out.println("Owner Name : " + event7_1.getOwner().getName() + " Owner Email : " + event7_1.getOwner().getEmail());
		System.out.println("Attendee Name : " + event7_1.getAttendee().getName() + " Attendee Email : " + event7_1.getAttendee().getEmail());
		
		System.out.println("Id : " + event7_2.getId() + " When : " + event7_2.getWhen() + " Summary : " + event7_2.getSummary());
		System.out.println("Owner Name : " + event7_2.getOwner().getName() + " Owner Email : " + event7_2.getOwner().getEmail());
		System.out.println("Attendee Name : " + event7_2.getAttendee().getName() + " Attendee Email : " + event7_2.getAttendee().getEmail());
		
		//8. 5개의 Event 모두 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력)
		JdbcEventDao jdbcEventDao8 = context.getBean("jdbcEventDao", JdbcEventDao.class);
		Event event8 = new Event();
		
		System.out.println("문제 8");
		event8 = jdbcEventDao8.getEvent(100);		
		System.out.println("Id : " + event8.getId() + " When : " + event8.getWhen());
		System.out.println("Sumarry : " + event8.getSummary());
		System.out.println("Owner Name : " + event8.getOwner().getName() + " Owner Email : " + event8.getOwner().getEmail());
		System.out.println("Attendee Name : " + event8.getAttendee().getName() + " Attendee Email : " + event8.getAttendee().getEmail());
		event8 = jdbcEventDao8.getEvent(101);		
		System.out.println("Id : " + event8.getId() + " When : " + event8.getWhen() + " Summary : " + event8.getSummary());
		System.out.println("Owner Name : " + event8.getOwner().getName() + " Owner Email : " + event8.getOwner().getEmail());
		System.out.println("Attendee Name : " + event8.getAttendee().getName() + " Attendee Email : " + event8.getAttendee().getEmail());
		event8 = jdbcEventDao8.getEvent(102);		
		System.out.println("Id : " + event8.getId() + " When : " + event8.getWhen() + " Summary : " + event8.getSummary());
		System.out.println("Owner Name : " + event8.getOwner().getName() + " Owner Email : " + event8.getOwner().getEmail());
		System.out.println("Attendee Name : " + event8.getAttendee().getName() + " Attendee Email : " + event8.getAttendee().getEmail());
		event8 = jdbcEventDao8.getEvent(103);		
		System.out.println("Id : " + event8.getId() + " When : " + event8.getWhen() + " Summary : " + event8.getSummary());
		System.out.println("Owner Name : " + event8.getOwner().getName() + " Owner Email : " + event8.getOwner().getEmail());
		System.out.println("Attendee Name : " + event8.getAttendee().getName() + " Attendee Email : " + event8.getAttendee().getEmail());
		event8 = jdbcEventDao8.getEvent(104);		
		System.out.println("Id : " + event8.getId() + " When : " + event8.getWhen() + " Summary : " + event8.getSummary());
		System.out.println("Owner Name : " + event8.getOwner().getName() + " Owner Email : " + event8.getOwner().getEmail());
		System.out.println("Attendee Name : " + event8.getAttendee().getName() + " Attendee Email : " + event8.getAttendee().getEmail());
	}
}
