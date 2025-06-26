package koBus.mvc.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

import com.util.DBConn;

import koBus.mvc.domain.ReservationDTO;

public class ReservationDAO {

    // 1. 예매 INSERT
	public boolean insertReservation(ReservationDTO dto) {
	    String insertSql = "INSERT INTO reservation "
	            + "(resID, bshID, seatID, kusID, rideDate, resvDate, resvStatus, resvType, qrCode, mileage, seatAble) "
	            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    String updateSeatSql = "UPDATE BUSSCHEDULE B "
	            + "SET REMAINSEATS = ( "
	            + "    SELECT COUNT(*) "
	            + "    FROM SEAT S "
	            + "    WHERE S.BUSID = B.BUSID "
	            + "      AND S.SEATABLE = 'Y' "
	            + ") "
	            + "WHERE B.BSHID = ( "
	            + "    SELECT R.BSHID "
	            + "    FROM RESERVATION R "
	            + "    WHERE R.RESID = ? "
	            + ")";

	    int insertResult = 0;
	    int seatUpdateResult = 0;

	    Connection conn = null;

	    try {
	        conn = DBConn.getConnection();
	        conn.setAutoCommit(false); // 트랜잭션 시작

	        // 1. 예약 INSERT
	        try (PreparedStatement pstmt1 = conn.prepareStatement(insertSql)) {
	            pstmt1.setString(1, dto.getResID());
	            pstmt1.setString(2, dto.getBshID());
	            pstmt1.setString(3, dto.getSeatID());
	            pstmt1.setString(4, dto.getKusID());
	            pstmt1.setTimestamp(5, Timestamp.valueOf(dto.getRideDateTime()));
	            pstmt1.setDate(6, dto.getResvDate());
	            pstmt1.setString(7, dto.getResvStatus());
	            pstmt1.setString(8, dto.getResvType());
	            pstmt1.setInt(9, dto.getQrCode());
	            pstmt1.setInt(10, dto.getMileage());
	            pstmt1.setString(11, dto.getSeatAble());

	            insertResult = pstmt1.executeUpdate();
	        }

	        System.out.println("예약 등록 완료: " + insertResult + "건");
	        System.out.println("rideDate: " + dto.getRideDate());

	        // 2. 좌석 수 갱신
	        try (PreparedStatement pstmt2 = conn.prepareStatement(updateSeatSql)) {
	            pstmt2.setString(1, dto.getResID());
	            seatUpdateResult = pstmt2.executeUpdate();
	        }

	        System.out.println("남은 좌석 수 갱신 완료: " + seatUpdateResult + "건");

	        conn.commit(); // 트랜잭션 커밋

	    } catch (SQLException e) {
	        System.out.println("[ReservationDAO] insertReservation 오류: " + e.getMessage());
	        try {
	            if (conn != null) conn.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        return false;
	    } finally {
	        try {
	            if (conn != null) conn.setAutoCommit(true); // 상태 복원
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // INSERT와 좌석 갱신이 모두 성공해야 true
	    return insertResult > 0 && seatUpdateResult > 0;
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
