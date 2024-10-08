import java.awt.BorderLayout;

import javax.swing.*;

import bookmanagemnet.BookView;

public class Main extends JFrame{

	BookView book;
	
	Main(){
		book = new BookView();
		
		JTabbedPane jTabPane = new JTabbedPane();
		jTabPane.addTab("황모기 도서 대여&반납", book);
		
		
		add(jTabPane,BorderLayout.CENTER);
		
		setTitle("학생관리 & 도서관리 & 카페관리");
		setBounds(100, 100, 1200, 800);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		
		new Main();
	}
}
