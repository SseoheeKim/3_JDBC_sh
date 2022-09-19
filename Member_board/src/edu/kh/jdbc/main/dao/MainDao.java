package edu.kh.jdbc.main.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.member.vo.Member;


// DAO : DB 연결용 객체
public class MainDao {
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	// null은 생략 가능
	
	private Properties prop = null;
	// Map<String, String>형식으로 제한, XML파일 읽고/쓰는데 특화된 객체
	
	
	// (매개변수가 없는) 기본 생성자
	public MainDao() {
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("main-query.xml"));
			// "main-query.xml" 파일의 내용을 읽어와 Properties 객체에 저장

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	/** 아이디 중복 검사 DAO
	 * @param conn
	 * @param memberId
	 * @return result 
	 * @throws Exception
	 */
	public int idDupCheck(Connection conn, String memberId) throws Exception {
		// 1. 결과 저장용 변수 
		int result = 0;
		
		try {
			// 2. sql얻어오기
			String sql =  prop.getProperty("idDupCheck");
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 알맞는 값 세팅
			pstmt.setString(1, memberId);
			
			// 5. sql 실행 후 결과 반환받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과 옮겨닮기 -> 1행 조회시 if, N행 조회시 while
			if(rs.next()) {
				// result = rs.getInt("COUNT(*)"); // 컬럼명
				result = rs.getInt(1);
			}
			
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환
			close(rs);
			close(pstmt);
		}
		
		// 8. 결과 반환
		return result;
	}


	/** 회원 가입 DAO
	 * @param conn
	 * @param member
	 * @return result 
	 * @throws Exception
	 */
	public int signUp(Connection conn, Member member) throws Exception {
		
		int result = 0;
		
		try {
			
			String sql = prop.getProperty("signUp");
			
			// placeholder 사용시 PreparedStatement 객체 사용
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getMemberPw());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getMemberGender());
			
			result = pstmt.executeUpdate(); // DML 수행 후 성공한 행의 수를 반환
		
		} finally {
			close(pstmt);
		}
		
		return result;
	}



	/** 로그인 DAO
	 * @param conn
	 * @param memberId
	 * @param memberPw
	 * @return loginMember
	 * @throws Exception
	 */
	public Member login(Connection conn, String memberId, String memberPw) throws Exception{
		// 1. 결과 저장용 변수
		Member loginMember = null;

		try {
			// 2. SQL 얻어오기
			String sql = prop.getProperty("login");
			
			// 3. ? 기호 - PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// 4. ? 값 대입
			pstmt.setString(1, memberId);
			pstmt.setString(2, memberPw);
			
			// 5. SQL 수행 후 결과 반환받기
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과 있을 경우 값 얻어오기
			if(rs.next()) {
				loginMember = new Member(rs.getInt("MEMBER_NO"),
										memberId, 
										rs.getString("MEMBER_NM"),
										rs.getString("MEMBER_GENDER"),
										rs.getString("ENROLL_DATE"));
			}
			
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환
			close(rs);
			close(pstmt);
		}
		
		// 8.결과 반환
		return loginMember;
	}
}
