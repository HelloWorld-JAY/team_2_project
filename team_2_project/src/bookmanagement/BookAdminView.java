package bookmanagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class BookAdminView extends JPanel{

	JComboBox<String>	largeCate,smallCate,searchCate;	// 대분류, 소분류, 검색분류

	JTextField	jtfSearch;								// 검색 텍스트필드

	JButton		bAdd,bModi,bDel;						// 추가, 수정 , 삭제

	String [] header = {"책번호","대분류","소분류","도서명","저자","츌판사","총수량","잔여수량","도서위치"};	// 테이블 헤더 셋팅

	DefaultTableModel model;														// 디폴트 테이블 모델 지정

	JTable table;																	// J테이블 선언

	// 모델단 선언
	BookDao dao;

	// 추가,수정 화면 변수
	JFrame jf;

	JComboBox<String>	jcLargeCategory;
	JComboBox<String>	jcSmallCategory;

	JTextField	jtfBookName,jtfAuthor,jtfPublisher,jtfLocation,jtfCount;		

	JLabel	jlLargeCategory	,jlSmallCategory,jlBookName,jlAuthor,jlPublisher,jlLocation,jlCount;

	JButton		jbAdd,jbCancel,jbModi;

	public BookAdminView(){

		addLayout();	// 화면출력 메서드
		eventProc();	// 이벤트 메서드
		connetDB();		// 디비 연결
		showAllItem();	// 모든 책정보 불러서 테이블에 출력하는 메서드
		resizeColumnWidth(table); // 테이블 컬럼 리사이즈

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

		String[] searchCateStr		= {"도서명","책고유번호","저자","츌판사","도서위치"};
		searchCate					= new JComboBox<>(searchCateStr);

		model 						= new DefaultTableModel(header,0){
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

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
		jtfSearch.setHorizontalAlignment(JTextField.LEFT);

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

		// 테이블 UI셋팅
		

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
				String category = (String)largeCate.getSelectedItem();

				if(category.equals("대분류")) {
					model.setNumRows(0);
					showAllItem();
				}else {
					largeCategorySelect(category);
				}
			}
		});
		// 카테고리 이벤트
		smallCate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String category = (String)smallCate.getSelectedItem();
				if(category.equals("소분류")) {
					model.setNumRows(0);
					showAllItem();
				}else {
					smallCategorySelect(category);
				}


			}
		});



		jtfSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int category 	= searchCate.getSelectedIndex();
				String search	= jtfSearch.getText();

				searchFieldAction(category,search);

			}
		});


		// 추가버튼 이벤트
		bAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				bookAddLayout();// 추가 화면 띄우기


			}
		});

		// 수정버튼 이벤트
		bModi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				BookVO vo = new BookVO();
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "수정할 도서를 선택해주세요.");
				}else {
					vo.setBookID(Integer.parseInt((String)table.getValueAt(row, 0)));
					vo.setBookName((String)table.getValueAt(row, 3));
					vo.setLargeCategory((String)table.getValueAt(row, 1));
					vo.setSmallCategory((String)table.getValueAt(row, 2));
					vo.setAuthor((String)table.getValueAt(row, 4));
					vo.setPublisher((String)table.getValueAt(row, 5));

					bookModiLayout(vo);


				}


			}
		});
		// 삭제버튼 이벤트
		bDel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				int row = table.getSelectedRow();
				
				if(row == -1) {
					JOptionPane.showMessageDialog(null, "수정할 도서를 선택해주세요.");
				}else {
					int bookID = (Integer.parseInt((String)table.getValueAt(row, 0)));
					String bookName = (String)table.getValueAt(row, 3);
					bookDelete(bookID,bookName);

				}
				
				
			
				
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
			System.out.println("전체불러오기 오류");
			e.printStackTrace();
		}

	}

	
	// 소분류 선택시 검색기능
	void smallCategorySelect(String category) {

		try {
			ArrayList<BookVO> list = dao.smallCategorySelect(category); 
			model.setNumRows(0);
			for(BookVO vo : list) {
				model.addRow(vo.toStringList());
			}
			model.fireTableDataChanged();

		} catch (Exception e) {
			System.out.println("카테고리서치 실패");
			e.printStackTrace();
		}

	}
	// 대분류 선택시 검색기능
	void largeCategorySelect(String category) {

		try {
			ArrayList<BookVO> list = dao.largeCategorySelect(category); 
			model.setNumRows(0);
			for(BookVO vo : list) {
				model.addRow(vo.toStringList());
			}
			model.fireTableDataChanged();

		} catch (Exception e) {
			System.out.println("카테고리서치 실패");
			e.printStackTrace();
		}

	}
	// 검색 필드 엔터 엑션
	void searchFieldAction(int category, String Search) {

		try {
			ArrayList<BookVO> list = dao.searchFieldAction(category,Search);
			model.setNumRows(0);
			for(BookVO vo : list) {
				model.addRow(vo.toStringList());
			}
			model.fireTableDataChanged();
		} catch (Exception e) {
			System.out.println("검색");
			e.printStackTrace();
		}
	}
	// 추가 화면설정
	void bookAddLayout(){

		jf = new JFrame();

		String[] largeCateStr 		= {"언어","역사","예술"};
		jcLargeCategory 	= new JComboBox<>(largeCateStr);
		String[] smallCateLangStr 	= {"한국어","영어","중국어","일본어","불어"};
		jcSmallCategory		= new JComboBox<>(smallCateLangStr);

		jtfBookName			= new JTextField(15);
		jtfAuthor			= new JTextField(15);
		jtfPublisher		= new JTextField(15);
		jtfLocation			= new JTextField(15);
		jtfCount			= new JTextField(15);

		jlLargeCategory		= new JLabel("대분류");
		jlSmallCategory		= new JLabel("소분류");
		jlBookName			= new JLabel("도서명");
		jlAuthor			= new JLabel("저자");
		jlPublisher			= new JLabel("출판사");
		jlLocation			= new JLabel("도서위치");
		jlCount				= new JLabel("입고수량");

		jbAdd				= new JButton("  등    록  ");
		jbCancel			= new JButton("  취    소  ");


		JPanel jp = new JPanel();

		jp.setBorder(new TitledBorder((new LineBorder(Color.black)),"도서추가",TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,18) ));
		jp.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 10, 20, 10);


		gbc.gridx = 0; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlLargeCategory,gbc);
		jlLargeCategory.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jcLargeCategory,gbc);
		jcLargeCategory.setPreferredSize(new Dimension(170,20));

		gbc.gridx = 0; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlSmallCategory,gbc);
		jlSmallCategory.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jcSmallCategory,gbc);
		jcSmallCategory.setPreferredSize(new Dimension(170,20));

		gbc.gridx = 0; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlBookName,gbc);
		jlBookName.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfBookName,gbc);

		gbc.gridx = 0; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlAuthor,gbc);
		jlAuthor.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfAuthor,gbc);

		gbc.gridx = 0; gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlPublisher,gbc);
		jlPublisher.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfPublisher,gbc);

		gbc.gridx = 0; gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlLocation,gbc);
		jlLocation.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfLocation,gbc);

		gbc.gridx = 0; gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlCount,gbc);
		jlCount.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfCount,gbc);

		gbc.gridx = 0; gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jbAdd,gbc);
		jbAdd.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jbCancel,gbc);
		jbCancel.setFont(new Font("돋음",Font.BOLD,15));

		//------------------------------------------추가버튼화면 이벤트
		jcLargeCategory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addComboboxSelect();
			}
		});

		// 추가화면의 등록버튼
		jbAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				addBook();
				jf.dispose();
				model.setNumRows(0);
				showAllItem();
			}
		});
		// 취소버튼 이벤트
		jbCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jf.dispose();				
			}
		});
		//------------------------------------------추가버튼화면 이벤트


		jf.add(jp);

		jf.setTitle("도서추가페이지");
		jf.setBounds(500,200,400,600);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
	}

	// 데이터데이스에 추가
	void addBook() {

		BookVO vo 	= new BookVO();
		int result 	= 0;
		vo.setBookName(jtfBookName.getText());
		vo.setLargeCategory((String)jcLargeCategory.getSelectedItem());
		vo.setSmallCategory((String)jcSmallCategory.getSelectedItem());
		vo.setAuthor(jtfAuthor.getText());
		vo.setPublisher(jtfPublisher.getText());
		vo.setLocation(jtfLocation.getText());
		int count = Integer.parseInt(jtfCount.getText()); 

		try {
			result = dao.addBookInsert(vo,count);

			JOptionPane.showMessageDialog(null,vo.getBookName()+" 도서 "+ result+"권 입고 되었습니다.");

		} catch (Exception e) {
			System.out.println("[추가sql문제발생]:" + e.getMessage());
			e.printStackTrace();
		}


	}
	void addComboboxSelect() {

		if(jcLargeCategory.getSelectedIndex() == 0) {
			String[] smallCateLangStr 	= {"한국어","영어","중국어","일본어","불어"};
			jcSmallCategory.setModel(new DefaultComboBoxModel<>(smallCateLangStr));

		}else if(jcLargeCategory.getSelectedIndex() == 1) {
			String[] smallCateHisStr 	= {"아시아","유럽","북아메리카","남아메리카","아프리카"};
			jcSmallCategory.setModel(new DefaultComboBoxModel<>(smallCateHisStr));

		}else if(jcLargeCategory.getSelectedIndex() == 2) {
			String[] smallCateArtStr	= {"건축","사진","연극","음악","미술"};
			jcSmallCategory.setModel(new DefaultComboBoxModel<>(smallCateArtStr));

		}

	}
	void bookModiLayout(BookVO vo){

		jf = new JFrame();

		String[] largeCateStr 		= {"언어","역사","예술"};
		jcLargeCategory 	= new JComboBox<>(largeCateStr);
		String[] smallCateLangStr 	= {"한국어","영어","중국어","일본어","불어"};
		jcSmallCategory		= new JComboBox<>(smallCateLangStr);

		jtfBookName			= new JTextField(15);
		jtfAuthor			= new JTextField(15);
		jtfPublisher		= new JTextField(15);
		jtfLocation			= new JTextField(15);

		jlLargeCategory		= new JLabel("대분류");
		jlSmallCategory		= new JLabel("소분류");
		jlBookName			= new JLabel("도서명");
		jlAuthor			= new JLabel("저자");
		jlPublisher			= new JLabel("출판사");
		jlLocation			= new JLabel("도서위치");


		jbModi				= new JButton("  수    정  ");
		jbCancel			= new JButton("  취    소  ");


		JPanel jp = new JPanel();

		jp.setBorder(new TitledBorder((new LineBorder(Color.black)), Integer.toString(vo.getBookID()) +"."+vo.getBookName()+" 도서 수정",TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,14) ));
		jp.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(30, 10, 20, 10);


		gbc.gridx = 0; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlLargeCategory,gbc);
		jlLargeCategory.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jcLargeCategory,gbc);
		jcLargeCategory.setPreferredSize(new Dimension(170,20));

		gbc.gridx = 0; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlSmallCategory,gbc);
		jlSmallCategory.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jcSmallCategory,gbc);
		jcSmallCategory.setPreferredSize(new Dimension(170,20));

		gbc.gridx = 0; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlBookName,gbc);
		jlBookName.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfBookName,gbc);

		gbc.gridx = 0; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlAuthor,gbc);
		jlAuthor.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfAuthor,gbc);

		gbc.gridx = 0; gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlPublisher,gbc);
		jlPublisher.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfPublisher,gbc);

		gbc.gridx = 0; gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jlLocation,gbc);
		jlLocation.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jtfLocation,gbc);


		gbc.gridx = 0; gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.WEST;
		jp.add(jbModi,gbc);
		jbModi.setFont(new Font("돋음",Font.BOLD,15));
		gbc.gridx = 1; gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.EAST;
		jp.add(jbCancel,gbc);
		jbCancel.setFont(new Font("돋음",Font.BOLD,15));

		jcLargeCategory.setSelectedItem(vo.getLargeCategory());;
		jcSmallCategory.setSelectedItem(vo.getSmallCategory());;
		jtfBookName.setText(vo.getBookName());
		jtfAuthor.setText(vo.getAuthor());
		jtfPublisher.setText(vo.getPublisher());
		jtfLocation.setText(vo.getLocation());
		
		//------------------------------------------수정버튼화면 이벤트
		jcLargeCategory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addComboboxSelect();
			}
		});
		jbModi.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				vo.setLargeCategory((String)jcLargeCategory.getSelectedItem());
				vo.setSmallCategory((String)jcSmallCategory.getSelectedItem());
				vo.setBookName(jtfBookName.getText());
				vo.setAuthor(jtfAuthor.getText());
				vo.setPublisher(jtfPublisher.getText());
				vo.setLocation(jtfLocation.getText());
				
				bookUpdate(vo);
				jf.dispose();
				model.setNumRows(0);
				showAllItem();

			}
		});
		jbCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jf.dispose();

			}
		});

		//------------------------------------------수정버튼화면 이벤트

		jf.add(jp);


		jf.setTitle("도서추가페이지");
		jf.setBounds(500,200,400,600);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
	}

	void bookUpdate(BookVO vo) {



		try {
			int result = dao.bookUpdate(vo);
			if(result == 1) {
				JOptionPane.showMessageDialog(null, vo.getBookName()+" 도서내용 수정 하였습니다"  );
			}else {
				JOptionPane.showMessageDialog(null, "수정실패");
			}
		} catch (Exception e) {
			System.out.println("[수정sql문제발생]:" + e.getMessage());
			e.printStackTrace();
		}

	}
	void bookDelete(int bookID,String bookName) {
		
		try {
			int result = dao.bookDelete(bookID);
			if(result == 1) {
				JOptionPane.showMessageDialog(null, bookName +" 도서를 삭제 하였습니다"  );
				model.setNumRows(0);
				showAllItem();
			}else {
				JOptionPane.showMessageDialog(null, "삭제실패");
			}
		} catch (Exception e) {
			System.out.println("[삭제sql문제발생]:" + e.getMessage());
			e.printStackTrace();
		}
	}
	// 테이블 사이즈 자동 조정 메서드
		public void resizeColumnWidth(JTable table) {
		    final TableColumnModel columnModel = table.getColumnModel();
		    for (int column = 0; column < table.getColumnCount(); column++) {
		        int width = 50; // Min width
		        for (int row = 0; row < table.getRowCount(); row++) {
		            TableCellRenderer renderer = table.getCellRenderer(row, column);
		            Component comp = table.prepareRenderer(renderer, row, column);
		            width = Math.max(comp.getPreferredSize().width +1 , width);
		        }
		        columnModel.getColumn(column).setPreferredWidth(width);
		    }
		}
}
