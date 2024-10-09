package login;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import main.MainStart;

public class Login extends JPanel {

	JTextField		jtfID;
	JPasswordField	jpfPassword;
	JButton			jbAdmin, jbUser;

	JLabel			jlMain,jlID,jlPassword;

	MainStart mainStart;
	
	public Login(MainStart mainStart){
		this.mainStart = mainStart;
		
		addLayout();
		eventProc();
	}
	
	
	void eventProc() {
		// 관리자로그인 버튼 누를시
		jpfPassword.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				char[] passwordchar = jpfPassword.getPassword();
				String password	= new String(passwordchar);
				
				if(jtfID.getText().equals("admin") & password.equals("admin")) {
					mainStart.adminLoginTab();
				}else JOptionPane.showMessageDialog(null, "아이디/비밀번호가 잘못 입력되었습니다.");
			
			}
		});
		
		
		jbAdmin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				char[] passwordchar = jpfPassword.getPassword();
				String password	= new String(passwordchar);
				
				if(jtfID.getText().equals("admin") & password.equals("admin")) {
					mainStart.adminLoginTab();
				}else JOptionPane.showMessageDialog(null, "아이디/비밀번호가 잘못 입력되었습니다.");
			}
		});
		
		jbUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainStart.userLoginTab();
			}
		});
	}

	void addLayout() {

		jtfID		= new JTextField(14);
		jpfPassword	= new JPasswordField(14);

		jbAdmin		= new JButton("관리자 로그인");
		jbUser	= new JButton("일반사용자 로그인");

		jlMain		= new JLabel("로 그 인 페 이 지");
		jlID		= new JLabel("아   이   디              ");
		jlPassword	= new JLabel("비 밀 번 호              ");

		JPanel center_panel = new JPanel();

		center_panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();				// 그리드백 세부설정 메서드
		gbc.insets = new Insets(25, 10, 25, 10);						// 여백설정
		gbc.anchor = GridBagConstraints.CENTER;

		gbc.gridwidth = 2 ;
		gbc.gridx = 0 ; gbc.gridy = 0 ;
		center_panel.add(jlMain,gbc);
		jlMain.setFont(new Font("돋음",Font.BOLD,30));

		gbc.gridwidth = 1 ;
		gbc.gridx = 0 ; gbc.gridy = 1 ;
		center_panel.add(jlID,gbc);
		jlID.setFont(new Font("돋음",Font.BOLD,15));

		gbc.gridx = 1 ; gbc.gridy = 1 ;
		center_panel.add(jtfID,gbc);

		gbc.gridx = 0 ; gbc.gridy = 2 ;
		center_panel.add(jlPassword,gbc);
		jlPassword.setFont(new Font("돋음",Font.BOLD,15));


		gbc.gridx = 1 ; gbc.gridy = 2 ;
		center_panel.add(jpfPassword,gbc);


		gbc.gridx = 0 ; gbc.gridy = 3 ;
		center_panel.add(jbAdmin,gbc);
		jbAdmin.setPreferredSize(new Dimension(180,30));
		jbAdmin.setFont(new Font("돋음",Font.BOLD,15));

		gbc.gridx = 1 ; gbc.gridy = 3 ;
		center_panel.add(jbUser,gbc);
		jbUser.setPreferredSize(new Dimension(180,30));
		jbUser.setFont(new Font("돋음",Font.BOLD,15));

		setLayout(new BorderLayout());
		add(center_panel,BorderLayout.CENTER);

	}


}
