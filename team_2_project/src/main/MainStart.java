package main;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import bookmanagement.BookAdminView;
import bookmanagement.BookView;
import chart.ChartView;
import login.Login;
import studnetmanagement.StudentView;

public class MainStart extends JFrame{

	BookView 		book;
	BookAdminView 	bookAdmin;
	Login	 		login;
	JTabbedPane 	jTabPane;
	StudentView		student;
	ChartView		chart;
	
	public MainStart(){
		book 		= new BookView();
		bookAdmin	= new BookAdminView();
		login		= new Login(this);
		student		= new StudentView();
		chart		= new ChartView();
		
		jTabPane 	= new JTabbedPane();

		jTabPane.addTab("로그인페이지", login);
		
		
	
		add(jTabPane,BorderLayout.CENTER);

		setTitle("학생관리 & 도서관리");
		setBounds(100, 100, 1200, 800);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void adminLoginTab() {
		setTitle("학생관리,도서관리,");
		jTabPane.remove(login);
		jTabPane.addTab("학생 관리", student);
		jTabPane.addTab("도서 관리", bookAdmin);
		jTabPane.addTab("도서 대여 통계", chart);
		// 추후 도서관리 , 카페관리 , 학생관리페이지 추가
		
	}

	public void userLoginTab() {
		setTitle("도서 대여&반납");
		jTabPane.remove(login);

		jTabPane.addTab("도서 대여&반납", book);  // 도서 대여/반납 탭 추가

	}

	public static void main(String[] args) {

		new MainStart();
	}
}
