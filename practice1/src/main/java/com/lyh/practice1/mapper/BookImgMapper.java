package com.lyh.practice1.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.lyh.practice1.dto.BookImg;

@Mapper
public interface BookImgMapper {
	int insert(BookImg newImage); // 도서 표지 등록
	int originalImgDelete(Map<String, Object> params); // 수정시에 원래사진 행 지우기
	int update(BookImg newImage); // 도서 표지 수정
}
