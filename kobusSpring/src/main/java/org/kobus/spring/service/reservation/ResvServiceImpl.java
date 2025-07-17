package org.kobus.spring.service.reservation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kobus.spring.domain.reservation.ResvDTO;
import org.kobus.spring.mapper.reservation.ResvMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ResvServiceImpl implements ResvService {
	
	@Autowired
	ResvMapper resvMapper;

	@Override
	public List<ResvDTO> searchResvList(String loginId) throws SQLException {
		return resvMapper.searchResvList(loginId);
	}

	@Override
	public int cancelResvList(String mrsMrnpNo) throws SQLException {
		// TODO Auto-generated method stub
		return resvMapper.cancelResvList(mrsMrnpNo);
	}

	@Override
	public List<ResvDTO> searchCancelResvList(String loginId) throws SQLException {
	    return resvMapper.searchCancelResvList(loginId);
	}


	@Override
	public int changeRemainSeats(String mrsMrnpNo, String rideTime) throws SQLException {
		return resvMapper.changeRemainSeats(mrsMrnpNo, rideTime);
	}

}
