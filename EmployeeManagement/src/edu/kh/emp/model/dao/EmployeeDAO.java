package edu.kh.emp.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import edu.kh.emp.model.vo.Employee;

// DAO (Data Access Object, 데이터 접근 객체)
// 데이터베이스에 접근(연결)하는 객체 ---> JDBC 객체 생성


public class EmployeeDAO {

	// JDBC 객체 참조변수로 필드 선언(Class 내에서 공통 사용)
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	private PreparedStatement pstmt;
	/* PreparedStatement 
	 - Statement의 하위 클래스로 '향상된 기능' 제공
	 - ' ? '(placeholder, 위치홀더) 기호를 이용해서 SQL에 작성될 리터럴을 동적으로 제어
	 
	 - SQL ?기호에 추가되는 값은
	  숫자인 경우 ''없이, 문자열인 경우 ''가 자동으로 추가되어 대입 */
	
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "kh_ksh";
	private String pw = "kh1234";
	
	
	
	
	/** 전체 사원 정보 조회 DAO
	 * @return empList
	 */
	public List<Employee> selectAll() {
		// 1. 가장 먼저 '결과 저장용 변수'가 필요!
		List<Employee> empList = new ArrayList<>();
		
		try {
			// 2. JDBC 참조 변수에 객체 대입 -> conn, stmt, rs
			Class.forName(driver); 	 // Oracle JDBC Driver 객체를 메모리에 로드
			conn = DriverManager.getConnection(url, user, pw);  // Oracle JDBC Driver 객체를 이용해 DB접속
			
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서X') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE"
					+ " LEFT JOIN DEPARTMENT ON(DEPT_ID = DEPT_CODE)"
					+ " JOIN JOB USING(JOB_CODE)"
					+ " ORDER BY 1";
			
			stmt = conn.createStatement(); // conn을 이용하여 Statement객체 생성
			rs = stmt.executeQuery(sql);  // SQL을 수행하여 rs에 반환
			
			
			
			// 3. SQL을 수행해서 반환받은 결과 (ResultSet)에 한 행씩 접근해서 컬럼값 얻어오기
			while (rs.next()) {
				
				int empId = rs.getInt("EMP_ID");
				// EMP_ID 컬럼은 문자열 컬럼이지만 저장된 값들이 모두 숫자형태
				// -> DB에서 자동으로 형변환하여 가져옴
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle =rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				Employee emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
				empList.add(emp);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				// 4. JDBC객체 반환
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		// 5. 결과 반환
		return empList;
	}


	/** 사번이 일치하는 사원 정보 조회 DAO
	 * @param empId
	 * @return
	 */
	public Employee selectEmpId(int empId) {
		
		// 결과 저장용 변수 선언
		Employee emp = null; 
		// 조회 결과가 있으면 Employee객체를 생성하여 emp에 대입
		// 조회 결과가 없으면 emp에 아무 것도 대입 X
		
		try {
			
			// Connection 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// SQL 작성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE"
					+ " LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)"
					+ " JOIN JOB USING(JOB_CODE)"
					+ " WHERE EMP_ID = " + empId;
			
			// Statement 생성
			stmt = conn.createStatement();
			// SQL 수행 후 결과 반환(ResultSet)
			rs = stmt.executeQuery(sql);

			
			// 조회결과가 최대 1행인 경우 불필요한 조건 검사를 줄이기 위해 if문 권장
			if(rs.next()) {
				// int empId = rs.getInt("EMP_ID"); -> 파라미터와 같은 값
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle =rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				
				// 조회된 결과를 담을 Employee 객체 생성 후 결과 저장용 emp에 대입
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
	    		if(stmt != null) stmt.close();
	    		if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return emp;
	}


	/** 주민번호가 일치하는 사원 찾기
	 * @param empNo
	 * @return emp
	 */
	public Employee selectEmpNo(String empNo) {
		Employee emp = null;
		
		try {
			// Connection 생성
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			// sql 생성
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, NVL(DEPT_TITLE, '부서없음') DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE"
					+ " LEFT JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)"
					+ " LEFT JOIN JOB USING(JOB_CODE)"
					+ " WHERE EMP_NO = ?"
					+ " ORDER BY EMP_ID";
									// placeholder or 위치홀더
			
			// Statement 객체 사용시 순서
			// SQL 작성 -> Statement 객체 생성 -> SQL 수행 후 결과 반환(rs)
			
			// PreparedStatement 객체 사용시 순서
			// SQL 작성 -> PreparedStatement객체 생성 -> SQL 수행 후 결과 반환(rs) 
			// 단, PreparedStatement객체 생성 시 ?기호가 포함된 SQL을 매개변수로 사용하여 ?에 알맞는 값을 대입 
			
			// PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, empNo);
		
			
			rs = pstmt.executeQuery(); 
			// 여기서 executeQuery()에 sql은 위에서 이미 적재된 상태기 때문에 생략
			// PreparedStatement는 객체 생성 시 이미 SQL이 담겨져 있으니까 매개변수로 전달할 필요 없음
			// -> 실수로 SQL을 매개변수에 추가하면 ?기호에 작성했던 값이 모두 사라져 오류 발생!!
			
			if(rs.next()) {
				int empId = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				// String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String departmentTitle =rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				emp = new Employee(empId, empName, empNo, email, phone, departmentTitle, jobName, salary);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
	    		if(pstmt != null) pstmt.close();
	    		if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
				
		return emp;
	}


	/** 사원 정보 추가 DAO
	 * @param emp
	 * @return result (insert성공한 행의 개수를 반환)
	 */
	public int insertEmp(Employee emp) {
		int result = 0;
		
		try {
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false); // AutoCommit 비활성화
			
			/* DML수행 
			 - 트랜잭션에 DML 구문이 임시 저장
			    -> 정상적인 DML인지 판별하여 개발자가 직접 commit, rollback 수행
			 
			 - 단, Connection  객체 생성 시 AutoCommit이 활성화되어 있기 때문에
			 	이를 해제하는 코드 conn.setAutoCommit(false)를 추가!!!!
			 	
			 - AutoCommit 비활성화하더라도 conn.close()가 수행되면 자동으로 Commit수행
			   따라서 close() 수행 전 트랜잭션 제어 코드 작성이 필요!!!! */
			
	
			String sql = "INSERT INTO EMPLOYEE VALUES(?,?,?,?,?,?,?,?,?,?,?, SYSDATE, NULL, DEFAULT)";
																		//default :  퇴사 여부 컬럼의 기본값
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, emp.getEmpId());
			pstmt.setString(2, emp.getEmpName());
			pstmt.setString(3, emp.getEmpNo());
			pstmt.setString(4, emp.getEmail());
			pstmt.setString(5, emp.getPhone());
			pstmt.setString(6, emp.getDeptCode());
			pstmt.setString(7, emp.getJobCode());
	        pstmt.setString(8, emp.getSalLevel());
	        pstmt.setInt(9, emp.getSalary());
	        pstmt.setDouble(10, emp.getBonus());
	        pstmt.setInt(11, emp.getManagerId());
	        
	        result = pstmt.executeUpdate();
	        // executeUpdate() : SQL 수행 후에 결과 행 개수 반환
			// executeQuery() :  SELECT 수행 후 ResultSet반환
			
	        
	        // close() 수행 전 트랜잭션 제어 코드
	        if(result > 0) conn.commit();
	        else 		   conn.rollback();
	        
		} catch (Exception e) {
			e.printStackTrace();
		
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
			} 
		}
		
		return result;
	}


	/** 사번이 일치하는 사원의 정보를 수정 DAO
	 * @param emp
	 * @return
	 */
	public int updateEmp(Employee emp) {
		int result = 0;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false); // 자동커밋 비활성화!!!!!! 
			
			String sql = "UPDATE EMPLOYEE SET "
					+ "EMAIL = ?, PHONE = ?, SALARY = ?"
					+ "WHERE EMP_ID = ?";			
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, emp.getEmail());
			pstmt.setString(2, emp.getPhone());
			pstmt.setInt(3, emp.getSalary());
			pstmt.setInt(4, emp.getEmpId());
			
			result = pstmt.executeUpdate(); // 반영된 행의 개수를 int result에 대입
			
			if (result == 0) conn.rollback(); // 반영된 행 없으므로 실패
			else			 conn.commit();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null) conn.close();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}


