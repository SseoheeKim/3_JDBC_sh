package edu.kh.jdbc.run;

import edu.kh.jdbc.model.service.TestService;
import edu.kh.jdbc.model.vo.TestVo;

// TB_TEST 테이블에 한번에 3행 삽입
public class Run2 {
	public static void main(String[] args) {
		
		TestService service = new TestService();
		
		TestVo vo1 = new TestVo(40, "제목40", "내용 40입니다.");
		TestVo vo2 = new TestVo(50, "제목50", "내용 50입니다.");
		TestVo vo3 = new TestVo(60, "제목60", null);
		
		try {
			int result = service.insert(vo1, vo2, vo3);
			
			if(result > 0) {
				System.out.println("[삽입 성공]");
			} else {
				System.out.println("[삽입 실패]");
			}
			
		} catch (Exception e) {
			// service, dao 수행 중 발생해서 던져진 예외 처리
			System.out.println("[SQL수행 중 오류 발생!]");
			e.printStackTrace();
		}
	}
}
