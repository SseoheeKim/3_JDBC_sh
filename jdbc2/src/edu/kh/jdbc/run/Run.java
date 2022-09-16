package edu.kh.jdbc.run;

import java.sql.SQLException;

import edu.kh.jdbc.model.service.TestService;
import edu.kh.jdbc.model.vo.TestVo;

// TB_TEST테이블에 1행 INSERT 수행
public class Run {
	public static void main(String[] args) {
		TestService service =new TestService();
		
		// DBeaver에 존재하는 TB_TEST테이블에 INSERT수행
		TestVo vo1 = new TestVo(1, "제목1", "내용1");
		
		// TB_TEST테이블에 insert를 수행하는 서비스 메서드 호출 후 결과 반환
		try {
			int result = service.insert(vo1);
			
			if(result > 0) {
				System.out.println("[insert 성공]");
			} else {
				System.out.println("[insert 실패]");
				// 서브쿼리를 INSERT시 실패할 가능성 존재
			}
		} catch (SQLException e) {
			System.out.println("SQL 수행 중 오류 발생");
			e.printStackTrace();
		}
	}
}
