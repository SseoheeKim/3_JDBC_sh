package edu.kh.jdbc.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.vo.TestVo;


// DAO : 데이터가 저장된 DB에 접근하는 객체로
//		-> SQL을 수행하여 결과를 반환받는 기능
public class TestDao {
	
	// JDBC 객체를 참조하기 위한 참조변수 선언
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// XML을 이용하여 SQL을 다루기 위해 Properties 객체
	private Properties prop;
	
	/**
	 * 기본생성자
	 */
	public TestDao(){
		// TestDao 객체가 기본 생성자로 객체 생성될 때
		// test-query.xml 파일의 내용을 읽어와 properties 객체에 저장
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("test-query.xml"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/** 1행 삽입 DAO
	 * @param conn
	 * @param vo1
	 * @return result
	 */
	public int insert(Connection conn, TestVo vo1) throws SQLException {
								// pstmt 객체에서 발생한 예외를 모아서 처리하기 위해서
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL작성(test-query.xml 파일에 작성된 (prop에 저장된)SQL얻어오기)
			String sql = prop.getProperty("insert");
			
			// 3. PreparedState 객체 생성
			pstmt = conn.prepareStatement(sql);
			// -> throws 예외처리 사용 (호출한 곳으로 발생한 예외를 던지는 방법)
			
			// 4. 위치홀더 ? 에 알맞는 값 세팅
			pstmt.setInt(1, vo1.getTestNo());
			pstmt.setString(2, vo1.getTestTitle());
			pstmt.setString(3, vo1.getTestContent());

			// 5. SQL수행 후 결과 반환받기
			result = pstmt.executeUpdate();
					// executeUpdate() dml 수행 후 반영된 행의 개수
		
		} finally {
			// 6. 사용한 JDBC객체 자원 반환
			close(pstmt);
        }
		
		return result;
	}


	public int update(Connection conn, TestVo vo) {
		int result = 0;
		
		try {
		
	    String sql = prop.getProperty("update");
		pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, vo.getTestTitle());
		pstmt.setString(2, vo.getTestContent());
		pstmt.setInt(3, vo.getTestNo());
		
		result = pstmt.executeUpdate();
				
		} catch (SQLException e){
			close(pstmt);
		} 
		return result;
	}












}
