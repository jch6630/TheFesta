package kr.co.thefesta.admin;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.co.thefesta.admin.domain.AdminDTO;
import kr.co.thefesta.admin.domain.Criteria;
import kr.co.thefesta.admin.domain.PageDTO;
import kr.co.thefesta.admin.domain.QuestionDTO;
import kr.co.thefesta.admin.domain.ReportDTO;
import kr.co.thefesta.admin.service.IAdminService;
import kr.co.thefesta.board.domain.BoardDTO;
import kr.co.thefesta.festival.service.IFestivalService;
import kr.co.thefesta.member.domain.MemberDTO;
import kr.co.thefesta.member.service.IMemberService;
import lombok.extern.log4j.Log4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
@Log4j
public class AdminController {
	@Autowired
	public IAdminService service;
	
	@Autowired
	private IFestivalService festivalService;
	
	@Autowired
	private IMemberService memberService;
	
	
	//member 테이블 회원정보 list
	@RequestMapping(value = "/memberList", method = RequestMethod.GET)
	public Map<String, Object> memberGet(Criteria cri)throws Exception{
		log.info("memberlist Get....");
		log.info("cri = " + cri.toString());
		Map<String, Object> result = new HashMap<>();
		
		List<AdminDTO> memberList = service.memberList(cri);
		
		
		result.put("list", memberList);
		int total = service.memberCnt();
		 
	     log.info("total : " + total);
	     
	     result.put("pageMaker", new PageDTO(cri, total));
	     log.info("result" + result.toString());
	     
	     
		return result;
	}
	
	//member 테이블 회원총원
		@RequestMapping(value = "/memberListCnt", method = RequestMethod.GET)
		public int memberListCnt()throws Exception{
			log.info("memberListCnt Get....");
			int total = service.memberCnt();
			log.info("total" + total);
			
			return total;
		}
	
	
	//회원 상세페이지(신고 list 페이지)
	@RequestMapping(value = "/memberDetail", method = RequestMethod.GET)
	public Map<String, Object> memberDetailGet(String id, Criteria cri)throws Exception{
		log.info("memberDetail Get...");
		log.info("회원 상세 id값 = " + id);
		log.info("회원 상세 cri값 = " + cri.toString());
		Map<String, Object> result = new HashMap<>();
		List<ReportDTO> reportList = service.memberDetail(id, cri);
		
		
		result.put("list", reportList);
		int total = service.memberReportCnt(id);
		
		
		 
	    log.info("total : " + total);
	    
	     
	    result.put("pageMaker", new PageDTO(cri, total));
	    
		log.info("reportList = " + reportList.toString());
		return result;
	}
	
	//회원 상세페이지 갯수(신고 list 페이지)
	@RequestMapping(value = "/memberDetailCnt", method = RequestMethod.GET)
	public int memberDetailCnt(String id)throws Exception{
		log.info("memberDetailCnt Get....");
		log.info("받은 id값 "+ id);
		int total = service.memberReportCnt(id);
		log.info("total" + total);
		
		return total;
	}
	
	//회원 닉네임 표시
	@RequestMapping(value = "/memberNickName", method = RequestMethod.GET)
	public MemberDTO MemberDTO(String id)throws Exception{
		log.info("memberNickName get...");
		log.info("받은 값 id = " + id);
		MemberDTO memberDto = memberService.selMember(id);
		log.info("memberDto" + memberDto.toString());
		
		return memberDto;
	}
	
	//신고내용 표시
	@RequestMapping(value = "/memberReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String memberReportGet(Integer reportid)throws Exception {
		log.info("memberReport Get...");
		String reportcontent = service.memberReport(reportid);
		log.info("신고내용 = " + reportcontent);
		
		return reportcontent;
	}
	
	//신고내용 삭제
	@RequestMapping(value = "/memberReportDelete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int memberReportDelete(Integer reportid)throws Exception {
		log.info("memberReportDelete post...");
		log.info("react에서 받은 값 : " + reportid);
		
		service.memberReportDelete(reportid);
		
		
		return reportid;
	}
	
	//신고 list
	@RequestMapping(value = "/reportList", method = RequestMethod.GET)
	public Map<String, Object> reportListGet(Criteria cri)throws Exception{
		
		log.info("reportList Get...");
		List<ReportDTO> reportList = service.reportList(cri);
		log.info("reportList = " + reportList.toString());
		log.info("cri = " + cri);
		Map<String, Object> result = new HashMap<>();
		
		//ReportDTO rbno,rfrno,rbid를 reportnumber(화면표시글자로) 변경 
		for (ReportDTO reportDTO : reportList) {
			if(reportDTO.getRbrno() > 0) {
				reportDTO.setReportnumber("게시글 댓글 코드");
			}else if(reportDTO.getRfrno() > 0) {
				reportDTO.setReportnumber("축제 댓글 코드");
			}else if(reportDTO.getRbid() > 0){
				reportDTO.setReportnumber("게시글 코드");
			}else {
				reportDTO.setReportnumber("해당 글은 삭제되었습니다.(회원탈퇴)");// 회원(reported)이 탈퇴한 경우
			}
		}
		
		result.put("list", reportList);
		int total = service.reportListCnt();
		 
	    log.info("total : " + total);
	     
	    result.put("pageMaker", new PageDTO(cri, total));
		
		return result;
	}
	
