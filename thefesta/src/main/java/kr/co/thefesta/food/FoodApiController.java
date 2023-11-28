package kr.co.thefesta.food;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;

import kr.co.thefesta.food.domain.ItemDTO;
import kr.co.thefesta.food.domain.ResultDTO;
import kr.co.thefesta.food.service.IFoodService;
import lombok.extern.log4j.Log4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/food")
@Log4j
public class FoodApiController {

	@Autowired
	private IFoodService service;
	

	@GetMapping("/apicall")
	public void apiCall(){
		

		log.info("api call success.........");
		
		// RestTemplate 인스턴스 생성
		RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
	    
	    // Gson 인스턴스 생성
	    Gson gson = new Gson();
	    
	    
		try {
			// baseURL
			String baseUrl = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1";

			// service Key
			String decodeServiceKey = "2MithscUucMQWsH4BixwE4Y39fJl+Te/9AJQrbYYBgw+VRrDAn5XNSBD/j5qb/YCu5YA5o7aSlkegFxvF71uBQ==";
			String decodeServiceKey1 = "sxM6XuFTCETUKfUia3EOsqJj2UPNmebC5DENhavYsj7KsSdf+tC6t+i+PtSLoiy+1mfRsDqOQo0+FibnTAKUqQ==";
			String decodeServiceKey2 = "FUZ9ccIzOa33akBnPSF3ULPUdvCk/Cfj7x8/1hXBaStY+b8nVOmDepdDMLhQBp5qMvBSzdGCv/mOFMFAFTKMhA==";
			String decodeServiceKey3 = "Pl6K060b4TyB9IpUXHlnOQONaTgkdSlRW8yGTnUDedutQ5Y915/K84w7UW4BOae8X7S8FSmZlXrbtQeeaT5Dsw==";
			String encodeServiceKey = URLEncoder.encode(decodeServiceKey, "UTF-8");

			// API 호출을 위한 파라미터 설정
			int pageNo = 1;
			int numOfRows = 100;

			// URI 및 파라미터 설정
			URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl)
					.queryParam("serviceKey", encodeServiceKey)
					.queryParam("pageNo", pageNo)
					.queryParam("numOfRows", numOfRows)
					.queryParam("MobileApp", "Thefesta")
					.queryParam("MobileOS", "ETC")
					.queryParam("contentTypeId", "39")
					.queryParam("arrange", "O")
					.queryParam("_type", "json")
					.build(true)
					.toUri();

//			log.info("uri address : " + uri);

			// HttpHeaders 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			// API 호출
			ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
//			log.info("API Response : " + response.getBody());

			// 호출한 값 ResultDto 객체에 담기
			ResultDTO resultDto = gson.fromJson(response.getBody(), ResultDTO.class);
//			log.info("resultDto : " + resultDto.toString());
			
			// 음식점 총 개수 구하기
//			int totalCount = resultDto.getResponse().getBody().getTotalCount();
			int totalCount = 1500;
			log.info("totalCount : " + totalCount);
			
			// pageNo 구하기
			int divNum = Math.floorDiv(totalCount, numOfRows);
			int modNum = Math.floorMod(totalCount, numOfRows);
			if (modNum == 0) {
				pageNo = divNum;
			} else {
				pageNo = divNum+1;
			}
			log.info(pageNo);
			
			for (int i = 1; i <= pageNo; i++) {
				
				URI uri1 = UriComponentsBuilder.fromHttpUrl(baseUrl)
						.queryParam("serviceKey", encodeServiceKey)
						.queryParam("pageNo", i)
						.queryParam("numOfRows", 100)
						.queryParam("MobileApp", "Thefesta")
						.queryParam("MobileOS", "ETC")
						.queryParam("contentTypeId", "39")
						.queryParam("arrange", "O")
						.queryParam("_type", "json")
						.build(true)
						.toUri();
				
				log.info("uri1 address : " + uri1);
				
				// HttpHeaders 설정
				HttpHeaders headers1 = new HttpHeaders();
				headers1.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

				// API 호출
				ResponseEntity<String> response1 = restTemplate.exchange(uri1, HttpMethod.GET, null, String.class);
				log.info("API Response1 : " + response1.getBody());

				// 호출한 값 ResultDto 객체에 담기
				ResultDTO resultDto1 = gson.fromJson(response1.getBody(), ResultDTO.class);
				log.info("resultDto1 : " + resultDto1.toString());
				
				List<ItemDTO> itemDto = resultDto1.getResponse().getBody().getItems().getItem();
				
				for (ItemDTO item : itemDto) {
					String contentid = item.getContentid();
					item.setContentid(contentid);
					log.info("contentid: " +contentid);
					
					String baseUrl2 = "http://apis.data.go.kr/B551011/KorService1/detailCommon1";
					String baseUrl3 = "http://apis.data.go.kr/B551011/KorService1/detailIntro1";
					
					URI uri2 = UriComponentsBuilder.fromHttpUrl(baseUrl2)
							.queryParam("serviceKey", encodeServiceKey)
							.queryParam("MobileApp", "Thefesta")
							.queryParam("MobileOS", "ETC")
							.queryParam("contentId", contentid)							
							.queryParam("defaultYN", "Y")							
							.queryParam("firstImageYN", "Y")							
							.queryParam("areacodeYN", "Y")							
							.queryParam("catcodeYN", "Y")							
							.queryParam("addrinfoYN", "Y")							
							.queryParam("mapinfoYN", "Y")							
							.queryParam("overviewYN", "Y")
							.queryParam("_type", "json")
							.build(true)
							.toUri();
					
					URI uri3 = UriComponentsBuilder.fromHttpUrl(baseUrl3)
							.queryParam("serviceKey", encodeServiceKey)
							.queryParam("MobileApp", "Thefesta")
							.queryParam("MobileOS", "ETC")
							.queryParam("contentId", contentid)
							.queryParam("contentTypeId", "39")
							.queryParam("_type", "json")
							.build(true)
							.toUri();
					
					log.info("uri2 address : " + uri2);
					log.info("uri3 address : " + uri3);
					
					
					// uri2 API 호출
					ResponseEntity<String> response2 = restTemplate.exchange(uri2, HttpMethod.GET, null, String.class);
					log.info("API Response2 : " + response2.getBody());
					
					// uri3 API 호출
					ResponseEntity<String> response3 = restTemplate.exchange(uri3,  HttpMethod.GET, null, String.class);
					log.info("API Response3 : " + response3.getBody());
					
					//uri2 json 값을 ResultDTO에 담기
					ResultDTO resultDto2 = gson.fromJson(response2.getBody().toString(), ResultDTO.class);
					log.info("resultDto2 : " + resultDto2.getResponse().getBody().getItems());
					
					// ResultDTO에서 값 가져오기
					String title = resultDto2.getResponse().getBody().getItems().getItem().get(0).getTitle();
					String addr1 = resultDto2.getResponse().getBody().getItems().getItem().get(0).getAddr1();
					String overview = resultDto2.getResponse().getBody().getItems().getItem().get(0).getOverview();
					String firstimage = resultDto2.getResponse().getBody().getItems().getItem().get(0).getFirstimage();
					String firstimage2 = resultDto2.getResponse().getBody().getItems().getItem().get(0).getFirstimage2();
					String mapx = resultDto2.getResponse().getBody().getItems().getItem().get(0).getMapx();
					String mapy = resultDto2.getResponse().getBody().getItems().getItem().get(0).getMapy();
					String areacode = resultDto2.getResponse().getBody().getItems().getItem().get(0).getAreacode();
					String sigungucode = resultDto2.getResponse().getBody().getItems().getItem().get(0).getSigungucode();
					String cat3 = resultDto2.getResponse().getBody().getItems().getItem().get(0).getCat3();
					
					// ItemDTO에 값 담기
					item.setTitle(title);
					item.setAddr1(addr1);
					item.setOverview(overview);
					item.setFirstimage(firstimage);
					item.setFirstimage2(firstimage2);
					item.setMapx(mapx);
					item.setMapy(mapy);
					item.setAreacode(areacode);
					item.setSigungucode(sigungucode);
					item.setCat3(cat3);
					
					if (response3.getBody().contains("\"items\": \"\"")) {
						item.setFirstmenu(null);
						item.setTreatmenu(null);
						item.setInfocenterfood(null);
						item.setParkingfood(null);
						item.setOpentimefood(null);
						item.setRestdatefood(null);
					} else {
						//uri3 json 값을 ResultDTO에 담기
						ResultDTO resultDto3 = gson.fromJson(response3.getBody(), ResultDTO.class);
						log.info("resultDto3 : " + resultDto3.getResponse().getBody().getItems());
						
						String firstmenu = resultDto3.getResponse().getBody().getItems().getItem().get(0).getFirstmenu();
						String treatmenu = resultDto3.getResponse().getBody().getItems().getItem().get(0).getTreatmenu();
						String infocenterfood = resultDto3.getResponse().getBody().getItems().getItem().get(0).getInfocenterfood();
						String parkingfood = resultDto3.getResponse().getBody().getItems().getItem().get(0).getParkingfood();
						String opentimefood = resultDto3.getResponse().getBody().getItems().getItem().get(0).getOpentimefood();
						String restdatefood = resultDto3.getResponse().getBody().getItems().getItem().get(0).getRestdatefood();
						
						item.setFirstmenu(firstmenu);
						item.setTreatmenu(treatmenu);
						item.setInfocenterfood(infocenterfood);
						item.setParkingfood(parkingfood);
						item.setOpentimefood(opentimefood);
						item.setRestdatefood(restdatefood);
						
					}
					
				}
				
				for (ItemDTO item : itemDto) {
					log.info("itemDto : " + item.toString());
					service.create(item);
				}
				log.info("DB 저장 완료");
//				saveDataAsync(itemDto);
				
			}
			

		} catch (UnsupportedEncodingException e) {
			log.error("Error encoding service key", e);
//			return "Error encoding service key";
		} catch (Exception e) {
			log.error("Error making API call", e);
//			return "Error making API call";
		} 
	}
	
//	@Async
//	@Transactional
//	public CompletableFuture<Void> saveDataAsync(List<ItemDTO> itemDtoList) {
//		for (ItemDTO item : itemDtoList) {
//			log.info("itemDto : " + item.toString());
//			try {
//				service.create(item);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		log.info("DB 저장 완료");
//		return CompletableFuture.completedFuture(null);
//	}
	
}
