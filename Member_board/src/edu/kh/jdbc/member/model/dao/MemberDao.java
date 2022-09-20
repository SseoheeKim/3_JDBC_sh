package edu.kh.jdbc.member.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.spi.DirStateFactory.Result;

import edu.kh.jdbc.member.vo.Member;

public class MemberDao {
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties prop;
	
	public MemberDao() {
		try {
			
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("member-query.xml"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	/** 회원 목록 조회 DAO
	 * @param conn
	 * @return memberList
	 * @throws Exception
	 */
	public List<Member> selectAll(Connection conn) throws Exception {
		// 1. 결과 저장용 변수 선언
		List<Member> memberList = new ArrayList<>();
		
		try {
			// 2. SQL 얻어오기
			String sql = prop.getProperty("selectAll");
			
			// 3. stmt 객체 생성
			stmt = conn.createStatement();
			
			// 4. SQL수행 후 결과 반환 받기
			rs = stmt.executeQuery(sql);
			
			// 5. Member 객체에 List 추가
			while(rs.next()) {
				String memberId = rs.getString("MEMBER_ID");
				String memberName = rs.getString("MEMBER_NM");
				String memberGender = rs.getString("MEMBER_GENDER");
				
				
				// 방법1) 매개변수가 있는 생성자 생성
				// Member member = new Member(memberId, memberName, memberGender);
				// memberList.add(member);
				
				// 방법2) 기본 생성자를 이용 -> 더 많이 사용하는 방법
				Member member = new Member();
				member.setMemberId(memberId);
				member.setMemberName(memberName);
				member.setMemberGender(memberGender);
				
				memberList.add(member);
			}
			
			
		} finally {
			// 6. JDBC 객체 자원 반환
			close(rs);
			close(stmt);
		}
		
		// 7. 조회 결과를 옮겨 담은 리스트를 반환
		return memberList;
	}

	
	/** 회원 정보 수정 DAO
	 * @param conn
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int updateMyinfo(Connection conn, Member member) throws Exception {
		// 결과 저장용 변수 생성
		int result = 0;
		
		try {
			// SQL 얻어오기
			String sql = prop.getProperty("updateMyinfo");
			
			// ? 이용을 위한 PrepareStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ? 알맞은 값 대입
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getMemberGender());
			pstmt.setInt(3, member.getMemberNo());

			// 성공한 행의 수 반환
			result = pstmt.executeUpdate();
			
			
		} finally {
			// JDBC객체 자원 반환
			close(pstmt);
		}
		
		// 수정 결과 반환
		return result;
	}

	
	
	
	/** 비밀번호 변경 DAO
	 * @param conn
	 * @param crruntPw
	 * @param newPw1
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int updatePw(Connection conn, String curruntPw, String newPw1, int memberNo) throws Exception {
		// 결과 저장용 변수 생성
		int result = 0;
		
		try {
			// SQL 얻어오기
			String sql = prop.getProperty("updatePw");
			
			// ? 이용을 위한 PrepareStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// ? 알맞은 값 대입
			pstmt.setString(1, newPw1);
			pstmt.setInt(2, memberNo);
			pstmt.setString(3, curruntPw);

			// 성공한 행의 수 반환
			result = pstmt.executeUpdate();
			
			
		} finally {
			// JDBC객체 자원 반환
			close(pstmt);
		}
		
		// 수정 결과 반환
		return result;
	}

	/** 회원 탈퇴 DAO
	 * @param conn
	 * @param memberPw
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int secession(Connection conn, String memberPw, int memberNo) throws Exception{
		int result = 0;
		
		
		try {
			String sql = prop.getProperty("secession");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, memberNo);
			pstmt.setString(2, memberPw);
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}
	
	
	
	
}
