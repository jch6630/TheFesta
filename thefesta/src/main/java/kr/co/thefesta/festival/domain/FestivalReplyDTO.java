package kr.co.thefesta.festival.domain;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class FestivalReplyDTO {
	private int frno;
	private String contentid;
	private String id;
	private String nickname;
	private String frcontent;
	private char frstatecode;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "GMT+9")
	private Date frregist;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm:ss", timezone = "GMT+9")
	private Date fredit;
}
