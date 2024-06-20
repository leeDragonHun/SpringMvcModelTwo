package com.lyh.practice1.service;

import java.util.List;
import java.util.Map;

import com.lyh.practice1.dto.Book;
import com.lyh.practice1.dto.BookRequest;
import com.lyh.practice1.dto.BookUpdateRequest;

// 인터페이스는 일종의 계약이다(이거해라 저거해라)
// 서비스구현체가 이 인터페이스를 implements 해갈것이다.
// 추상적이다.
// 어차피 서비스 구현체내의 메서드는 맛깔나게 재정의를 할 것이다.
public interface BookService {
	List<Book> getList(int currentPage, int rowPerPage); // 도서 목록 출력, controller에서 currenPage, rowPerPage받아옴. 
	int getLastPage(int rowPerPage); // 마지막 페이지 구하기
	void addBook(BookRequest bookRequest); // 도서 등록
	List<Map<String, Object>> selectOne(Map<String, Object> params); // 도서 한권 상세보기
	void bookDelete(BookUpdateRequest bur); // 도서 삭제
	void bookImgDelete(BookUpdateRequest bur); // 도서 사진의 실제파일을 삭제(사진만 삭제 할 때도 있어서 따로 뺐음)
	void bookUpdate(BookUpdateRequest bur); // 도서 한권 수정(제목과 작가만)
	// 수정시에 행을 삭제해버리려고 했는데 그럼 기본키 넘버링이 달라지는 이슈가 있다. 삭제말고 수정할 사진의 uuid로 업데이트 해야겠다. 이서비스는 사용 X
	void originalImgDelete(Map<String, Object> params); // 수정시에 원래사진 행 지우기
	void bookImgUpdate(BookUpdateRequest bur); // 수정할 사진 -> 사진 UUID방식으로 변환 + 기존 사진행에 업데이트 + 실제파일 업로드
}
