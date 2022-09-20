package edu.kh.jdbc.member.model.service;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.member.model.dao.MemberDao;
import edu.kh.jdbc.member.vo.Member;

public class MemberService {
	private MemberDao dao = new MemberDao();

	/** 회원 목록 조회 
	 * @return memberList
	 * @throws Exception
	 */
	public List<Member> selectAll() throws Exception {
		Connection conn = getConnection();
		
		List<Member> memberList = dao.selectAll(conn);
		
		close(conn);
		
		return memberList;
	}

	
	/** 회원 정보 수정
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int updateMyinfo(Member member) throws Exception{
		
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. DAO 메서드 호출 후 결과 반환
		int result = dao.updateMyinfo(conn, member);
		
		
		// 3. 트랜잭션 제어
		if(result > 0) commit(conn);
		else		   rollback(conn);
		
		// 4. 커넥션 반환
		close(conn);
		
		// 5. 수정 결과 반환
		return result;
	}


	/** 비밀번호 변경 서비스
	 * @param crruntPw
	 * @param newPw1
	 * @param memberNo
	 * @return
	 * @throws Exception
	 */
	public int updatePw(String curruntPw, String newPw1, int memberNo) throws Exception {
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. DAO 메서드 호출 후 결과 반환
		int result = dao.updatePw(conn, curruntPw, newPw1, memberNo);
		
		
		// 3. 트랜잭션 제어
		if(result > 0) commit(conn);
		else		   rollback(conn);
		
		// 4. 커넥션 반환
		close(conn);
		
		// 5. 수정 결과 반환
		return result;
	}


	/** 회원 탈퇴 서비스
	 * @param memberPw
	 * @param memberNo
	 * @return result
	 * @throws Exception
	 */
	public int secession(String memberPw, int memberNo) throws Exception{
		Connection conn = getConnection();
		int result = dao.secession(conn, memberPw, memberNo);
		if(result > 0) commit(conn);
		else		   rollback(conn);
		close(conn);
		return result;
	}

	
	
	
	
	
	
}
