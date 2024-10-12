package bookmanagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
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

public class BookView extends JPanel{

	JComboBox<String>	largeCate,smallCate,searchCate;	// 대분류, 소분류, 검색분류

	JTextField	jtfSearch;								// 검색 텍스트필드

	JButton		bBorrow,breturn;						// 대여, 반납 버튼

	String [] header = {"책번호","대분류","소분류","도서명","저자","츌판사","총수량","잔여수량","도서위치"};	// 테이블 헤더 셋팅

	DefaultTableModel model;																// 디폴트 테이블 모델 지정

	JTable table;																			// J테이블 선언

	JDialog jdialog;
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

	// 생성자함수
	public BookView(){

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

					borrowLayout(address,borrowBookID);// 창 띄우고 책정보 입력 및 대여 학번 받아서 대여기간 창 띄우기

				}

			}
		});
		// 반납버튼 이벤트
		breturn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String studentID = enterStudentIDUnique();// 학번입력 메서드 

				if(studentID.equals("")) {}				
				else {
					returnBook(studentID);// 해당 학번이 빌린 책 테이블화 해서 뜨게 만드는 메서드창

				}

				// 반납이 완료되었습니다 창띄우기.(연체료 계산까지)

			}
		});
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev){
				if(ev.getClickCount()>1) {

					int row = table.getSelectedRow();
					int bookID  = Integer.parseInt((String)table.getValueAt(row, 0));
					String address = imageAddressSelect(bookID);
					try {
						BookVO vo = dao.bookSelectOne(bookID);

						jdialog = new JDialog();
						((JComponent) jdialog.getContentPane()).setBorder(new TitledBorder((new LineBorder(Color.black)),"도서정보",TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,18) ));
						jdialog.setLayout(new GridBagLayout());

						jlImage			 = new JLabel(" ",JLabel.CENTER);
						icon			 = new ImageIcon(address);
						image			 = icon.getImage();
						scaleimage 		 = image.getScaledInstance(270, 340, image.SCALE_SMOOTH);
						imageicon 		 = new ImageIcon(scaleimage);

						jlImage.setIcon(imageicon);

						jdialog.setTitle("도서정보창");
						jdialog.setModal(true);

						GridBagConstraints gbc = new GridBagConstraints();
						gbc.insets = new Insets(5, 10, 20, 10);

						gbc.gridwidth = 2;
						gbc.gridx = 0; 	gbc.gridy = 0;
						jdialog.add(jlImage,gbc);

						gbc.gridwidth = 1;
						gbc.gridx = 0; 	gbc.gridy = 1;
						jdialog.add(new JLabel("도서명: ") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
						gbc.gridx = 1; 	gbc.gridy = 1;
						jdialog.add(new JLabel(vo.getBookName()){{setFont(new Font("돋음",Font.BOLD,15));}},gbc);

						gbc.gridx = 0; 	gbc.gridy = 2;
						jdialog.add(new JLabel("저자명: ") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
						gbc.gridx = 1; 	gbc.gridy = 2;
						jdialog.add(new JLabel(vo.getAuthor()){{setFont(new Font("돋음",Font.BOLD,15));}},gbc);

						gbc.gridx = 0; 	gbc.gridy = 3;
						jdialog.add(new JLabel("출판사: ") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
						gbc.gridx = 1; 	gbc.gridy = 3;
						jdialog.add(new JLabel(vo.getPublisher()){{setFont(new Font("돋음",Font.BOLD,15));}},gbc);

						jdialog.setBounds(450,200,380,580);
						jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						jdialog.setVisible(true);




					} catch (Exception e) {
						e.printStackTrace();
					} //책정보 가져오기
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
			model.setNumRows(0);
			for(BookVO vo : list) {
				model.addRow(vo.toStringList());
			}
			model.fireTableDataChanged();
		} catch (Exception e) {
			System.out.println("전체불러오기 오류");
			e.printStackTrace();
		}

	}

	// 학번 입력하는 창 띄우고 고유번호 리턴하는 메서드
	String enterStudentIDUnique() {

		JPanel panel = new JPanel(); 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel jl = new JLabel("학번을 입력해주세요.");
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField jtf = new JTextField(11);
		jtf.setHorizontalAlignment(JTextField.CENTER);

		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(jl,gbc);
		gbc.gridy = 1;
		panel.add(jtf,gbc);


		int result = JOptionPane.showOptionDialog(null, panel, "학번입력",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				new Object[]{"확인","취소"}, null);

		if(result == JOptionPane.OK_OPTION) {
			return jtf.getText();
		}else {
			return "";
		}

	}
	// 책고유번호 입력하는 창 띄우고 고유번호 리턴하는 메서드
	String enterBookUnique() {

		JPanel panel = new JPanel(); 
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel jl = new JLabel("책번호를 입력해주세요.");
		jl.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField jtf = new JTextField(11);
		jtf.setHorizontalAlignment(JTextField.CENTER);

		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(jl,gbc);
		gbc.gridy = 1;
		panel.add(jtf,gbc);

		int row = table.getSelectedRow();
		if(row != -1) {
			jtf.setText((String)table.getValueAt(row, 0));
		}

		int result = JOptionPane.showOptionDialog(null, panel, "책번호 입력",
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

				int studentID =Integer.parseInt(jtfStudentID.getText());
				int countID	 = Integer.parseInt(jtfCountID.getText());

				borrowDate(studentID,countID);
				jf.dispose();
				jtfStudentID.setText("");
				jtfStudentName.setText("");
				jtfStudentTel.setText("");
				jtfStudentGender.setText("");
				jtfStudentAge.setText("");
				jtfCountID.setText("");
				jtfClass.setText("");
				showAllItem();
			}
		});
		jbBorrowNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				jf.dispose();
			}
		});
		//-------------------------------------------------------------
		jf.setLayout(new BorderLayout());
		jf.add(jp_North,BorderLayout.NORTH);
		jf.add(jp_Center,BorderLayout.CENTER);

		jf.setTitle("도서대여");
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
	// 학생 id 조회
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
	// 대여버튼 데이터베이스 적용
	void borrowDate(int studentID,int countID) {
		try {
			ArrayList list = dao.borrowDateInsert(studentID,countID);

			JOptionPane.showMessageDialog(null, "대여번호:" + (String)list.get(0)+"\n" + "대여기간:" + list.get(1)+" ~ "+list.get(2)+" 입니다.");	
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	// 반납 학번입력후 뜨는 창 메서드
	void returnBook(String studentID) {

		jf 				 		= new JFrame();
		String[] returnHeader	= {"대여번호","책고유번호","책이름","대여일","반납예정일","미납여부"};
		DefaultTableModel model2					= new DefaultTableModel(returnHeader,0)
		{
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		JTable table2					= new JTable(model2);

		JButton jbReturnYes = new JButton(" 반    납 ") {{setFont(new Font("돋음",Font.BOLD,15)); setPreferredSize(new Dimension(150,30));}};
		JButton jbReturnNo	= new JButton(" 취    소 ") {{setFont(new Font("돋음",Font.BOLD,15)); setPreferredSize(new Dimension(150,30));}};

		jf.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);

		gbc.gridx = 0; gbc.gridy = 0;
		jf.add(new JLabel( "학번 '"+ studentID +"' 님의 도서대여 현황") {{setFont(new Font("돋음", Font.BOLD, 15));}},gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 0; gbc.gridy = 1;
		jf.add(new JScrollPane(table2) {{setPreferredSize(new Dimension(670,350));}},gbc);
		ArrayList<ArrayList> list = rentSelectAll(Integer.parseInt(studentID));
		model2.setNumRows(0);
		for(ArrayList temp : list) {
			model2.addRow(temp.toArray());
		}
		model2.fireTableDataChanged();
		resizeColumnWidth(table2);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0; gbc.gridy = 2;
		jf.add(jbReturnYes,gbc);
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridx = 1; gbc.gridy = 2;
		jf.add(jbReturnNo,gbc);
		//==================================================================================이벤트
		// 반납버튼
		jbReturnYes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {


				int row = table2.getSelectedRow();
				if(row == -1 ) {
					JOptionPane.showMessageDialog(null, "반납할 도서를 선택해주세요");
				}
				int countID =  Integer.parseInt(table2.getValueAt(row, 1).toString());
				
				
				
				try {
					BookVO vo = dao.returnSelectOne(countID);
					String address = imageAddressSelect(vo.getBookID());
					
					jdialog = new JDialog();
					((JComponent) jdialog.getContentPane()).setBorder(new TitledBorder((new LineBorder(Color.black)),"반납도서정보",TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,18) ));
					jdialog.setLayout(new GridBagLayout());

					jlImage			 = new JLabel(" ",JLabel.CENTER);
					icon			 = new ImageIcon(address);
					image			 = icon.getImage();
					scaleimage 		 = image.getScaledInstance(270, 340, image.SCALE_SMOOTH);
					imageicon 		 = new ImageIcon(scaleimage);
					JButton jbReturnYes2		 = new JButton(" 확   인 "){{setFont(new Font("돋음",Font.BOLD,15));}};
					JButton jbReturnNo2			 = new JButton(" 취   소 "){{setFont(new Font("돋음",Font.BOLD,15));}};
					
					jlImage.setIcon(imageicon);

					jdialog.setTitle("반납도서정보");
					jdialog.setModal(true);

					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(5, 0, 20, 0);

					gbc.gridwidth = 2;
					gbc.gridx = 0; 	gbc.gridy = 0;
					jdialog.add(jlImage,gbc);

					gbc.gridwidth = 1;
					gbc.gridx = 0; 	gbc.gridy =1;
					gbc.anchor = GridBagConstraints.WEST;
					jdialog.add(new JLabel("책고유번호: ") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
					gbc.gridx = 1; 	gbc.gridy =1;
					gbc.anchor = GridBagConstraints.EAST;
					jdialog.add(new JLabel(countID+""){{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
					
					gbc.gridx = 0; 	gbc.gridy =2;
					gbc.anchor = GridBagConstraints.WEST;
					jdialog.add(new JLabel("도서명: ") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
					gbc.gridx = 1; 	gbc.gridy =2;
					gbc.anchor = GridBagConstraints.EAST;
					jdialog.add(new JLabel(vo.getBookName()){{setFont(new Font("돋음",Font.BOLD,15));}},gbc);

					gbc.gridx = 0; 	gbc.gridy =3;
					gbc.anchor = GridBagConstraints.WEST;
					jdialog.add(new JLabel("저자명: ") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
					gbc.gridx = 1; 	gbc.gridy =3;
					gbc.anchor = GridBagConstraints.EAST;
					jdialog.add(new JLabel(vo.getAuthor()){{setFont(new Font("돋음",Font.BOLD,15));}},gbc);

					gbc.gridx = 0; 	gbc.gridy =4;
					gbc.anchor = GridBagConstraints.WEST;
					jdialog.add(new JLabel("출판사: ") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
					gbc.gridx = 1; 	gbc.gridy =4;
					gbc.anchor = GridBagConstraints.EAST;
					jdialog.add(new JLabel(vo.getPublisher()){{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
					
					gbc.gridwidth = 2;
					gbc.gridx = 0; 	gbc.gridy =5;
					gbc.anchor = GridBagConstraints.CENTER;
					jdialog.add(new JLabel("해당 도서를 반납하시겠습니까 ?") {{setFont(new Font("돋음",Font.BOLD,15));}},gbc);
					
					gbc.gridwidth = 1;
					gbc.gridx = 0; 	gbc.gridy =6;
					gbc.anchor = GridBagConstraints.WEST;
					jdialog.add(jbReturnYes2,gbc);
					gbc.gridx = 1; 	gbc.gridy =6;
					gbc.anchor = GridBagConstraints.EAST;
					jdialog.add(jbReturnNo2,gbc);
					
					
					//=================================================================== 이벤트
					
					jbReturnYes2.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							int row = table2.getSelectedRow();
							int rentid = Integer.parseInt(table2.getValueAt(row, 0).toString());
							try {
								String late = dao.returnRentUpIn(countID,rentid);
								if(late.equals("정상")) {
									JOptionPane.showMessageDialog(null, "반납이 완료되었습니다.");
									jdialog.dispose();
									ArrayList<ArrayList> list = rentSelectAll(Integer.parseInt(studentID));
									model2.setNumRows(0);
									for(ArrayList temp : list) {
										model2.addRow(temp.toArray());
										
									}
									model2.fireTableDataChanged();
									showAllItem();
								}else {
									JOptionPane.showMessageDialog(null, "미납기간은 " + late + "일 입니다");
									jdialog.dispose();
									ArrayList<ArrayList> list = rentSelectAll(Integer.parseInt(studentID));
									model2.setNumRows(0);
									for(ArrayList temp : list) {
										model2.addRow(temp.toArray());
									}
									model2.fireTableDataChanged();
									showAllItem();
								}
								
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}
						
					});
					
					jbReturnNo2.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							
							jdialog.dispose();
							
						}
					});
					
					//========================================================================
					
					jdialog.setBounds(650,200,380,700);
					jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					jdialog.setVisible(true);




				} catch (Exception ex) {
					ex.printStackTrace();

				}
			
			}
			
		});

		//취소버튼
		jbReturnNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jf.dispose();

			}
		});


		//======================================================================================


		jf.setTitle("도서반납");
		jf.setBounds(500,200,740,500);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
		
	}

	// 대여 여부 검색
	ArrayList<ArrayList> rentSelectAll(int studentID) {
		ArrayList<ArrayList> list = new ArrayList<ArrayList>();
		try {
			list = dao.rentSelectAll(studentID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
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
