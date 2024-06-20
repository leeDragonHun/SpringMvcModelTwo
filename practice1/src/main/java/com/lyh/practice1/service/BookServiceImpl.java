package com.lyh.practice1.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.lyh.practice1.dto.Book;
import com.lyh.practice1.dto.BookImg;
import com.lyh.practice1.dto.BookRequest;
import com.lyh.practice1.dto.BookUpdateRequest;
import com.lyh.practice1.mapper.BookImgMapper;
import com.lyh.practice1.mapper.BookMapper;
import com.lyh.practice1.util.DebugingColor;

import lombok.extern.slf4j.Slf4j;

@Service // 스프링에게 해당 클래스가 Service Component이라고 알려 주는 것
@Transactional // 매서드 안에 로직이 다 처리 되지 않으면 롤백, 지금같이 클래스에 적용하면 내부 모든 메서드에 @Transactional이 적용된다.
@Slf4j // 디버깅 기능 사용
// 클래스 이름에 마우스를 올리자. 인터페이스에 있는데 여기에 구현되지 않은 메서드를 자동으로 추가 할 수 있다.
// implements는 extends와 다르게 인터페이스를 구현할 때 사용하고 인터페이스 내의 있는 모든 메서드를 구현해야한다.
public class BookServiceImpl implements BookService{  //서비스 인터페이스를 상속.
	@Autowired // 의존성 주입(DI)시킨다. 여기서는 매퍼의 쿼리결과를 쓰기 위함임
	BookMapper bookMapper; // di할 것을 정하고 사용할 변수명 정하기
	@Autowired
	BookImgMapper bookImgMapper;
	
	// 부모를 오버라이딩함. 
	// 메서드시그니처(메서드이름, 매개변수의 타입과 순서, 반환타입)가 틀릴경우 컴파일 오류발생시킴. 
	// 이를통해 부모클래스나 인터페이스를 올바르게 재정의 했는지 유효성검사를 해줌.
	// (재정의를 하지만 '메서드시그니처는 변하지 않았는가?' 검사한다는 의미임)
	// 도서 목록 출력
	@Override 
	public List<Book> getList(int currentPage, int rowPerPage) { 
		
		// 인터페이스 타입인 Map으로 객체를 직접 생성할 수 없다. 그래서 map 인터페이스를 구현하는 클래스인 HashMap으로 생성한다.
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("rowPerPage", rowPerPage);
		// 페이징 복습. 한페이지에 5개씩 보여줄거고 현재페이지가 3며이면 3-1=2, 2*5는 15, 15가 현재페이지인 3페이지에서 첫번째 줄에 올 데이터의 인덱스가 된다.
		paramMap.put("beginRow", (currentPage-1)*rowPerPage);
		
		 // 매퍼의 매서드인 selectList에다가 rowPerPage와 beginRow가 담긴 map을 매개변수로.
		List<Book> list = bookMapper.selectList(paramMap);
		log.debug(DebugingColor.hun + "[Service] list : " + list + DebugingColor.reset);
		return list;
	}

	// 마지막 페이지 구하기
	@Override
	public int getLastPage(int rowPerPage) {
		int listCnt = bookMapper.listCnt(); // 쿼리를 통해 구해진 전체갯수를 대입
		int lastPage = listCnt / rowPerPage; // 마지막 페이지는 = 전채갯수 / 페이지당갯수 (몫이 나온다)
		// listCnt % rowPerPage는 나머지가 나온다. 나머지가 0이 아닐때 즉, 딱 떨어지지 않는다면 윗줄에서 구한 lastPage에서 한 페이지가 더 나와야 한다.
		if(listCnt % rowPerPage != 0) { 
			lastPage += 1;
		}
		return lastPage;
	}
	
