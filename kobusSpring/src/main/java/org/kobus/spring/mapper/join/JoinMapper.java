package org.kobus.spring.mapper.join;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Param;
import org.kobus.spring.domain.join.JoinDTO;


public interface JoinMapper {
		
		// 회원가입한 회원정보 DB에 insert
		int insert(JoinDTO dto) throws SQLException;

}
