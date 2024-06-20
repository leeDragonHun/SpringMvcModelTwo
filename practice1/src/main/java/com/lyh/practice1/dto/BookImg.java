package com.lyh.practice1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookImg {
	private int imgNo;
	private int bookNo;
	private String imgName;
}