	//신고list 갯수
	@RequestMapping(value = "/reportListCnt", method = RequestMethod.GET)
	public int reportListCnt()throws Exception{
		log.info("reportListCnt Get....");
		int total = service.reportListCnt();
		log.info("total" + total);
		
		return total;
	}
	
	//신고처리상태 변경(reportstate->2)
	@RequestMapping(value = "/reportstateChange", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int reportstateChange(Integer reportid)throws Exception{
		log.info("reportstateChange post...");
		service.reportstateChange(reportid);
		
		return reportid;
	}
	
	//회원 신고누적 count
	@RequestMapping(value = "/memberReportnumCnt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int reportstateChange(String id, Integer reportid)throws Exception{
		log.info("memberReportnumCnt get...");
		log.info("리액트에서 받은 id값 = " + id);

		service.memberReportnumCnt(id,reportid);
		
		return reportid;
	}
	
	//신고 누적횟수 
	@RequestMapping(value = "/memberReportnumRead", method = RequestMethod.GET)
	public int memberReportnumRead(Integer reportid, String id)throws Exception{
		log.info("memberReportnumRead get...");
		log.info("리액트에서 받은 reportid값 = " + reportid);
		log.info("리액트에서 받은 id값 = " + id);
		int reportnum = service.memberReportnumRead(id);
		
		return reportnum;
	}
	
	
	
	//축제list & 축제 문의list
	@RequestMapping(value = "/festaList", method = RequestMethod.GET)
	public Map<String, Object> festaList(Criteria cri)throws Exception{
		log.info("festaList Get....");
		log.info("cri = "+ cri.toString());
		List<QuestionDTO> festaList = service.festaList(cri);
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("questionDto", festaList);
		int total = service.festaListCnt();
		
		 
	    log.info("total : " + total);
	     
	    result.put("pageMaker", new PageDTO(cri, total));
	    
		return result;
	}
	
	//축제list 갯수
	@RequestMapping(value = "/festaListCnt", method = RequestMethod.GET)
	public int festaListCnt()throws Exception{
		log.info("festaListCnt Get....");
		int total = service.festaListCnt();
		log.info("total" + total);
		
		return total;
	}
	
	//축제 건의내용 list
	@RequestMapping(value = "/questionList", method = RequestMethod.GET)
	public Map<String, Object> questionList(Criteria cri, Integer contentid)throws Exception{
		log.info("questionList get...");
		log.info("받은 값 = " + contentid);
		log.info("받은 값 = " + cri.toString());
		
		List<QuestionDTO> list = service.questionList(cri, contentid);
		log.info("list = " + list.toString());
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("list", list);
		int total = service.questionListCnt(contentid);
		
		 log.info("total : " + total);
	     
		 result.put("pageMaker", new PageDTO(cri, total));
		
		return result;
	}
	
	//축제건의list 갯수
	@RequestMapping(value = "/questionListCnt", method = RequestMethod.GET)
	public int questionListCnt(Integer contentid)throws Exception{
		log.info("questionListCnt Get....");
		log.info("contentid = " + contentid);
		int total = service.questionListCnt(contentid);
		log.info("total" + total);
		
		return total;
	}
	
	//축제 건의 삭제
	@RequestMapping(value = "/questionDelete", method = RequestMethod.POST)
	public String questionDelete(String questionid)throws Exception{
		log.info("questionDelete post");
		log.info("받은 questionid 값 = " + questionid);
		service.questionDelete(questionid);
		
		return questionid;
	}
	
	//축제 삭제
	@RequestMapping(value = "/festaDelete", method = RequestMethod.POST)
	public int festaDelete(Integer contentid) throws Exception{
		log.info("festaDelete post...");
		log.info("받은 값" + contentid);
		
		service.festaDelete(contentid);
		
		return contentid;
	}
	
	//축제 건의내용 저장
	@RequestMapping(value = "/questionRegister", method = RequestMethod.POST)
	public void questionCreate(@RequestBody QuestionDTO questionDto) throws Exception{
		log.info("questionCreate post...");
		
		log.info("받은 값 = " + questionDto.toString());
		
		service.questionRegister(questionDto);
	}
	
	//게시판 list(자유 &리뷰)
	@RequestMapping(value = "/boardlist", method = RequestMethod.GET)
	public Map<String, Object> boardlist(Criteria cri)throws Exception{
		log.info("boardlist Get....");
		log.info("cri = " + cri.toString());
		Map<String, Object> result = new HashMap<>();
		
		List<BoardDTO> boardlist = service.boardlist(cri);
		
		
		result.put("list", boardlist);
		int total = service.boardListCnt();
		 
	     log.info("total : " + total);
	     
	     result.put("pageMaker", new PageDTO(cri, total));
	     log.info("result" + result.toString());
	     
	     
		return result;
	}
	
	//게시판 list(자유 &리뷰) 갯수
	@RequestMapping(value = "/boardListCnt", method = RequestMethod.GET)
	public int boardListCnt()throws Exception{
		log.info("boardlistCnt Get....");
		int total = service.boardListCnt();
		log.info("total" + total);
		
		return total;
	}
		
	
	//문의사항 list
	@RequestMapping(value = "/adminQuestionList", method = RequestMethod.GET)
	public Map<String, Object> adminQuestionList(Criteria cri)throws Exception{
		log.info("adminQuestionList Get....");
		log.info("cri = " + cri.toString());
		Map<String, Object> result = new HashMap<>();
		
		List<BoardDTO> adminQuestionList = service.adminQuestionList(cri);
		
		
		result.put("list", adminQuestionList);
		int total = service.adminQuestionListCnt();
		 
	     log.info("total : " + total);
	     
	     result.put("pageMaker", new PageDTO(cri, total));
	     log.info("result" + result.toString());
	     
	     
		return result;
	}
	
	//문의사항 list 갯수
	@RequestMapping(value = "/adminQuestionListCnt", method = RequestMethod.GET)
	public int adminQuestionListCnt()throws Exception{
		log.info("adminQuestionListCnt Get....");
		int total = service.adminQuestionListCnt();
		log.info("total" + total);
		
		return total;
	}
	
	//문의 bstatecode = c 변경
	@RequestMapping(value = "/adminQuestionbstatecodeChange", method = RequestMethod.POST)
	public void adminQuestionDelete(Integer bid)throws Exception{
		log.info("받은 bid 값 =" + bid);
		log.info("adminQuestionDelete post....");
		service.adminQuestionDelete(bid);
	}
	
	
	//DB 자동삭제 처리
	@Scheduled(cron = "0 0 0 * * *")//매일 자정에 실행
	public void DBdataDelete() throws Exception {
		log.info("DBdataDelete is started");
		
		String stringTime = LocalDateTime.now().minusYears(1).toString();
		String replaceTiem = stringTime.replace("-", "");
		
		//현재 서버 기준 날짜(YYYMMDD)
		String festaDeleteTime = replaceTiem.substring(0,8);
		//현재 서버 기준 날짜(YY/MM/DD)
		String newTime1 = festaDeleteTime.substring(2,4).concat("/");
		String newTime2 = festaDeleteTime.substring(4,6).concat("/");
		String newTime3 = festaDeleteTime.substring(6);
		String boardDeleteTime = newTime1.concat(newTime2).concat(newTime3);

		service.DBdataDelete(festaDeleteTime, boardDeleteTime);

	}
	
	
	
	
	
	
	
	//축제 댓글 신고(parameter : reportcontent, reporter, reported, rfrno)
	@RequestMapping(value = "/festaReplyReport", method = RequestMethod.POST)
	public void festaReplyReport(@RequestBody ReportDTO reportDto) throws Exception{
		log.info("festaReplyReport post...");
		log.info("받은 값 = " + reportDto.toString());
		
		service.festaReplyReport(reportDto);
	}
	
	//게시글 댓글 신고(parameter : reportcontent, reporter, reported, rbrno)
	@RequestMapping(value = "/boardReplyReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String boardReplyReport(@RequestBody ReportDTO reportDto) throws Exception{
		log.info("boardReplyReport post ...");
		MemberDTO memberDto = memberService.selMember(reportDto.getReported());
		String num = memberDto.getStatecode();
		log.info("받은 값 = " + reportDto.toString());
		log.info("회원 상태코드 = " + num);
		
		if(num.equals("1")) {
			int result = service.boardReplyReport(reportDto);
			return  "댓글이 신고 되었습니다.";
		}else {
			return "해당 댓글 작성자는 현재 사이트의 회원이 아닙니다. 신고가 불가능 합니다. 관리자에게 문의 바람니다.";
		}
	}
	
	//게시글 신고 (parameter : reportcontent, reporter, reported, rbid)
	@RequestMapping(value = "/boardReport", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String boardReport(@RequestBody ReportDTO reportDto) throws Exception{
		log.info("boardReport post ...");
		MemberDTO memberDto = memberService.selMember(reportDto.getReported());
		String num = memberDto.getStatecode();
		log.info("받은 값 = " + reportDto.toString());
		log.info("회원 상태코드 = " + num);
		
		if(num.equals("1")) {
			int result = service.boardReport(reportDto);
			return  "게시글이 신고 되었습니다.";
		}else {
			return "해당 게시글 작성자는 현재 사이트의 회원이 아닙니다. 신고가 불가능 합니다. 관리자에게 문의 바람니다.";
		}
	}
	 

}