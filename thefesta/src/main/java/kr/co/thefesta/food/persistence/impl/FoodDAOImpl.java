package kr.co.thefesta.food.persistence.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.thefesta.festival.domain.FestivalDTO;
import kr.co.thefesta.food.domain.AreacodeDTO;
import kr.co.thefesta.food.domain.ItemDTO;
import kr.co.thefesta.food.domain.LikeDTO;
import kr.co.thefesta.food.domain.RecommendDTO;
import kr.co.thefesta.food.persistence.IFoodDAO;
import lombok.extern.log4j.Log4j;

@Repository
@Log4j
public class FoodDAOImpl implements IFoodDAO {

	@Autowired
	private SqlSession session;

	@Override
	public void insert(ItemDTO itemDto) throws Exception {
		session.insert("FoodMapper.insert", itemDto);
	}

	@Override
	public void delete() throws Exception {
		session.delete("FoodMapper.delete");
	}

	@Override
	public List<RecommendDTO> listAll() throws Exception {
		return session.selectList("FoodMapper.select");
	}

	@Override
	public ItemDTO read(String contentid) throws Exception {
		return session.selectOne("FoodMapper.read", contentid);
	}

	@Override
	public List<FestivalDTO> selectFestaId() throws Exception {
		return session.selectList("FoodMapper.selectFestaid");
	}

	@Override
	public AreacodeDTO selectArea() throws Exception {
		return session.selectOne("FoodMapper.selectAreacode");
	}

	@Override
	public void insertLike(LikeDTO likeDto) throws Exception {
		session.insert("FoodMapper.insertLike", likeDto);
	}

	@Override
	public void delete(LikeDTO likeDto) throws Exception {
		session.delete("FoodMapper.deleteLike", likeDto);
	} 
	
}
