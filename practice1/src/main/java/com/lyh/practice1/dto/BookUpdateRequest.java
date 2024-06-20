package com.lyh.practice1.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateRequest {
	private String bookTitle;
	private String bookWriter;
	private String imgName;
	private MultipartFile NewImgName;
	private int imgNo;
	private int bookNo;
}
