package bookmanagemnet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
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

public class BookView extends JPanel{

	JComboBox<String>	largeCate,smallCate,searchCate;	// 대분류, 소분류, 검색분류

	JTextField	jtfSearch;								// 검색 텍스트필드

	JButton		bBorrow,breturn;						// 대여, 반납 버튼

	String [] header = {"책번호","대분류","소분류","도서명","저자","츌판사","총수량","잔여수량","도서위치"};	// 테이블 헤더 셋팅

	DefaultTableModel model;																// 디폴트 테이블 모델 지정

	JTable table;																			// J테이블 선언

	// 모델단 선언
	BookDao dao;


	// 대여화면 맴버변수
	JFrame 		jf;
	
	JLabel 		jlImage,jlStudentID,jlStudentName,jlStudentTel,jlStudentGender,jlStudentAge,jlClass,jlCountID;
	
	JTextField 	jtfStudentID,jtfStudentName,jtfStudentTel,jtfStudentAge,jtfClass,jtfStudentGender,jtfCountID;
	
	JButton		jbBorrowYes,jbBorrowNo;
	// 이미지
	ImageIcon icon,imageicon;
	Image image,scaleimage;	
	
	
	public BookView(){

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

		String[] searchCateStr		= {"도서명","책고유번호","저자","츌판사","도서위치"};
		searchCate					= new JComboBox<>(searchCateStr);

		model 						= new DefaultTableModel(header,0){
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		table						= new JTable(model);

		bBorrow						= new JButton(" 대     여 ");
		breturn						= new JButton(" 반     납 ");

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
		south_panel.setLayout(new FlowLayout(FlowLayout.CENTER,300,15));
		south_panel.add(bBorrow);
		bBorrow.setPreferredSize(new Dimension(180,40));
		bBorrow.setFont(new Font("돋음",Font.BOLD,20));;
		south_panel.add(breturn);
		breturn.setPreferredSize(new Dimension(180,40));
		breturn.setFont(new Font("돋음",Font.BOLD,20));;


		// 테이블 UI셋팅
		table.getColumn("도서명").setPreferredWidth(250);

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

		// 대여버튼 이벤트
		bBorrow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String borrowBookID = enterBookUnique();

				if(borrowBookID.equals("")) {}				
				else {
					String address = imageAddressSelect(Integer.parseInt(borrowBookID));// 해당북아이디로 책정보 불러오는 메서드

					borrowLayout(address,borrowBookID);// 창 띄우고 책정보 입력 및 대여 학번 받아서 통계 관련 정보 저장.

					// 책대여기간 알려주는 창 띄우고 마무리.
				}

			}
		});
		// 반납버튼 이벤트
		breturn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String returnBookID = enterBookUnique();

				if(returnBookID.equals("")) {}				
				else {
					// 해당북아이디로 책정보 및 대여정보 불러오는 메서드

					// 해당 책정보 및 대여정보,반납여부 묻는 창 띄우기.

					// 반납이 완료되었습니다 창띄우기.(연체료 계산까지)
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

	// 책고유번호 입력하는 창 띄우고 고유번호 리턴하는 메서드
	String enterBookUnique() {

		JPanel panel = new JPanel(); 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel jl = new JLabel("책고유번호를 입력해주세요.");
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField jtf = new JTextField(20);
		jtf.setHorizontalAlignment(JTextField.CENTER);

		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(jl,gbc);
		gbc.gridy = 1;
		panel.add(jtf,gbc);

		int result = JOptionPane.showOptionDialog(null, panel, "고유번호 입력",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				new Object[]{"확인","취소"}, null);

		if(result == JOptionPane.OK_OPTION) {
			return jtf.getText();
		}else {
			return "";
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
	// 대여창화면 생성
	void borrowLayout(String address,String borrowBookID) {

		jf 				 = new JFrame();

		jlStudentID 	 = new JLabel("학번입력");
		jlClass			 = new JLabel("학과");
		jlStudentName	 = new JLabel("이름");
		jlStudentTel	 = new JLabel("전화번호");
		jlStudentAge	 = new JLabel("나이");
		jlStudentGender	 = new JLabel("성별");
		jlCountID		 = new JLabel("책고유번호");
		
		jtfStudentID	 = new JTextField(17);
		jtfClass		 = new JTextField(17);
		jtfStudentName	 = new JTextField(17);
		jtfStudentTel	 = new JTextField(17);
		jtfStudentAge	 = new JTextField(17);
		jtfStudentGender = new JTextField(17);
		jtfCountID		 = new JTextField(17);
		
		jbBorrowYes		 = new JButton(" 확   인 ");
		jbBorrowNo		 = new JButton(" 취   소 ");
		// 이미지 설정
		jlImage			 = new JLabel(" ",JLabel.CENTER);
		icon			 = new ImageIcon(address);
		image			 = icon.getImage();
		scaleimage 		 = image.getScaledInstance(200, 250, image.SCALE_SMOOTH);
		imageicon 		 = new ImageIcon(scaleimage);
		
		jlImage.setIcon(imageicon);
		//-------------------------------------------------------------------------------
		
		
		JPanel jp_North = new JPanel();
		jp_North.setBorder(new TitledBorder((new LineBorder(Color.black)),"책정보",TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,14) ));
		jp_North.setLayout(new GridLayout());
		jp_North.add(jlImage);
		
		JPanel jp_Center = new JPanel();

		jp_Center.setBorder(new TitledBorder((new LineBorder(Color.black)),"대여정보입력",TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,14) ));
		jp_Center.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 0;
		jp_Center.add(jlStudentID,gbc);
		jlStudentID.setFont(new Font("돋음",Font.BOLD,13));
		gbc.gridx = 1;	gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jtfStudentID,gbc);
		jtfStudentID.setFont(new Font("돋음",Font.BOLD,13));
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 1;
		jp_Center.add(jlClass,gbc);
		jlClass.setFont(new Font("돋음",Font.BOLD,13));
		gbc.gridx = 1;	gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jtfClass,gbc);
		jtfClass.setFont(new Font("돋음",Font.BOLD,13));
		jtfClass.setEditable(false);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 2;
		jp_Center.add(jlStudentName,gbc);
		jlStudentName.setFont(new Font("돋음",Font.BOLD,13));
		gbc.gridx = 1;	gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jtfStudentName,gbc);
		jtfStudentName.setFont(new Font("돋음",Font.BOLD,13));
		jtfStudentName.setEditable(false);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 3;
		jp_Center.add(jlStudentTel,gbc);
		jlStudentTel.setFont(new Font("돋음",Font.BOLD,13));
		gbc.gridx = 1;	gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jtfStudentTel,gbc);
		jtfStudentTel.setFont(new Font("돋음",Font.BOLD,13));
		jtfStudentTel.setEditable(false);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 4;
		jp_Center.add(jlStudentAge,gbc);
		jlStudentAge.setFont(new Font("돋음",Font.BOLD,13));
		gbc.gridx = 1;	gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jtfStudentAge,gbc);
		jtfStudentAge.setFont(new Font("돋음",Font.BOLD,13));
		jtfStudentAge.setEditable(false);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 5;
		jp_Center.add(jlStudentGender,gbc);
		jlStudentGender.setFont(new Font("돋음",Font.BOLD,13));
		gbc.gridx = 1;	gbc.gridy = 5;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jtfStudentGender,gbc);
		jtfStudentGender.setFont(new Font("돋음",Font.BOLD,13));
		jtfStudentGender.setEditable(false);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 6;
		jp_Center.add(jlCountID,gbc);
		jlCountID.setFont(new Font("돋음",Font.BOLD,13));
		gbc.gridx = 1;	gbc.gridy = 6;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jtfCountID,gbc);
		jtfCountID.setFont(new Font("돋음",Font.BOLD,13));
		jtfCountID.setEditable(false);
		
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;	gbc.gridy = 7;
		jp_Center.add(jbBorrowYes,gbc);
		jbBorrowYes.setFont(new Font("돋음",Font.BOLD,15));
		jbBorrowYes.setPreferredSize(new Dimension(150,30));
		
		gbc.gridx = 1;	gbc.gridy = 7;
		gbc.anchor = GridBagConstraints.EAST;
		jp_Center.add(jbBorrowNo,gbc);
		jbBorrowNo.setFont(new Font("돋음",Font.BOLD,15));
		jbBorrowNo.setPreferredSize(new Dimension(150,30));
		
		//-------------------------------------------------------이벤트
		
		jtfStudentID.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				studentIDSelect(borrowBookID);
				
			}
		});
		jbBorrowYes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String studentID = jtfStudentID.getText();
				
				borrowDate(studentID,borrowBookID);
				
			}
		});
		jbBorrowNo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				jf.dispose();
			}
		});
		
		jf.setLayout(new BorderLayout());
		jf.add(jp_North,BorderLayout.NORTH);
		jf.add(jp_Center,BorderLayout.CENTER);

		jf.setTitle("도서추가페이지");
		jf.setBounds(500,200,450,700);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
	}


	// 이미지 주소 불러오기
	String imageAddressSelect(int bookID) {
		String address = "";
		try {
			address = dao.bookImageSelect(bookID);

		} catch (Exception e) {
			System.out.println("이미지주소 불러오기 실패");
			e.printStackTrace();
		}
		return address;
	}

	void studentIDSelect(String borrowBookID) {
		int studentID = Integer.parseInt(jtfStudentID.getText());
		
		try {
			ArrayList list = dao.studentIDSelect(studentID,borrowBookID);
			if(((String)list.get(0)).equals("대여가능책없음")) {
				JOptionPane.showMessageDialog(null, "대여 가능한 책이 없습니다.");
			}else {
			jtfStudentID.setText((String)list.get(0));
			jtfClass.setText((String)list.get(1));
			jtfStudentName.setText((String)list.get(2));
			jtfStudentTel.setText((String)list.get(3));
			jtfStudentAge.setText((String)list.get(4));
			jtfStudentGender.setText((String)list.get(5));
			jtfCountID.setText((String)list.get(6));
			}
			
		} catch (Exception e) {
			System.out.println("학번검색 실패");
			e.printStackTrace();
		}
	}

	void borrowDate(String studentID,String borrowBookID) {
		try {
			dao.borrowDateInsert(studentID,borrowBookID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
