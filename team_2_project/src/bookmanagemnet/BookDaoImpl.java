package bookmanagemnet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.PseudoColumnUsage;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BookDaoImpl implements BookDao{

	private static final String DBDRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DBURL	 = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	private static final String DBUSER	 = "jinsang";
	private static final String DBPASS	 = "hwang";

	public BookDaoImpl()throws Exception {

		Class.forName(DBDRIVER);
	}

	// 전체 검색 메서드
	@Override
	public ArrayList<BookVO> bookSelectAll() throws Exception {

		ArrayList<BookVO> list = new ArrayList<BookVO>();

		Connection con = null;
		PreparedStatement ps = null;


		String sql = "SELECT * FROM K_BOOK b LEFT OUTER JOIN (SELECT count(BOOKID) count , sum(renting) renting, BOOKID Bookid2 FROM K_BOOK_COUNT GROUP BY BOOKID) c  ON b.BOOKID = c.Bookid2 ";



		try {
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);

			ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {
				BookVO vo = new BookVO();


				vo.setBookID(rs.getInt("BOOKID"));
				vo.setLargeCategory(rs.getString("LARGECATEGORY"));
				vo.setSmallCategory(rs.getString("SMALLCATEGORY"));
				vo.setBookName(rs.getString("BOOKNAME"));
				vo.setAuthor(rs.getString("AUTHOR"));
				vo.setPublisher(rs.getString("PUBLISHER"));
				vo.setLocation(rs.getString("LOCATION"));
				vo.setCount(rs.getInt("COUNT"));
				vo.setTotalrenting(rs.getInt("RENTING"));
				
				list.add(vo);
			}





		}finally {
			ps.close();
			con.close();
		}

		return list;
	}

	// 소뷴류 선택시 메서드
	@Override
	public ArrayList<BookVO> smallCategorySelect(String category) throws Exception {

		ArrayList<BookVO> list = new ArrayList<BookVO>();

		Connection con = null;
		PreparedStatement ps = null;

		String sql = "SELECT * FROM K_BOOK b LEFT OUTER JOIN (SELECT count(BOOKID) count, sum(renting) renting , BOOKID Bookid2 FROM K_BOOK_COUNT GROUP BY BOOKID) c  ON b.BOOKID = c.Bookid2 where SMALLCATEGORY = ?";

		try {
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);

			ps = con.prepareStatement(sql);
			ps.setString(1, category);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				BookVO vo = new BookVO();

				vo.setBookID		(rs.getInt("BOOKID"));
				vo.setLargeCategory	(rs.getString("LARGECATEGORY"));
				vo.setSmallCategory	(rs.getString("SMALLCATEGORY"));
				vo.setBookName		(rs.getString("BOOKNAME"));
				vo.setAuthor		(rs.getString("AUTHOR"));
				vo.setPublisher		(rs.getString("PUBLISHER"));
				vo.setLocation		(rs.getString("LOCATION"));
				vo.setCount			(rs.getInt("COUNT"));
				vo.setTotalrenting(rs.getInt("RENTING"));
				
				list.add(vo);
			}

		}finally {
			ps.close();
			con.close();
		}

		return list;
	}
	// 대분류 선택시 메서드
	@Override
	public ArrayList<BookVO> largeCategorySelect(String category) throws Exception {

		ArrayList<BookVO> list = new ArrayList<BookVO>();

		Connection con = null;
		PreparedStatement ps = null;

		String sql = "SELECT * FROM K_BOOK b LEFT OUTER JOIN (SELECT count(BOOKID) count, sum(renting) renting  , BOOKID Bookid2 FROM K_BOOK_COUNT GROUP BY BOOKID) c  ON b.BOOKID = c.Bookid2 where LARGECATEGORY = ?";

		try {
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);

			ps = con.prepareStatement(sql);
			ps.setString(1, category);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				BookVO vo = new BookVO();

				vo.setBookID		(rs.getInt("BOOKID"));
				vo.setLargeCategory	(rs.getString("LARGECATEGORY"));
				vo.setSmallCategory	(rs.getString("SMALLCATEGORY"));
				vo.setBookName		(rs.getString("BOOKNAME"));
				vo.setAuthor		(rs.getString("AUTHOR"));
				vo.setPublisher		(rs.getString("PUBLISHER"));
				vo.setLocation		(rs.getString("LOCATION"));
				vo.setCount			(rs.getInt("COUNT"));
				vo.setTotalrenting	(rs.getInt("RENTING"));
				
				list.add(vo);
			}

		}finally {
			ps.close();
			con.close();
		}

		return list;
	}
	// 검색 필드 액션 메서드
	@Override
	public ArrayList<BookVO> searchFieldAction(int category, String Search) throws Exception {

		String[] searchCateStr		= {"BOOKNAME","BOOKID","AUTHOR","PUBLISHER","LOCATION"};
		ArrayList<BookVO> list = new ArrayList<BookVO>();

		Connection con = null;
		PreparedStatement ps = null;

		String sql = "SELECT * FROM K_BOOK b LEFT OUTER JOIN (SELECT count(BOOKID) count, sum(renting) renting  , BOOKID Bookid2 FROM K_BOOK_COUNT GROUP BY BOOKID) c  ON b.BOOKID = c.Bookid2 WHERE "+ searchCateStr[category] + " LIKE '%" +Search+ "%'";

		try {
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);

			ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while(rs.next()) {

				BookVO vo = new BookVO();

				vo.setBookID		(rs.getInt("BOOKID"));
				vo.setLargeCategory	(rs.getString("LARGECATEGORY"));
				vo.setSmallCategory	(rs.getString("SMALLCATEGORY"));
				vo.setBookName		(rs.getString("BOOKNAME"));
				vo.setAuthor		(rs.getString("AUTHOR"));
				vo.setPublisher		(rs.getString("PUBLISHER"));
				vo.setLocation		(rs.getString("LOCATION"));
				vo.setCount			(rs.getInt("COUNT"));
				vo.setTotalrenting	(rs.getInt("RENTING"));
				
				list.add(vo);
			}

		}finally {
			ps.close();
			con.close();
		}

		return list;

	}
	// 추가 등록버튼 데이터베이스 메서드
	@Override
	public int addBookInsert(BookVO vo, int count) throws Exception {
		int	result			 = 0;
		Connection con		 = null;
		PreparedStatement bookPs = null;
		PreparedStatement countPs = null;


		String bookSql ="INSERT INTO k_book(BOOKID,LARGECATEGORY,SMALLCATEGORY,BOOKNAME,AUTHOR,PUBLISHER,LOCATION)"
				+ "	 VALUES(SEQ_BOOKID.NEXTVAL,?,?,?,?,?,?)";

		String countSql = "INSERT INTO k_book_count(bookid,countid,renting)"
				+ "		   VALUES(SEQ_BOOKID.CURRVAL,SEQ_BOOK_COUNT.NEXTVAL,1)";

		try {
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);

			bookPs	= con.prepareStatement(bookSql);

			bookPs.setString(1, vo.getLargeCategory());
			bookPs.setString(2, vo.getSmallCategory());
			bookPs.setString(3, vo.getBookName());
			bookPs.setString(4, vo.getAuthor());
			bookPs.setString(5, vo.getPublisher());
			bookPs.setString(6, vo.getLocation());

			bookPs.executeUpdate();

			countPs = con.prepareStatement(countSql);

			for(int i = 0; i < count; i++) {
				result = result + countPs.executeUpdate();
			}

		}finally {
			countPs.close();
			bookPs.close();
			con.close();
		}
		return result;
	}
	// 수정버튼 데이터베이스 메서드
	@Override
	public int bookUpdate(BookVO vo) throws Exception {
		int result = 0;

		Connection con		 = null;
		PreparedStatement bookPs = null;


		String bookSql ="UPDATE k_book SET LARGECATEGORY	= ?"
				+ "						   ,SMALLCATEGORY	= ?"
				+ "						   ,BOOKNAME		= ?"
				+ "						   ,AUTHOR			= ?"
				+ "						   ,PUBLISHER		= ?"
				+ "						   ,LOCATION		= ?"
				+ "		 WHERE BOOKID 						= ?";
	

		try {
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);

			bookPs	= con.prepareStatement(bookSql);

			bookPs.setString(1, vo.getLargeCategory());
			bookPs.setString(2, vo.getSmallCategory());
			bookPs.setString(3, vo.getBookName());
			bookPs.setString(4, vo.getAuthor());
			bookPs.setString(5, vo.getPublisher());
			bookPs.setString(6, vo.getLocation());
			bookPs.setInt	(7, vo.getBookID());
			

			result = bookPs.executeUpdate();

		}finally {
			bookPs.close();
			con.close();
		}
		return result;

	}

	// 삭제버튼 데이터베이스 메서드
	@Override
	public int bookDelete(int bookid) throws Exception {
		int result = 0;
		
		Connection con = null;
		PreparedStatement countPs = null;
		PreparedStatement bookPs = null;
		
		
		String countSql = "DELETE FROM k_book_count WHERE BOOKID = ?";
		
		String bookSql	= "DELETE FROM k_book WHERE BOOKID = ?";
		
		
		try {
		con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);
		
		countPs = con.prepareStatement(countSql);
		countPs.setInt(1, bookid);
		
		countPs.executeUpdate();

		
		bookPs = con.prepareStatement(bookSql);
		bookPs.setInt(1, bookid);
		

		result = bookPs.executeUpdate();
	
		}finally {
			countPs.close();
			bookPs.close();
			con.close();
		}
		
		return result;
	}

	// 이미지 주소 불러오는 메서드
	@Override
	public String bookImageSelect(int bookID) throws Exception {
		String address = "";
		Connection con = null;
		PreparedStatement ps = null;
		
		String sql = "SELECT image FROM k_book WHERE bookid = ?";
		
		try {
		con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);
		
		ps = con.prepareStatement(sql);
		ps.setInt(1, bookID);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			
			address = rs.getString("IMAGE");
		}
		
		}finally {
			ps.close();
			con.close();
		}
		
		return address;
	}

	
	// 학생관리 연동 메서드
	@Override
	public ArrayList studentIDSelect(int studentID,String borrowID) throws Exception {
		ArrayList list		 = new ArrayList();
		Connection con 		 = null;
		PreparedStatement ps = null;
		PreparedStatement countPs = null;
		
		
		String sql = "SELECT * FROM k_student WHERE studentid = ?";
		
		String countSql = "SELECT countid FROM k_book_count WHERE renting = 1 AND rownum = 1 AND bookid = ?";
		
		try {
		con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);
		
		ps = con.prepareStatement(sql);
		ps.setInt(1, studentID);
		
		ResultSet rs = ps.executeQuery();
		
		countPs = con.prepareStatement(countSql);
		System.out.println(borrowID);
		countPs.setInt(1, Integer.parseInt(borrowID));
		
		ResultSet rs2 = countPs.executeQuery();
		if(!rs2.next()) {
			
			 list.add("대여가능책없음");
			 
		}
		else if(rs.next()) {
			
			list.add(rs.getString("STUDENTID"+""));
			list.add(rs.getString("CLASSNAME"));
			list.add(rs.getString("SNAME"));
			list.add(rs.getString("TEL"));
			list.add(rs.getString("AGE"+""));
			list.add(rs.getString("GENDER"));
			list.add(rs2.getString("countid"+""));
			
		}
		
		}finally {
			ps.close();
			con.close();
		}
		
		return list;
	}

	@Override
	public void borrowDateInsert(String studentID, String borrowBookID) throws Exception {
		ArrayList list			 = new ArrayList();
		Connection con 			 = null;
		PreparedStatement ps	 = null;
		PreparedStatement countPS= null;
		
		String sql = "INSERT INTO k_bookmanagement(rentid,rentday,bookid,studentid)"
				+ "	  VALUES(SEQ_RENTID.nextval,sysdate,?,?)";
		
		String countSql = "UPDATE k_book_count SET RENTING = 0 WHERE bookid = (SELECT bookid FROM k_book_count WHERE renting = 1 AND rownum = 1)";
		
		try {
		con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);
		con.setAutoCommit(false);
		
		ps	= con.prepareStatement(sql);
		
		ps.setInt(1, Integer.parseInt(studentID));
		ps.setInt(2, Integer.parseInt(borrowBookID));
		
		int result = ps.executeUpdate();
		
		countPS = con.prepareStatement(countSql);
		
		
		}finally {
			ps.close();
			con.close();
		}
	}

}
