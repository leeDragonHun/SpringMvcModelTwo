package com.lyh.practice1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // @Getter, @Setter, @RequiredArgsConstructor, @ToString, @EqualsAndHashCode 를 한번에 설정
@AllArgsConstructor // 필드의 값이 있는 생성자를 만들어준다
@NoArgsConstructor // 필드의 값이 없는 생성자를 만들어준다
// book테이블 DTO
public class Book {
	private int bookNo;
	private String bookTitle;
	private String bookWriter;
}
