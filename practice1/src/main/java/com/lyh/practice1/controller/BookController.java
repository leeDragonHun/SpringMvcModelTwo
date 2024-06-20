package com.lyh.practice1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lyh.practice1.dto.Book;
import com.lyh.practice1.dto.BookRequest;
import com.lyh.practice1.dto.BookUpdateRequest;
import com.lyh.practice1.service.BookService;
import com.lyh.practice1.util.DebugingColor;

import lombok.extern.slf4j.Slf4j;

@Controller // 웹의 요청을 처리하고 매핑함
@Slf4j // 디버깅코드 사용가능하게
public class BookController {
	@Autowired // 의존성 주입(DI)시킨다. 여기서는 서비스를 쓰기 위함이다.
	BookService bookService; // 서비스의 인터페이스를 주입시킬것이다. bookService DI
	
	// 도서 목록
	// GET 요청을 처리하는데 사용된다. 즉, 클라가 웹 리소스를 요청할 때 사용되는 HTTP GET 메서드와 매핑된다.
	@GetMapping("/bookList") // 클라이언트가 아래의 메소드를 호출하기 위해 사용해야하는 URL의 일부라고 보면된다. 
	// 클라이언트가 URL에 괄호안의 내용을 똑같이 쓰면 컨트롤러단에서는 그 요청을 받아와 아래 메서드를 실행시키고
	// return에 해당하는 것을 반환한다.
	// @GetMapping의 메서드는 당연히 String타입으로 선언한다. return은 뷰의 이름을 나타내기 때문이다.
	public String bookList(
			// @RequestParam는 HTTP 요청의 쿼리 파라미터를 매개변수에 바인딩한다.
			// 파라미터가 필수인지 선택할 수 있다. 기본값은 required = true이다.
			// 파라미터가 전달되지 않았을 때의 기본값 설정 : defaultValue = "1"
			// 다른 변수를 집어 넣을수도 있음 : (@RequestParam(value = "nowPage") int currentPage
			@RequestParam(name = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(name = "rowPerPage", defaultValue = "3") int rowPerPage,
			Model m) { // <-- 'model 변수명' 은 뷰에게 데이터를 전달해준다.("키", "값") 이런 형태로.
		
		List<Book> bookList = bookService.getList(currentPage, rowPerPage); // List<>타입 변수에 서비스.도서목록출력 메서드를 대입.
		// log클래스의 debug메서드는 일반적으로 문자열을 전달받아 로그를 출력한다.
		// 그런데 내가 출력하려는 변수 bookList는 List<Book> 타입이여서 toString()을 이용해 문자열로 변환했다.
		log.debug(DebugingColor.hun + "[Controller] bookList : " + bookList.toString() + DebugingColor.reset); 
		
     	// 마지막 페이지 값 불러오기
		int lastPage = bookService.getLastPage(rowPerPage);
		log.debug(DebugingColor.hun + "[Controller] bookList lastPage : " + lastPage + DebugingColor.reset);
		
		// 현재 페이지 디버깅
		log.debug(DebugingColor.hun + "[Controller] bookList currentPage : " + currentPage + DebugingColor.reset);
		
		// Model을 이용하여 m이라는 Model에 list라는 이름으로 bookList라는 변수를 m에 대입.
		m.addAttribute("list", bookList); // * 학습목적으로 이름을 다르게 하였으나 같게 하는 것이 편할듯.
		m.addAttribute("lastPage", lastPage);
		m.addAttribute("currentPage", currentPage);
		
		// application.properties 에서 spring.mvc.view의 prefix(리턴 앞에 오는 것),
		// suffix(리턴 뒤에 오는 것)를 설정해놓았기 때문에 해당 경로의 파일이 리턴되고 
		// 클라이언트는 웹을 통해 해당 페이지로 접근하게 된다.
		// 현재 prefix로 WEB-INF/의 특정 폴더로 경로를 지정해놓았다.
		// WEB-INF폴더를 통해 클라이언트에게 정보가 노출이 되지 않게 한 것이다.
		// (클라이언트는 직접적으로 WEB-INF와 그 아래의 파일에 접근할 수 없다.)
		return "bookList";
	}
	
	// 도서 추가 페이지
	@GetMapping("/addBook")
	public String addBook() {
		return "addBook";
	}
	
	// 도서 추가 액션
	@PostMapping("/addBook")
	public String addBook(BookRequest bookRequest) {
		log.debug(DebugingColor.hun + "[Controller] addBook bookTitle : " + bookRequest.getBookTitle() + DebugingColor.reset);
		log.debug(DebugingColor.hun + "[Controller] addBook bookWriter : " + bookRequest.getBookWriter() + DebugingColor.reset);
		log.debug(DebugingColor.hun + "[Controller] addBook imgName : " + bookRequest.getImgName() + DebugingColor.reset);
		
		bookService.addBook(bookRequest);
		
		return "redirect:/bookList";
	}
	
	// 도서 한권 상세보기
	@GetMapping("/bookInfo")
	public String bookInfo(@RequestParam Map<String, Object> params, Model m) {
		log.debug(DebugingColor.hun + "[Controller] bookInfo bookNo : " + params.get("bookNo") + DebugingColor.reset);
		List<Map<String, Object>> list = bookService.selectOne(params); // 상세보기 Service
		
		// model에 list담기(bookInfo.jsp 에서 쓸것이다.)
		m.addAttribute("list", list);
		return "bookInfo";
	}
	
	// 도서 삭제
	@PostMapping("/bookDelete")
	public String bookDelete(BookUpdateRequest bur) {
		log.debug("bur : " + bur.toString());
		bookService.bookImgDelete(bur);
		// 도서(book) 삭제 Service
		// DELETE CASCADE에 의해 book테이블에서 도서 삭제시, book_img테이블에있는 데이터도 같이 삭제된다.
		// book테이블이 부모, bookImg가 자식.. 자식은 혼자삭제가능. 부모삭제되면 자식도 삭제되게 설계함. 
		bookService.bookDelete(bur);
		// 삭제 되고 나면 다시 도서목록으로
		return "redirect:/bookList";
	}
	
	// 도서 수정
	@GetMapping("/bookUpdate")
	public String bookUpdate(@RequestParam Map<String, Object> params, Model m) {
		log.debug(DebugingColor.hun + "[Controller] bookUpdate bookNo : " + params.get("bookNo") + DebugingColor.reset);
		List<Map<String, Object>> list = bookService.selectOne(params); // 상세보기 Service를 수정폼에 입힐것이다.
		
		m.addAttribute("list", list);
		return "bookUpdate";
	}
	
	// 도서 수정 액션
	// 1. 제목과 작가 수정
	// 2. 원래 사진은 삭제(기존 사진의 실제파일 삭제)
	// 3. 수정할 사진 -> 사진 UUID방식으로 변환 + 기존 사진행에 업데이트 + 실제파일 업로드
	// 나는 post와 getmapping 요청 URL을 다르게 했다. URL을 다르게 설정하여 요청의 의미를 명확하게 보려고.
	@PostMapping("/bookUpdateAction")
	public String bookUpdateAction(BookUpdateRequest bur) {
		log.debug(DebugingColor.hun + "[Controller] bookUpdateAction bur : " + bur.toString() + DebugingColor.reset);
		// 편하게 map타입 매개변수로 받아오곤했는데... map에 담긴 MultipartFile이 디버깅 되지 않음. 관련된 확인사항 기억나는 거 다 적용해보겠음.
		// 1. jsp에서 file이 포함된 form을 전송할 때는 form태그에 enctype="multipart/form-data"를 붙여줘야한다.
		// 2. 그리고 MultipartFile은 getOriginalFilename()으로 불러와야 파일이름을 볼 수 있다.
		// 3. application.properties에서 파일업로드 설정을 해야한다.
		// 4. 외부경로에 업로드 파일위치, 외부경로의 리소스 접근 하는 메서드 넣기(main메소드 바로 아래에 했던거)
		// MultipartFile객체를 Map<String, Object>에 저장할 수는 있다.
		// 하지만 @RequestParam Map<String, Object>로 파일 업로드를 처리할 때, Spring이 파일을 자동으로 Map에 넣어주지는 않는다.
		// 따라서 파일을 받기 위해서는 지금 이 컨트롤러 메서드에 매개변수() 안에 별도로 @RequestParam MultipartFile을 받아야 한다.
		// 이렇게 하면 매개변수 2개를 들고 다녀야 한다. 그래서 그냥 도서 수정전용 DTO를 하나 만들어서 쓰는 것으로 방법을 변경했다.
		
		// 1. 제목과 작가 수정
		bookService.bookUpdate(bur);
		// 2. 기존 사진의 실제파일 삭제
		bookService.bookImgDelete(bur);
		// 3. 수정할 사진 -> 사진 UUID방식으로 변환 + 기존 사진행에 업데이트 + 실제파일 업로드
		bookService.bookImgUpdate(bur);

		// 다시 도서상세보기 페이지로 이동.
		return "redirect:/bookInfo?bookNo="+bur.getBookNo();
	}
}
