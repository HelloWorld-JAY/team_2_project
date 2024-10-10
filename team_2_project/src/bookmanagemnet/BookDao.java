package bookmanagemnet;

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
	public void borrowDateInsert(String studentID,String borrowBookID)throws Exception;
	// 학생 관리 연동
	public ArrayList studentIDSelect(int studentID,String borrowID)throws Exception;
	
}
