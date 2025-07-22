package org.kobus.spring.service.member;

import java.sql.SQLException;

public class MemberMyPageServiceImpl implements MemberMyPageService{

	@Override
	public int reservationCount(String auth) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int popCouponCount(String auth) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int freeCouponCount(String auth) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTelNum(String auth) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOldPw(String auth) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updatePw(String auth, String changePw) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateTel(String auth, String changeTel) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String deleteUsr(String auth) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
