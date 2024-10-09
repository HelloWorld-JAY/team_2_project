package bookmanagemnet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;

public class BookAdminView extends JPanel{

	JComboBox<String>	largeCate,smallCate,searchCate;	// 대분류, 소분류, 검색분류

	JTextField	jtfSearch;								// 검색 텍스트필드

	JButton		bAdd,bModi,bDel;						// 추가, 수정 , 삭제

	String [] header = {"대분류","소분류","도서명","저자","츌판사","총수량","잔여수량","도서위치"};	// 테이블 헤더 셋팅

	DefaultTableModel model;														// 디폴트 테이블 모델 지정

	JTable table;																	// J테이블 선언

	// 모델단 선언
	BookDao dao;
	
	public BookAdminView(){
		
		addLayout();	// 화면출력 메서드
		eventProc();	// 이벤트 메서드
		connetDB();		// 디비 연결
		showAllItem();	// 모든 책정보 불러서 테이블에 출력하는 메서드


	}
	// 디비 연결
	void connetDB() {
		try {
			dao = new BookDaoImpl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 화면출력 메서드
	void addLayout() {
		//--------------------------------------------------------------------------화면 출력 변수 초기화
		String[] largeCateStr 		= {"대분류","언어","역사","예술"};
		largeCate					= new JComboBox<>(largeCateStr);
		String[] smallCateStr 		= {"소분류"};
		smallCate					= new JComboBox<>(smallCateStr);

		String[] searchCateStr		= {"도서명","저자","츌판사","도서위치"};
		searchCate					= new JComboBox<>(searchCateStr);

		model 						= new DefaultTableModel(header,0);
		table						= new JTable(model);

		bAdd						= new JButton(" 추     가 ");
		bModi						= new JButton(" 수     정 ");
		bDel						= new JButton(" 삭     제 ");
		
		jtfSearch					= new JTextField(20);
		// 북쪽 화면셋팅

		JPanel north_panel = new JPanel();
		north_panel.setLayout(new FlowLayout(FlowLayout.LEFT,0,15));
		north_panel.add(largeCate);
		largeCate.setPreferredSize(new Dimension(200,20));
		north_panel.add(new JLabel("       "));
		north_panel.add(smallCate);
		smallCate.setPreferredSize(new Dimension(200,20));
		north_panel.add(new JLabel("                                                                                                                                       "));
		north_panel.add(searchCate);
		north_panel.add(new JLabel("   "));
		searchCate.setPreferredSize(new Dimension(120,20));
		north_panel.add(jtfSearch);
		jtfSearch.setHorizontalAlignment(JTextField.RIGHT);

		// 남쪽 화면셋팅
		JPanel south_panel = new JPanel();
		south_panel.setLayout(new FlowLayout(FlowLayout.CENTER,150,15));
		south_panel.add(bAdd);
		bAdd.setPreferredSize(new Dimension(180,40));
		bAdd.setFont(new Font("돋음",Font.BOLD,20));;
		south_panel.add(bModi);
		bModi.setPreferredSize(new Dimension(180,40));
		bModi.setFont(new Font("돋음",Font.BOLD,20));;
		south_panel.add(bDel);
		bDel.setPreferredSize(new Dimension(180,40));
		bDel.setFont(new Font("돋음",Font.BOLD,20));;

		//화면배치
		setLayout(new BorderLayout());						// 메인 레이아웃은 보더레이아웃

		add(north_panel,BorderLayout.NORTH);				// 북쪽에 판넬 배치
		add(new JScrollPane(table),BorderLayout.CENTER);	// 중앙에 테이블 배치
		add(south_panel,BorderLayout.SOUTH);				// 남쪽에 판넬 배치
	}
	// 이벤트 메서드
	void eventProc() {
		// 카테고리 이벤트
		largeCate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comboboxSelect();							// 대분류 카테고리 객체 선택시 메서드 호출
			}
		});



		// 추가버튼 이벤트
		bAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 추가 화면 띄우기
				//
			}
		});
		// 수정버튼 이벤트
		bModi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		// 삭제버튼 이벤트
		bDel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});


	}
	// 대분류 카테고리 선택시 소분류 카테고리 재설정
	void comboboxSelect() {

		if(largeCate.getSelectedIndex() == 0) {
			String[] smallCateStr 	= {"소분류"};
			smallCate.setModel(new DefaultComboBoxModel<>(smallCateStr));
		}else if(largeCate.getSelectedIndex() == 1) {
			String[] smallCateLangStr 	= {"한국어","영어","중국어","일본어","불어"};
			smallCate.setModel(new DefaultComboBoxModel<>(smallCateLangStr));

		}else if(largeCate.getSelectedIndex() == 2) {
			String[] smallCateHisStr 	= {"아시아","유럽","북아메리카","남아메리카","아프리카"};
			smallCate.setModel(new DefaultComboBoxModel<>(smallCateHisStr));

		}else if(largeCate.getSelectedIndex() == 3) {
			String[] smallCateArtStr	= {"건축","사진","연극","음악","미술"};
			smallCate.setModel(new DefaultComboBoxModel<>(smallCateArtStr));

		}

	}
	// 모든 책정보 불러서 테이블에 출력하는 메서드
	void showAllItem() {
		
		try {
			ArrayList<BookVO> list = dao.bookSelectAll(); 
			for(BookVO vo : list) {
				model.addRow(vo.toStringList());
			}
			model.fireTableDataChanged();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}


}
