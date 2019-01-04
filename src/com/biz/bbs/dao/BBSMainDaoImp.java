package com.biz.bbs.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.biz.bbs.vo.BBSMainVO;

/*
 * 반드시
 * DaoImp는 Dao interface를 implement해서 구현을 해야 한다.
 */
public class BBSMainDaoImp implements BBSMainDao {
	/*
	 * DB 연결 설정
	 */

	Connection dbConn;
	
	public BBSMainDaoImp() {
		this.dbConntion();
	}
	
	/*
	 * dbConn 맴버변수를 초기화 하는 메서드
	 * dbConn 맴버변수 : db에 접속하기 위한 통로를 마련하고 그 정보를 가지고 있는 변수
	 */
	public void dbConntion() {
		// oracle.jdbc.driver.OracleDriver.class
		String dbDriver = "oracle.jdbc.driver.OracleDriver";
		
	
		try {
			// Driver Loading
			Class.forName(dbDriver);
			
			// DB 접속 profile
			String url ="jdbc:oracle:thin:@localhost:1521:xe";
			String user ="bbsuser";
			String password = "1234";
			
			dbConn = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	@Override
	public void insert(BBSMainVO vo) {
		// TODO vo를 매개변수로 전달받아 DB에 저장하기
		
		String strDate = vo.getB_date(); //vo안에 b_date를 가져와서 strdate에 저장
		String strAuth = vo.getB_auth(); //vo안에 b_auth를 가져와서 strAuth에 저장
		String strSubject = vo.getB_subject(); //vo안에 b_subject를 가져와서 strsubject에 저장
		String strText = vo.getB_text(); // vo안에 b_text를 가져와서 strtext에 저장
		
		// 구코드
		String sql = " INSERT INTO tbl_bbs_main "; // 빈칸을 넣지 않으면 실행이 되지 않음.(중요)
		sql += " VALUES( SEQ_BBS_MAIN.NEXTVAL, ";
		sql += " ' " + strDate + "' , ";
		sql += " ' " + strAuth + "' , ";
		sql += " ' " + strSubject + "' , ";
		sql += " ' " + strText ;
		sql += " ) ";
		
		// 신코드
		sql = " INSERT INTO tbl_bbs_main "; // ""안의 값을 sql에 추가.
		sql += " VALUES( SEQ_BBS_MAIN.NEXTVAL, ?, ?, ?, ? )"; // ""안의 값을 sql에 다시 또 추가
		
		PreparedStatement ps;
		try {
			
			ps = dbConn.prepareStatement(sql);
			ps.setString(1, strDate);
			ps.setString(2, strAuth);
			ps.setString(3, strSubject);
			ps.setString(4, strText);
			
			//ps.executeQuery();
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private BBSMainVO resultSet2BBSMainVO(ResultSet rs) {
		// 게시판 V2.0 Upgrade
		try {
			BBSMainVO vo = new BBSMainVO(
					rs.getLong("b_id"),
					rs.getString("b_date"),
					rs.getString("b_auth"),
					rs.getString("b_subject"),
					rs.getString("b_text")
					);
			return vo;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	@Override
	public List<BBSMainVO> selectAll() {
		// TODO DB로부터 모든 게시판 데이터를 읽어 List로 리턴
		
		String sql =" SELECT * FROM tbl_bbs_main ";
		
		// JDBC 연결 객체
		PreparedStatement ps;
		
		try {
			// DB에 연결하기 위하여 SQL문자열을 JDBC형태로 변환
			ps = dbConn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			// rs 데이터를 List 데이터로 변환
			List<BBSMainVO> bbsList = new ArrayList();
			
			// while(true)
			// if( rs.next() == false) break
			while(rs.next()) {
				/*
				Long id = rs.getLong("b_id");
				String strDate = rs.getString("b_date");
				String strAuth = rs.getString("b_auth");
				String strSubject = rs.getString("b_subject");
				String strText = rs.getString("b_text");
				
				BBSMainVO vo = new BBSMainVO();
				vo.setB_id(id);
				vo.setB_date(strDate);
				vo.setB_auth(strAuth);
				vo.setB_subject(strSubject);
				vo.setB_text(strText);
				*/
				
				// 게시판 2.0 Upgrade
				/*
				BBSMainVO vo = new BBSMainVO(
						rs.getLong("b_id"),
						rs.getString("b_date"),
						rs.getString("b_auth"),
						rs.getString("b_subject"),
						rs.getString("b_text")
						);
				*/
				// 게시판 V2.1
				bbsList.add(resultSet2BBSMainVO(rs));
			}
			rs.close();
			
			return bbsList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

	@Override
	public BBSMainVO findById(long id) {
		// TODO Auto-generated method stub
		
		String sql = " SELECT * FROM tbl_bbs_main ";
		sql += " WHERE b_id = ? ";
		
		PreparedStatement ps;
		
		try {
			ps = dbConn.prepareStatement(sql);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			// executeQuery는 ResultSet을 만드는 sql에서 사용하며, 주로 SELECT문을 수행할때 사용
			
			rs.next();
			
			// 게시판 V2.1
			return resultSet2BBSMainVO(rs);
						
						
			/*
			BBSMainVO vo = new BBSMainVO();
			
			vo.setB_id(rs.getLong("b_id"));
			vo.setB_date(rs.getString("b_date"));
			vo.setB_auth(rs.getString("b_auth"));
			vo.setB_subject(rs.getString("b_subject"));
			vo.setB_text(rs.getString("b_text"));
			*/
			
			// 게시판 V2.0 Upgrade(반드시 생성자의 순서를 알아야함)
			/*
			BBSMainVO vo = new BBSMainVO(
					rs.getLong("b_id"),
					rs.getString("b_date"),
					rs.getString("b_auth"),
					rs.getString("b_subject"),
					rs.getString("b_text")
					);
			return vo;
			*/
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void update(BBSMainVO vo) {
		// TODO vo를 매개변수로 받아 해당 레코드 데이터를 수정
		String sql = " UPDATE tbl_bbs_main ";
		sql += " Set b_auth = ?, ";
		sql += " b_subject = ?, ";
		sql += " b_text = ? ";
		sql += " WHERE b_id = ? ";
		
		PreparedStatement ps;
		
		try {
			ps = dbConn.prepareStatement(sql);
			ps.setString(1, vo.getB_auth());
			ps.setString(2, vo.getB_subject());
			ps.setString(3, vo.getB_text());
			
			ps.setLong(4, vo.getB_id());
			
			ps.executeUpdate(); //executeUpdate는 Insert나 Update와 같은 DDL, DML 명령문을 사용할때 이용
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void delete(long id) {
		// TODO Id를 매개변수로 받아 해당 레코드를 삭제
		String sql = " DELETE FROM tbl_bbs_main ";
		sql += " WHERE b_id = ? ";
		
		PreparedStatement ps;
		
		try {
			
			ps = dbConn.prepareStatement(sql);
			ps.setLong(1, id);
			ps.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

	
}
