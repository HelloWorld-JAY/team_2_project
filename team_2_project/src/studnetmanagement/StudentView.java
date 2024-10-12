package studnetmanagement;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class StudentView extends JPanel{

	JLabel			jlStudentID,		// 학번 라벨
	jlClassName,		// 학과 라벨
	jlStudentName,		// 학생이름 라벨
	jlGender,			// 성별 라벨
	jlAge,				// 나이 라벨
	jlAddres,			// 주소 라벨
	jlTel;				// 전화번호 라벨

	JTextArea		ta;

	JTextField		tfStudentID,		// 학번 입력
	tfClassName,		// 학과 입력
	tfStudentName,		// 이름 입력
	tfAge,				// 나이 입력
	tfAddres,			// 주소 이력
	tfSearch,			// 학색정보 검색창
	tfTel;				// 전화번호 입력

	JComboBox		comSearch,			// 학생정보 검색기준 선택
	cbGender;			// 성별선택

	JButton			bStudentInsert,		// 학생정보 등록 버튼
	bStudentModify,		// 학생정보 수정 버튼
	bStudentDelete,		// 학생정보 삭제 버튼
	bSelect;			// 학생정보 검색 버튼

	StudentTableModel model;

	// 다이얼로그
	JDialog			jDialog;

	// 테이블
	JTable			jTable;

	// 출력화면 컬럼명들 지정
	//String [] title = {"학번", "학과", "이름", "전화번호","성별", "나이", "주소"};

	// 모델단 변수 선언
	StudentDao	dao;

	public StudentView() {
		addLayout();		// 객체생성 및 화면구성
		eventProc();		// 이벤트처리 등록
		conectDB();			// DB연결
		selectByNumber();	// 학생목록 창띄우자마자 출력
	}

	void conectDB() {
		try {
			dao = new StudentDaoImpl();

		} catch (Exception e) {
			System.out.println("학생관리 드라이버 로딩 실패");
			e.printStackTrace();
		}
	}

	//이벤트 처리
	void eventProc() {
		// 검색창에서 엔터쳤을 때
		tfSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectByNumber();
			}
		});	

		tfStudentID.setEditable(false);
		// 검색버튼을 눌렀을 때
		bSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectByNumber();
			}
		});
		// 버튼 이벤트 등록
		ButtonEvent btnEvent = new ButtonEvent();

		bStudentInsert.addActionListener(btnEvent);
		bStudentModify.addActionListener(btnEvent);
		bStudentDelete.addActionListener(btnEvent);
		bSelect.addActionListener(btnEvent);

		// 검색한 열을 클릭했을때
		jTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {

				try {
					int row = jTable.getSelectedRow();
					int col = 0; 		// 검색한 열 클릭시 클릭한 열의 학번
					// Object -> Integer -> int 형변환
					int studentid = ((Integer)jTable.getValueAt(row, col)).intValue();

					StudentVO vo = dao.selectByNumber(studentid);

					// StudentVO의 각각의 값들을 화면에 출력
					tfStudentID.setText(vo.getStudentid()+"");
					tfClassName.setText(vo.getClassname());
					tfStudentName.setText(vo.getSname());
					tfTel.setText(vo.getTel());
					tfAge.setText(vo.getAge() + "" );
					cbGender.setSelectedItem(vo.getGender());
					tfAddres.setText(vo.getAddr());

				}catch(Exception ex) {
					ta.setText("검색실패" + ex.getMessage());
				}
			}
		});	
	} // end of eventProc

	// 삭제버튼을 눌렀을때 학생정보 삭제
	public void deleteStudent() {
		try {
			int result = JOptionPane.showConfirmDialog(bStudentDelete, "정말로 삭제하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				// 사용자가 "예"를 선택한 경우 정보 삭제
				JOptionPane.showMessageDialog(bStudentDelete, "삭제완료", "삭제완료", JOptionPane.INFORMATION_MESSAGE);
				dao.delete(Integer.parseInt(tfStudentID.getText()));
				// 사용자가 창을 닫은 경우
			} else if(result == JOptionPane.CLOSED_OPTION) {
				// 사용자가 "아니오"를 선택한 경우
			}else if (result == JOptionPane.NO_OPTION) {
			}
		}catch (Exception e) {
			JOptionPane.showMessageDialog(bStudentModify, "오류", "삭제실패", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}


	// 수정버튼을 눌렀을때 정보수정
	public void modifyStudent() {


		int		id		= Integer.parseInt(tfStudentID.getText());
		String	cname	= tfClassName.getText();
		String	sname	= tfStudentName.getText();
		String	gender	= (String)cbGender.getSelectedItem();
		String	tel		= tfTel.getText();
		int	age			= Integer.parseInt(tfAge.getText());
		String	addr	= tfAddres.getText();

		StudentVO vo = new StudentVO();
		vo.setStudentid(id);
		vo.setClassname(cname);
		vo.setSname(sname);
		vo.setTel(tel);
		vo.setGender(gender);
		vo.setAge(age);
		vo.setAddr(addr);

		try { // 정보를 수정할것인지 질문하기
			int result = JOptionPane.showConfirmDialog(bStudentModify, "정보를 수정하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
			// "예"를 선택했을경우
			if(result == JOptionPane.YES_OPTION) {
				int all = dao.modifystudent(vo);
				JOptionPane.showMessageDialog(bStudentModify, "수정완료", "수정완료", JOptionPane.INFORMATION_MESSAGE);
				// "아니오"를 선택했을경우
			}else if (result == JOptionPane.NO_OPTION) {
				// 창을 닫았을경우
			}else if (result == JOptionPane.CLOSED_OPTION) {
			}
		}catch (Exception e){
			JOptionPane.showMessageDialog(bStudentModify, "오류", "수정실패", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// 학생정보 검색
	public void selectByNumber() {

		int com = comSearch.getSelectedIndex();
		String sl = tfSearch.getText();

		try {
			ArrayList data = dao.selectstudent(com, sl);
			model.data = data;
			model.fireTableDataChanged();
			resizedColumm(jTable);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(bSelect, "오류", "검색실패", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();	
		}
	}

	// 학생정보 등록 클릭시 - 학생정보 등록
	public void insertStudent() {

		// 입력된값 불러와 변수에 저장
		String	cname	= tfClassName.getText();
		String	sname	= tfStudentName.getText();
		String	tel		= tfTel.getText();
		String	gender	= (String)cbGender.getSelectedItem();
		int		age		= Integer.parseInt(tfAge.getText());
		String	addr	= tfAddres.getText();

		// StudentVO 객체 생성 및 vo에다가 지정한 변수값들 대입
		StudentVO vo = new StudentVO();
		vo.setClassname(cname);
		vo.setSname(sname);
		vo.setGender(gender);
		vo.setAge(age);
		vo.setAddr(addr);
		vo.setTel(tel);
		
		try {
			int result = JOptionPane.showConfirmDialog(bStudentInsert, "정보를 등록하시겠습니까?", "확인", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				dao.insertStudent(vo);
				
				JOptionPane.showMessageDialog(bStudentInsert, "등록완료", "등록성공", JOptionPane.INFORMATION_MESSAGE);
				clear();
				}
		}catch (Exception e) {
			JOptionPane.showMessageDialog(bStudentInsert,"오류", "등록실패" , JOptionPane.ERROR_MESSAGE);
		}

	}

	// 화면 초기화
	public void clear() {
		tfStudentID.setText("");
		tfClassName.setText("");
		tfStudentName.setText("");
		tfTel.setText("");
		cbGender.setSelectedIndex(0);
		tfAge.setText("");
		tfAddres.setText("");
	}

	//버튼 이벤트 처리
	class ButtonEvent implements ActionListener{
		public void actionPerformed(ActionEvent ev) {
			Object ob =ev.getSource();

			if(ob == bStudentInsert) {
				insertStudent();				// 학생정보 등록
				selectByNumber();
			}
			else if (ob == bStudentModify) {
				modifyStudent();				// 학생정보 수정
				selectByNumber();
				clear();
			}
			else if (ob == bStudentDelete) {
				deleteStudent();				// 학생정보 삭제
				selectByNumber();				// 삭제확인위한 자동 검색
				clear();
			}
			else if (ob == bSelect) {
				selectByNumber();				// 학생정보 검색
			}
		}
	}

	// 테이블 컬럼크기 조절
	public void resizedColumm(JTable jtable) {
		final TableColumnModel colmodel = jtable.getColumnModel();

		for(int cl = 0; cl < jtable.getColumnCount(); cl++ ) {
			int width = 50; //min width

			// 헤더 크기 확인
			TableCellRenderer headrenderer = jtable.getTableHeader().getDefaultRenderer();
			Component headerComp = headrenderer.getTableCellRendererComponent(jtable,jtable.getColumnName(cl), false, false, 0, width);
			width = Math.max(headerComp.getPreferredSize().width +1, width);

			for (int row = 0; row < jtable.getRowCount(); row++) {
				TableCellRenderer renderer = jtable.getCellRenderer(row, cl);
				Component comp = jtable.prepareRenderer(renderer, row, cl);
				width = Math.max(comp.getPreferredSize().width +1, width);
			}
			colmodel.getColumn(cl).setPreferredWidth(width);
		}
	}

	// 화면설계
	public void addLayout() {

		// 멤버변수들의 객체생성
		tfStudentID 	= new JTextField(5);
		tfClassName		= new JTextField(5);
		tfStudentName	= new JTextField(5);
		tfTel 			= new JTextField(5);
		tfAge 			= new JTextField(5);
		tfAddres 		= new JTextField(14);

		// 텍스트 필드에서 중앙정렬
		tfStudentID.setHorizontalAlignment(JTextField.CENTER);
		tfClassName.setHorizontalAlignment(JTextField.CENTER);
		tfStudentName.setHorizontalAlignment(JTextField.CENTER);
		tfTel.setHorizontalAlignment(JTextField.CENTER);
		tfAge.setHorizontalAlignment(JTextField.CENTER);
		tfAddres.setHorizontalAlignment(JTextField.CENTER);

		// 텍스트 에어리어 객체생성
		ta				= new JTextArea();
		// 라벨들 객체생성
		jlStudentID		= new JLabel("학       번");
		jlClassName		= new JLabel("학       과");
		jlStudentName	= new JLabel("학생이름");
		jlTel			= new JLabel("전화번호");
		jlGender		= new JLabel("성       별");
		jlAge			= new JLabel("나       이");
		jlAddres		= new JLabel("주       소");

		// 글씨 크기 키우기
		Font font = new Font("돋음", Font.BOLD,15);

		// 버튼들 객체생성
		bStudentInsert = new JButton("학생정보 등록");
		bStudentInsert.setFont(font);	// 버튼내부의 글씨크기 키우기
		bStudentInsert.setPreferredSize(new Dimension(80, 80)); // 버튼 크기 설정

		bStudentModify = new JButton("학생정보 수정");
		bStudentModify.setFont(font);	// 버튼내부의 글씨크기 키우기
		bStudentModify.setPreferredSize(new Dimension(80, 80)); // 버튼 크기 설정

		bStudentDelete = new JButton("학생정보 삭제");
		bStudentDelete.setFont(font);	// 버튼내부의 글씨크기 키우기
		bStudentDelete.setPreferredSize(new Dimension(80, 100)); // 버튼 크기 설정

		bSelect = new JButton("검색");
		bSelect.setFont(font);			// 버튼내부의 글씨크기 키우기
		bSelect.setPreferredSize(new Dimension(80, 30)); 		 // 버튼 크기 설정

		// 검색칸 컬럼명 입력
		String [] cbCheck = {"학번", "전화번호", "이름"};
		comSearch	= new JComboBox<>(cbCheck);
		comSearch.setFont(font);
		comSearch.setRenderer(new BasicComboBoxRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬
				return label;
			}
		});
		tfSearch	= new JTextField(40);
		tfSearch.setPreferredSize(new Dimension(0,
				30));
		comSearch.setPreferredSize(new Dimension(130, 30));

		model		= new StudentTableModel();
		jTable		= new JTable(model);

		// 성별 선택
		String [] comGender = {"남자", "여자"};
		cbGender	= new JComboBox<>(comGender);
		cbGender.setFont(new Font("돋음", Font.BOLD, 23));
		cbGender.setRenderer(new BasicComboBoxRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬
				return label;
			}
		});


		//===========화면구성=========
		//왼쪽영역
		JPanel p_west = new JPanel();
		p_west.setLayout(new BorderLayout());

		// 왼쪽 위 영역
		JPanel p_west_west = new JPanel();
		p_west_west.setBorder(new TitledBorder((new LineBorder(Color.black)),"인적사항",
				TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,15) ));
		p_west_west.setLayout(new GridLayout(7, 2));
		p_west_west.add(jlStudentID);
		jlStudentID.setFont(new Font("돋음", Font.BOLD, 23));
		jlStudentID.setHorizontalAlignment(jlStudentID.CENTER);
		p_west_west.add(tfStudentID);
		tfStudentID.setFont(new Font("돋음", Font.PLAIN, 20));
		p_west_west.add(jlClassName);
		jlClassName.setFont(new Font("돋음", Font.BOLD, 23));
		jlClassName.setHorizontalAlignment(jlClassName.CENTER);
		p_west_west.add(tfClassName);
		tfClassName.setFont(new Font("돋음", Font.BOLD, 20));
		p_west_west.add(jlStudentName);
		jlStudentName.setFont(new Font("돋음", Font.BOLD, 23));
		jlStudentName.setHorizontalAlignment(jlStudentName.CENTER);
		p_west_west.add(tfStudentName);
		tfStudentName.setFont(new Font("돋음", Font.BOLD, 20));
		p_west_west.add(jlTel);
		jlTel.setFont( new Font("돋음", Font.BOLD, 23));
		jlTel.setHorizontalAlignment(jlStudentName.CENTER);
		p_west_west.add(tfTel);
		tfTel.setFont(new Font("돋음", Font.PLAIN, 20));
		p_west_west.add(jlGender);
		jlGender.setFont(new Font("돋음", Font.BOLD, 23));
		jlGender.setHorizontalAlignment(jlStudentName.CENTER);
		p_west_west.add(cbGender);
		p_west_west.add(jlAge);
		jlAge.setFont(new Font("돋음", Font.BOLD, 20));
		jlAge.setHorizontalAlignment(jlStudentName.CENTER);
		p_west_west.add(tfAge);
		tfAge.setFont(new Font("돋음", Font.PLAIN, 20));
		p_west_west.add(jlAddres);
		jlAddres.setFont(new Font("돋음", Font.BOLD, 20));
		jlAddres.setHorizontalAlignment(jlStudentName.CENTER);
		p_west_west.add(tfAddres);
		tfAddres.setFont(new Font("돋음", Font.BOLD, 20));

		// 왼쪽 아래 영역에 등록, 수정, 삭제 버튼 붙이기
		JPanel p_west_south = new JPanel();
		p_west_south.setLayout(new GridLayout(1, 3));
		p_west_south.add(bStudentInsert);
		p_west_south.add(bStudentModify);
		p_west_south.add(bStudentDelete);
		p_west.add(p_west_west, BorderLayout.WEST);
		p_west.add(p_west_south, BorderLayout.SOUTH);

		// 오른쪽 위쪽 영역
		JPanel p_east = new JPanel();
		p_east.setLayout(new BorderLayout());

		JPanel p_east_north = new JPanel();
		p_east_north.setBorder(new TitledBorder((new LineBorder(Color.black)),"학생정보 검색",
				TitledBorder.LEADING,TitledBorder.TOP,new Font("돋음",Font.BOLD,15) ));
		p_east_north.add(comSearch);
		p_east_north.add(tfSearch);
		p_east_north.add(bSelect);

		p_east.add(p_east_north, BorderLayout.NORTH);
		p_east.add(new JScrollPane(jTable), BorderLayout.CENTER);

		// 전체 붙이기
		setLayout(new GridLayout(2, 1));
		setLayout(new BorderLayout());
		add(p_west, BorderLayout.WEST);
		add(p_east, BorderLayout.EAST);

	}
}