	/** 사원 정보 삭제 DAO
	 * @param emp
	 * @return
	 */
	public int deleteEmp(int empId) {
		int result = 0;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false);
			
			String sql = "DELETE FROM EMPLOYEE WHERE EMP_ID = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, empId);
			
			result = pstmt.executeUpdate();
			
			if(result > 0) conn.commit();
			else		   conn.rollback();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return result;
	}


	/**  입력 받은 부서와 일치하는 모든 사원 정보 
	 * @param deptTitle
	 * @return
	 */
	public List<Employee> selectDeptEmp(String deptTitle) {
		List<Employee> empList = new ArrayList<>();
		
		try { 
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false);
			
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE"
					+ " LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID)"
					+ " JOIN JOB USING(JOB_CODE)"
					+ " WHERE DEPT_TITLE = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, deptTitle);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int empId = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				Employee emp = new Employee(empId, empName, empNo, email, phone, deptTitle, jobName, salary);
				empList.add(emp);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt !=null) pstmt.close();
				if(conn !=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		return empList;
	}


	/** 입력 받은 급여 이상을 받는 모든 사원 정보
	 * @param inputSalary
	 * @return
	 */
	public List<Employee> selectSalaryEmp(int inputSalary) {
	
	List<Employee> empList = new ArrayList<>();
		try { 
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			conn.setAutoCommit(false);
			
			String sql = "SELECT EMP_ID, EMP_NAME, EMP_NO, EMAIL, PHONE, DEPT_TITLE, JOB_NAME, SALARY"
					+ " FROM EMPLOYEE"
					+ " LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID)"
					+ " JOIN JOB USING(JOB_CODE)"
					+ " WHERE SAlARY > ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, inputSalary);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				int empId = rs.getInt("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String empNo = rs.getString("EMP_NO");
				String email = rs.getString("EMAIL");
				String phone = rs.getString("PHONE");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				int salary = rs.getInt("SALARY");
				
				Employee emp = new Employee(empId, empName, empNo, email, phone, deptTitle, jobName, salary);
				empList.add(emp);
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt !=null) pstmt.close();
				if(conn !=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		return empList;
	    

	}


	/** 부서별 급여 합계
	 * @return
	 */
	public Map<String, Integer> selectDeptTotalSalary() {
		
		Map<String, Integer> dept = new HashMap<>();
		
		
		try { 
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			String sql = "SELECT DEPT_TITLE, SUM(SALARY) SUM"
					+ " FROM EMPLOYEE "
					+ " JOIN DEPARTMENT ON (DEPT_ID = DEPT_CODE)"
					+ " GROUP BY DEPT_TITLE";

			stmt =conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				String deptTitle = rs.getString("DEPT_TITLE");
				int sum = rs.getInt("SUM");
				
				dept.put(deptTitle, sum);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt !=null) pstmt.close();
				if(conn !=null) conn.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}			
		return dept;
	}


	/** 직급별 급여 평균
	 * @return
	 */
	public Map<String, Double> selectJobAvgSalary() {
		Map<String, Double> job = new LinkedHashMap<>();
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, pw);
			
			String sql = "SELECT JOB_NAME, AVG(SALARY) AVG"
					+ " FROM EMPLOYEE"
					+ " JOIN JOB USING (JOB_CODE)"
					+ " GROUP BY JOB_NAME " ;
			
			stmt =conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				
				String jobName = rs.getString("JOB_NAME");
				double avg = rs.getDouble("AVG");
				
				job.put(jobName, avg);
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		 
		
		
		
		return job;
	}
}
