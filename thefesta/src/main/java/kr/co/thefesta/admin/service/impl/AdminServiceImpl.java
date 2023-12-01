package kr.co.thefesta.admin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.thefesta.admin.domain.Criteria;
import kr.co.thefesta.admin.domain.ReportDTO;
import kr.co.thefesta.admin.persistence.IAdminDAO;
import kr.co.thefesta.admin.service.IAdminService;
import kr.co.thefesta.member.domain.MemberDTO;


@Service
public class AdminServiceImpl implements IAdminService {
	@Autowired
	public IAdminDAO adminDao;
	
	@Override
	//member 테이블 회원정보 list
	public List<MemberDTO> memberList(Criteria cri) throws Exception {
		return adminDao.memberList(cri);
	}
	
	//member 회원 디테일 정보
	@Override
	public List<ReportDTO> memberDetail(String id) throws Exception {
		return adminDao.memberDetail(id);
	}
	
	//회원 신고내용
	@Override
	public String memberReport(Integer reportid) throws Exception {
		return adminDao.memberReport(reportid);
	}
	
	//회원 신고글 삭제
	@Override
	public int memberReportDelete(Integer reportid) throws Exception {
		return adminDao.memberReportDelete(reportid);
		
	}
	
	//신고 list
	@Override
	public List<ReportDTO> reportList(Criteria cri) throws Exception {
		return adminDao.reportList(cri);
	}
	
	//신고 list 총 갯수
	@Override
	public int reportListCnt() throws Exception {
		return adminDao.reportListCnt();
	}
	
	//신고처리상태 변경(reportstate->2)
	@Override
	public int reportstateChange(Integer reportid, String id) throws Exception {
		return adminDao.reportstateChange(reportid, id);
	}
	
	//member list 총 갯수
	@Override
	public int memberListCnt() throws Exception {
		return adminDao.memberListCnt();
	}
	
	//축제list & 축제 문의list
	@Override
	public Map<String, Object> festaList(Criteria cri) throws Exception {
		return adminDao.festaList(cri);
	}

	
	

}
