package org.kobus.spring.service.join;

import java.sql.SQLException;

import org.kobus.spring.domain.join.JoinDTO;

public interface JoinService {

	// 전화번호 입력해서 인증번호받기
	String telCertificationNum(String inputNumber, int randomNumber) throws SQLException;

	// 회원가입한 회원정보 DB에 insert
	int insert(JoinDTO dto) throws SQLException;

}
