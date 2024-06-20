package com.lyh.practice1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.lyh.practice1.dto.Book;
import com.lyh.practice1.dto.BookUpdateRequest;

@Mapper 
// 마이바티스에게 매퍼임을 알려줘서 .xml파일과 연결할 수 있게 해줌
// @Autowired를 사용하여 매퍼 인터페이스를 주입받을 수 있게 해줌
// MyBatis의 매퍼로 인식되어, MyBatis가 이 인터페이스의 구현체를 자동으로 생성함
public interface BookMapper {
	List<Book> selectList(Map<String, Integer> paramMap); // 도서 목록 출력, beginRow와 rowPerPage 이렇게 두개 값을 전달하기 위해 Map<String, Intger>타입의 매개변수
	int listCnt(); // 도서 목록 갯수
	int insert(Book book); // 도서 추가
	List<Map<String, Object>> selectOne(Map<String, Object> params); // 도서 한 권 상세보기
	int delete(BookUpdateRequest bur); // 도서 삭제(DELETE CASCADE에 의해서 지금 삭제할 도서의 img테이블에 있는 이미지 데이터도 삭제됨)
	int update(BookUpdateRequest bur); // 도서 제목과 작가만 수정 
}