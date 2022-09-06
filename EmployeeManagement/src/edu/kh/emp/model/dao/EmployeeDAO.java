package edu.kh.emp.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.emp.model.vo.Employee;

// DAO (Data Access Object, 데이터 접근 객체)
// 데이터베이스에 접근(연결)하는 객체 ---> JDBC 객체 생성
public class EmployeeDAO {

	// JDBC 객체 참조변수로 필드 선언(Class 내에서 공통 사용)
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "kh_ksh";
	private String pw = "kh1234";
	
	
	/** 전체 사원 정보를 조회하는 DAO
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
					+ " JOIN JOB USING(JOB_CODE)";
			
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
}
