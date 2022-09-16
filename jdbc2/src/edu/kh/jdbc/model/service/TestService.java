package edu.kh.jdbc.model.service;
// import static 
// -> static이 붙은 필드, 메서드를 호출 시 클래스명 생략 가능

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;
import java.sql.SQLException;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dao.TestDao;
import edu.kh.jdbc.model.vo.TestVo;

// Service : 비즈니스 로직(데이터 가공, 트랜잭션 제어 등) 처리
//			-> 실제 프로그램이 제공하는 기능

// 하나의 service에서 n개의 DAO메서드(지정된 SQL수행)를 호출하여
// 이를 하나의 트랜잭션 단위로 취급하여 한번에 commit, rollback 수행 가능
// (트랜잭선 단위 내에서 단 하나라도 SQL 성공한 행이 없다면 전부 실패)

// 여러 DML을 수행하지 않는 경우라도 코드의 통일성을 지키기 위해 Service에서 Connection 객체 생성
// -> Connection 객체가 commit, rollback제공

public class TestService {
	private TestDao dao = new TestDao();
	
	/** 1행 삽입 서비스
	 * @param vo1
	 * @return result
	 */
	public int insert(TestVo vo1) throws SQLException {
		// 무조건 첫 줄에는 커넥션 생성, return 전에는 커넥션 반환!!
		Connection conn = /*JDBCTemplate.*/getConnection();

		// INSERT DAO 메서드를 호출해서 수행하고 결과 반환 받기
		// -> service에서 생성한 Connection 객체를 같이 전달한다
		int result = dao.insert(conn, vo1);
		// result == SQL수행 후 반영된 결과 성공한 행의 수
		
		
		// 트랜잭션 제어
		if(result > 0) /*JDBCTemplate.*/commit(conn);
		else		   /*JDBCTemplate.*/rollback(conn);
		
		// 커넥션 반환
		/*JDBCTemplate.*/close(conn);
		// import구문으로 JDBCTemplate 클래스명 생략가능!!!!!
		
		// 결과 반환
		return result;
	}

	
	
	/** 3행 삽입 서비스
	 * @param vo1
	 * @param vo2
	 * @param vo3
	 * @return result
	 * @throws Exception 
	 */
	public int insert(TestVo vo1, TestVo vo2, TestVo vo3) throws Exception {
		// throws Exception
		// 아래 catch문에서 강제 발생된 예외를 호출부에서 처리하도록 호출부로 던짐
		// 왜 예외를 강제로 발생시켰는가?
		// Run2클래스에서 예외 발생에 대한 다른 처리 결과를 보여주기 위해서
		
		// 서비스 수행에서 먼저 connection 객체 생성
		Connection conn = getConnection();
		
		int res = 0; 
		// insert 3회 모두 성공 시 1, 아니면 0
		
		try {
			// insert중 오류가 발생하면 모든 insert내용 rollback
			// try-catch를 통해 직접 예외 처리
			
			int result1 = dao.insert(conn, vo1);
			int result2 = dao.insert(conn, vo2);
			int result3 = dao.insert(conn, vo3);
			
			//  트랜잭션(result1,2,3을 하나의 작업단위로 묶어둔) 제어
			if(result1+result2+result3 == 3) { // result 3개 전부 insert성공한 경우
				commit(conn);
				res = 1;
			} else { // result가 1~2개만 성공한 경우
				rollback(conn);
			}

		} catch (SQLException e) { // dao.insert() 수행 중 예외 발생 시 직접 처리
			rollback(conn); 
			// result1,2,3 중 하나라도 예외가 발생한 경우
			// 저장에 실패한 데이터를 DB에 삽입하지 않고, 저장에 성공한 데이터만 저장
			//  == DB에 저장된 데이터의 신뢰도가 상승
			e.printStackTrace();
			
			
			// Run2 클래스로 예외를 던져서 처리할 수 있도록 예외 강제 발생
			throw new Exception("DAO수행 중 예외 발생");
			// -> 강제로 예외를 발생시켜서 호출부로 던짐
			
			
		} finally {
			close(conn);
		}
		
		return res; // insert 3회 결과를 반환
	}



	public int update(TestVo vo) {
		Connection conn = getConnection();
		int result = 0;
		
		try {
			result = dao.update(conn, vo);
			
			if(result > 0) commit(conn);
			else		   rollback(conn);
			
		} finally {
			close(conn);
		}
		return result;
	}



	







	
}
