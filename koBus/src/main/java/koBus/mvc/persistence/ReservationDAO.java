package koBus.mvc.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import com.util.DBConn;

import koBus.mvc.domain.ReservationDTO;

public class ReservationDAO {

    // 1. 예매 INSERT
	public boolean insertReservation(ReservationDTO dto) {
		String sql = "INSERT INTO reservation "
				+ "(resID, bshID, seatID, kusID, rideDate, resvDate, resvStatus, resvType, qrCode, mileage, seatAble) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int cnt = 0;

		Connection conn = null;

	    try {
	        conn = DBConn.getConnection();          // 트랜잭션 시작을 위해 외부에서 선언
	        conn.setAutoCommit(false);              // 트랜잭션 시작

	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, dto.getResID());
	            pstmt.setString(2, dto.getBshID());
	            pstmt.setString(3, dto.getSeatID());
	            pstmt.setString(4, dto.getKusID());
	            pstmt.setDate(5, dto.getRideDate());
	            pstmt.setDate(6, dto.getResvDate());
	            pstmt.setString(7, dto.getResvStatus());
	            pstmt.setString(8, dto.getResvType());
	            pstmt.setInt(9, dto.getQrCode());
	            pstmt.setInt(10, dto.getMileage());
	            pstmt.setString(11, dto.getSeatAble());

	            cnt = pstmt.executeUpdate();
	            
	            System.out.println("dto.getRideDate() " + dto.getRideDate());
	            
//	         // 2. 좌석 상태 변경
//				String seatSql = "UPDATE BUSSCHEDULE B "
//						+ "SET REMAINSEATS = ( "
//						+ "    SELECT COUNT(*) "
//						+ "    FROM SEAT S "
//						+ "    WHERE S.BUSID = B.BUSID "
//						+ "      AND S.SEATABLE = 'Y' "
//						+ " ) "
//						+ " WHERE B.BSHID = ( "
//						+ "    SELECT R.BSHID "
//						+ "    FROM RESERVATION R "
//						+ "    WHERE R.RESID = ? "
//						+ "      AND R.RIDEDATE = TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS') "
//						+ " ) " ;
//				pstmt = conn.prepareStatement(seatSql);
//				pstmt.setString(1, mrsMrnpNo);
//				pstmt.setString(2, rideTime);
//				seatResult = pstmt.executeUpdate();
	            

	            conn.commit();  // 커밋
	        }

	    } catch (SQLException e) {
	        try {
	            if (conn != null) conn.rollback();  // 실패 시 롤백
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        System.out.println("[ReservationDAO] insertReservation 오류: " + e.getMessage());
	        return false;
	    } finally {
	        try {
	            if (conn != null) conn.setAutoCommit(true);  // 자동 커밋 복원
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }

	    // 변경된 행 수가 1 이상인지 확인 후 리턴
	    if (cnt > 0) {
	        return true;
	    } else {
	        return false;
	    }
	}

    // 2. 예매 상세 조회
    public ReservationDTO selectReservationDetail(String resID) {
        String sql = "SELECT * FROM reservation WHERE resID = ?";

        try (
            Connection conn = DBConn.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, resID);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setResID(rs.getString("resid"));
                    dto.setBshID(rs.getString("bshid"));
                    dto.setSeatID(rs.getString("seatid"));
                    dto.setKusID(rs.getString("kusid"));
                    dto.setRideDate(rs.getDate("ridedate"));
                    dto.setResvDate(rs.getDate("resvdate"));
                    dto.setResvStatus(rs.getString("resvstatus"));
                    dto.setResvType(rs.getString("resvtype"));
                    dto.setQrCode(rs.getInt("qrcode"));
                    dto.setMileage(rs.getInt("mileage"));
                    dto.setSeatAble(rs.getString("seatable"));

                    return dto;
                }
            }

        } catch (Exception e) {
            System.out.println("[ReservationDAO] selectReservationDetail 오류: " + e.getMessage());
        }
        return null;
    }

    // 3. 예매 ID 생성 (시퀀스)
    public String generateResId() {
        String sql = "SELECT 'RES' || reservation_seq.NEXTVAL FROM dual";

        try (
            Connection conn = DBConn.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        ) {
            if (rs.next()) {
                return rs.getString(1);
            }

        } catch (SQLException e) {
            System.out.println("[ReservationDAO] generateResId 오류: " + e.getMessage());
        }
        return null;
    }

    public int changeReservation(String changeResId) {
        String sql = "DELETE FROM reservation WHERE resid = ?";
        
        try (
            Connection conn = DBConn.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            pstmt.setString(1, changeResId);
            int result = pstmt.executeUpdate();
            return result;
        } catch (SQLException e) {
            System.out.println("[ReservationDAO] changeReservation 오류: " + e.getMessage());
        }
        
        return 0; // 실패 시 0 반환
    }

}
