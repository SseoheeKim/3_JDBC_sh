package edu.kh.jdbc1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample {
	public static void main(String[] args) {
		
		/* JDBC
		 - Java에서 DB에 연결(접근)할 수 있게 하는 기본 내장 인터페이스(Java Programming API)
		 - java.sql 패키지에서 제공
		 
		 
		 * JDBC를 이용한 애플리케이션을 만들 때 필요한 요소
		  1. Java의 JDBC관련 인터페이스
		  2. DBMS(Oracle)
		  3. OJDBC11.jar 라이브러리 -> OracleDriver.class(JDBC 드라이버) 이용
		  
		  
		  [참고] OJDBC11.jar 라이브러리
		  Oracle에서 Java애플리케이션과 연결할 때 사용할 클래스 모음으로
		  JDBC를 상속받아 구현한 클래스 모음  */
		
	
		
		
		// 1단계 : JDBC 객체 참조 변수 선언(java.sql패키지의 인터페이스)
		Connection conn = null;
		/* Connection 
		  : DB연결 정보를 담은 객체
		    DBMS 타입, 이름, IP, Port, 계정명, 비밀번호 등
	        (DBeaver의 계정 접속 방법과 유사)
	       
		 * Java와 DB 사이를 연결해주는 통로!!(Stream과 유사) */
	
		
		Statement stmt = null;
		/* Statement 
		  : Connection을 통해
		    SQL문을 DB에 전달하여 실행하고 생성된 결과를 반환하는데 사용하는 객체
		    (ResultSet, 성공한 행의 개수 등) */
		
		
		ResultSet rs = null;
		/* ResultSet
		  : SELECT질의 성공시 반환되는데 조회 결과 집합을 나타내는 객체 */
	
		
		try {
		// 2단계 : 참조 변수에 알맞는 객체 대입
			
			// 2-1) DB연결에 필요한 Oracle JDBC Driver메모리에 로드(==객체로 만들어두기)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// -> ()안에 작성된 클래스의 객체 반환
			// 	  메모리 안에 객체가 생성되면서 JDBC 필요 시 알아서 참조해서 사용
			// -> 생략하더라도 자동 메모리 로드 진행(단, 명시적 작성 권장)
			
			
			
			// 2-2) 연결 정보를 담은 Connection 생성
			//	   -> DriverManagere객체를 이용해 Connection 객체를 얻어옴 
			String type = "jdbc:oracle:thin:@"; // JDBC드라이버의 종류
			
			String ip = "localhost"; // DB서버 컴퓨터 IP
			// localhost == 127.0.0.1 (loop back ip)
			// 115.90.212.22 (서버 컴)
			
			String port = ":1521"; // 포트번호
			// 기본 port == 1521
			// 서버 port == 9000
			
			String sid = ":XE"; // DB이름
			
			String user = "kh_ksh";
			String pw = "kh1234";
			
			
			// DriverManagere
			// 메모리에 로드된 JDBC드라이버를 이용해 Connection 객체를 만드는 역할
			conn = DriverManager.getConnection(type+ip+port+sid, user, pw);
			// SQLException 데이터베이스 관련된 최상위 예외
			
			
			// System.out.println(conn); // 중간 확인
			// oracle.jdbc.driver.T4CConnection@6c40365c
			
			// 2-3) SQL작성
			// *** Java에서 작성되는 SQL은 마지막에 세미콜론 생략!!
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY, HIRE_DATE FROM EMPLOYEE";
			
			// 2-4) Statement 객체 생성
			// 		-> Connection 객체를 통해서 생성
			stmt = conn.createStatement();
					
			// 2-5) 생성된 Statement객체에 sql을 적재하여 실행한 후 
			//		결과를 반환받아와서 rs변수에 저장
			rs = stmt.executeQuery(sql);
			// executeQuery() : SELECT문 수행 메서드로 ResultSet반환
			
			
		// 3단계 : SQL을 수행해서 반환받은 결과 (ResultSet)에 한 행씩 접근해서 컬럼값 얻어오기
			while(rs.next()) {
				// rs.next() : rs가 참조하는 ResultSet 객체의 
				//            첫 번째 컬럼(행)부터 순서대로 한 행씩 이동하며 
				//			  다음 행이 있을 경우 true, 없으면 false 반환
				
				// rs.get자료형("컬럼명")
				String empId = rs.getString("EMP_ID"); 
				// 현재 행의 "EMP_ID" 문자형 컬럼값을 얻어옴
				
				String empName = rs.getString("EMP_NAME");
				// 현재 행의 "EMP_NAME" 문자형 컬럼값을 얻어옴
				
				int salary = rs.getInt("SALARY");
				// 현재 행의 "SALARY" 정수형(숫자) 컬럼값을 얻어옴		
				
				Date hireDate = rs.getDate("HIRE_DATE");
				// import java.sql.Date;
				// import java.util.Date;
				// java.sqld은 java.util상속하기 때문에 둘다 사용 가능하지만, 자식이 더 상세하니까 java.sql 사용
				
				
				// 조회 결과 출력
				System.out.printf("사번 : %s / 이름 : %s / 급여 : %d / 입사일 : %s\n", empId, empName, salary, hireDate.toString());
				// java.sql.Date의 toString()은 'yyyy-mm-dd'형식으로 오버라이딩 되어 있는 상태
			}
			
		} catch(ClassNotFoundException e) {  // Class.forName 예외
			System.out.println("JDBC Driver 경로가 잘못 작성되었습니다.");
			
		} catch(SQLException e) {
			e.printStackTrace();
			
	    } finally {
	    // 4단계: 사용한 JDBC객체 자원 반환( close() )
	    	// ResultSet, Statement, Connection 생성 역순으로 닫는 것을 권장
	    	try {
	    		if(rs != null) rs.close();
	    		if(stmt != null) stmt.close();
	    		if(conn != null) conn.close();
	    	} catch(SQLException e) {
	    		e.printStackTrace();
	    	}
		}
	}
}
