package bookmanagement;

import java.util.ArrayList;

public interface BookDao {

	// 전체검색 메서드
	public ArrayList<BookVO> bookSelectAll() throws Exception;
	// 대분류 선택시 메서드
	public ArrayList<BookVO> largeCategorySelect(String category) throws Exception;
	// 소분류 선택시 메서드
	public ArrayList<BookVO> smallCategorySelect(String category) throws Exception;
	// 검색필드 엑션 메서드
	public ArrayList<BookVO> searchFieldAction(int category,String Search)throws Exception;
	
	// 데이터베이스 도서정보 추가
	public int addBookInsert(BookVO vo,int count)throws Exception;
	// 데이터베이스 도서정보 수정
	public int bookUpdate(BookVO vo)throws Exception;
	// 데이터베이스 도서정보 삭제
	public int bookDelete(int bookid)throws Exception;
	
	// 데이터베이스에서 이미지주소 불러오기
	public String bookImageSelect(int bookID)throws Exception;
	
	// 대여 메서드
	public ArrayList borrowDateInsert(int studentID,int borrowBookID)throws Exception;
	// 학생 관리 연동
	public ArrayList studentIDSelect(int studentID,String countID)throws Exception;
	// 한가지 책정보 가져오기
	public BookVO bookSelectOne(int Bookid)throws Exception;
	// 반납에서 책정보 가져오기
	public BookVO returnSelectOne(int countid) throws Exception;
	
	// 대여 정보 가져오는 메서드
	public ArrayList<ArrayList> rentSelectAll(int studentID)throws Exception;
	
	// 반납 날짜 + 미납기간 + 책 대여가능 수정 메서드
	public String returnRentUpIn(int countID,int rentid)throws Exception;
	
}
