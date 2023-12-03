package kr.co.thefesta.food.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class LikeDTO {

	private String contentid;
	private String title;
	private String cat3;
	private String id;
	
}
