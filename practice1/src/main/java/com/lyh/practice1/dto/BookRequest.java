package com.lyh.practice1.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// 도서 등록시 필요한 dto
public class BookRequest {
	private String bookTitle;
	private String bookWriter;
	private MultipartFile imgName;
}