	// 도서 추가하기
	@Override
	public void addBook(BookRequest bookRequest) { // BookRequest 값넣기
		Book book = new Book(); // dto인 Book 생성
		book.setBookTitle(bookRequest.getBookTitle()); // book에다가 BookRequest에 있는 bookTitle 값 넣기
		book.setBookWriter(bookRequest.getBookWriter()); // book에다가 BookRequest에 있는 bookWriter 값 넣기
		
		// MyBatis에서 insert 메서드가 int 타입을 반환하는 이유는 삽입 작업의 결과로 영향받은 행(row)의 수를 반환하기 때문
		// 아래의 코드는 삽입 성공하면 1을 반환할 것이므로 성공시 row1 == 1 이 된다.
		int row1 = bookMapper.insert(book); // 매개변수 book은 윗줄에서 Book타입에 넣은 title, writer값을 가지고 있음.

		// 하나라도 실패하면 addBook매서드의 다른 실행결과도 취소되게 롤백시켜야한다.
		// @Transactional은 단순히 반환값만으로 롤백을 수행하는 것이 아니다.
		// 그래서 bookMapper가 어떤 경우에 insert를 성공해내지 못했다면 예외를 발생시켜서 @Transactional이 롤백할 수 있게 유도해야한다.
		if(row1 == 0) { // 명확하게 실패했다는 조건을 주기위해 != 1 보다는 == 0을 사용
			throw new RuntimeException();
		}
		// insert성공 했다면 AUTO_INCREMENT가 진짜 잘 되나? 가 제일 궁금하므로 로그찍어서 확인해보자.
		log.debug(DebugingColor.hun + "[Service] insertBookNo : " + book.getBookNo() + DebugingColor.reset);
		
		// MultipartFile은 스프링에서 파일 업로드를 처리하기 위한 인터페이스임
		// 이 인터페이스에 있는  getOriginalFilename()등을 써서 로직을 짤것임.
		MultipartFile mf = bookRequest.getImgName();
		
		// DB에 BookImg테이블대로 맞춘 dto에 필드에 값을 넣고, db에 저장되게 할 것임. 
		BookImg newImage = new BookImg();
		// imgNo는 AUTO_INCREMENT로 자동생성되며 어차피 목록 출력시에는 LEFT JOIN을 해서 bookNo가 같은 것을 불러오는 쿼리로 짰음. 
		// 아까 도서 insert하면서 AUTO_INCREMENT된 그 번호 bookNo에 set하기
		newImage.setBookNo(book.getBookNo());
		// 이제 BookImg dto에 필요한건 imgName뿐이다. 넣어주자. 첨부된 파일의 이름을 따주는 MultipartFile의 메서드를 이용했다
		newImage.setImgName(mf.getOriginalFilename());
		
		
		// 파일 이름이 저장될 때 랜덤한 UUID방식으로 저장되게 해줄 것이다.
		// UUID로 128비트 길이를 랜덤으로 만들어서 파일명의 중복을 피하고,
		// toString으로 생성된 UUID 를 문자열 형태로 반환한다. 8-4-4-4-12의 형태로 나오기에
		// "-"를 "" 으로 바꾼다. 즉 하이픈을 제거해버린다.
		String front = UUID.randomUUID().toString().replace("-", "");
		// 파일 이름을 "."부터 시작해서 자르기.
		int cutIndex = mf.getOriginalFilename().lastIndexOf(".");
		// 자른 이름을 대입
		String rear = mf.getOriginalFilename().substring(cutIndex);
		// 파일이 저장될 방식인 front+rear 를 사용
		newImage.setImgName(front+rear);
		
		// insert 시키고, 등록이 완료 되었으면 1을 반환할 것임.
		int row2 = bookImgMapper.insert(newImage);
		// 마찬가지로 실패시 @Transactional에 의해 롤백될 수 있게 해주기
		if(row2 == 0) {
			throw new RuntimeException();
		}
		
		
		// 파일 저장 로직
		// 위의 작업들이 DB에 사진을 저장한 게 아니고 사진의 이름이 저장시킨것이다.
		// 그렇기에 뷰에서 사진파일을 불러올 때 DB에 저장된 이름과 같은 사진을 불러오게 해야한다.
		// 사진을 프로젝트 내부에 저장하면 WAR로 보낼 때 초기화된다.
		// 즉 지금 하는 것이 파일을저장 + 이름 맞추기 작업이다.

		// 새로운 파일 객체를 생성하고 여기에 경로와 이름을 설정한다. 
		File realImageFile = new File("C:/upload/"+front+rear);
		try {
			// multipartFile을 지금은 비어있는 여기에 저장한다. 파일이 이미 있으므로 덮어쓴다. 
			mf.transferTo(realImageFile);
		} catch (Exception e) {
			// 실패하면 예외발생. 마찬가지로 @Transactional에 의해 롤백될 수 있게 해주기
			// e.printStackTrace()는 예외 정보를 콘솔에 출력헤준다.
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	// 도서 한권 상세보기
	@Override
	public List<Map<String, Object>> selectOne(Map<String, Object> params) {
		List<Map<String, Object>> list = bookMapper.selectOne(params);
		log.debug(DebugingColor.hun + "[Service] selectOne list : " + list + DebugingColor.reset);
		return list;
	}
	
	// 도서 삭제(DELETE CASCADE에 의해 bookImg테이블에 해당 데이터도 같이 삭제된다.)
	@Override
	public void bookDelete(BookUpdateRequest bur) {
		bookMapper.delete(bur);
	}
	
	// 도서 사진의 실제파일을 삭제(사진만 삭제 할 때도 있어서 따로 뺐음)
	@Override
	public void bookImgDelete(BookUpdateRequest bur) {
		// 상세보기 페이지에서 가져온 파일이름을 String타입으로 형변환하였다.
		// 이 이름인 파일을 삭제할 것이다.
		String delFileName = bur.getImgName();
		log.debug("삭제할 파일 이름 : "+delFileName);
		// 로그가 잘 찍힌다. (실제 파일 이름인 UUID.jpg와 같은형식으로 잘 찍힌다.)
		// 여기서부터는 실제 원본 파일 삭제하는 코드임
		// File이라는 '객체'에 저 경로+삭제할파일이름을 '생성'한다.
		File f = new File("c:/upload/"+delFileName);
		// exists()는 파일이나 경로(디렉토리)가 실제 있는지 확인해주고 있으면 true를 반환한다.
		if(f.exists()) {
			// 아까 File객체에 생성할 때 이름을 f라고 했다. 이 f를 지우겠다는 것이다. 
			// 참고로 삭제에 성공하면 true를 반환한다.
			f.delete();
			// boolean del = f.delete(); 이렇게 쓴다음 del이라는 변수명으로 조건문에 넣어 성공, 실패 분기문으로도 쓸 수도 있겠다.
		}
		
	}

	// 도서 한권 수정(제목과 작가만 수정)
	@Override
	public void bookUpdate(BookUpdateRequest bur) {
		bookMapper.update(bur);
	}

	@Override
	public void originalImgDelete(Map<String, Object> params) {
		bookImgMapper.originalImgDelete(params);
	}

	// 수정할 사진 -> 사진 UUID방식으로 변환 + 기존 사진행에 업데이트 + 실제파일 업로드
	@Override
	public void bookImgUpdate(BookUpdateRequest bur) {
		log.debug(DebugingColor.hun + "[Service] bookImgUpdate bur : " + bur.toString() + DebugingColor.reset);
		// MultipartFile은 스프링에서 파일 업로드를 처리하기 위한 인터페이스임
		// 이 인터페이스에 있는  getOriginalFilename()등을 써서 로직을 짤것임.
		MultipartFile mf = bur.getNewImgName();
		
		// DB에 BookImg테이블대로 맞춘 dto에 필드에 값을 넣고, db에 저장되게 할 것임. 
		BookImg newImage = new BookImg();
		// imgNo는 AUTO_INCREMENT로 자동생성되며 어차피 목록 출력시에는 LEFT JOIN을 해서 bookNo가 같은 것을 불러오는 쿼리로 짰음. 
		// 아까 도서 insert하면서 AUTO_INCREMENT된 그 번호 bookNo에 set하기
		newImage.setBookNo(bur.getBookNo());
		// 이제 BookImg dto에 필요한건 imgName뿐이다. 넣어주자. 첨부된 파일의 이름을 따주는 MultipartFile의 메서드를 이용했다
		newImage.setImgName(mf.getOriginalFilename());
		
		
		// 파일 이름이 저장될 때 랜덤한 UUID방식으로 저장되게 해줄 것이다.
		// UUID로 128비트 길이를 랜덤으로 만들어서 파일명의 중복을 피하고,
		// toString으로 생성된 UUID 를 문자열 형태로 반환한다. 8-4-4-4-12의 형태로 나오기에
		// "-"를 "" 으로 바꾼다. 즉 하이픈을 제거해버린다.
		String front = UUID.randomUUID().toString().replace("-", "");
		// 파일 이름을 "."부터 시작해서 자르기.
		int cutIndex = mf.getOriginalFilename().lastIndexOf(".");
		// 자른 이름을 대입
		String rear = mf.getOriginalFilename().substring(cutIndex);
		// 파일이 저장될 방식인 front+rear 를 사용
		newImage.setImgName(front+rear);
		
		// update 시키고, 완료 되었으면 1을 반환할 것임.
		int row = bookImgMapper.update(newImage);
		// 실패시 @Transactional에 의해 롤백될 수 있게 유도해주어야 함.
		if(row == 0) {
			throw new RuntimeException();
		}
		
		// 파일 저장 로직
		// 위의 작업들이 DB에 사진을 저장한 게 아니고 사진의 이름이 저장시킨것이다.
		// 그렇기에 뷰에서 사진파일을 불러올 때 DB에 저장된 이름과 같은 사진을 불러오게 해야한다.
		// 사진을 프로젝트 내부에 저장하면 WAR로 보낼 때 초기화된다.
		// 즉 지금 하는 것이 파일을저장 + 이름 맞추기 작업이다.

		// 새로운 파일 객체를 생성하고 여기에 경로와 이름을 설정한다. 
		File realImageFile = new File("C:/upload/"+front+rear);
		try {
			// multipartFile을 지금은 비어있는 여기에 저장한다. 파일이 이미 있으므로 덮어쓴다. 
			mf.transferTo(realImageFile);
		} catch (Exception e) {
			// 실패하면 예외발생. 마찬가지로 @Transactional에 의해 롤백될 수 있게 해주기
			// e.printStackTrace()는 예외 정보를 콘솔에 출력헤준다.
			e.printStackTrace();
			throw new RuntimeException();
		}		
		
	}
}
