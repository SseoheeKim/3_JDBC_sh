package edu.kh.jdbc1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc1.model.vo.Employee;

public class JDBSExample5 {
	public static void main(String[] args) {
		// 입사일("2022-09-06")을 입력받아
		// 입력받은 값보다 먼저 입사한 사람의 
		// 이름, 입사일, 성별(M,F) 조회
		
		Scanner sc = new Scanner(System.in);
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			System.out.print("입사일 입력(YYYY-MM-DD) >> ");
			String inputDate = sc.next(); // 띄어쓰기가 없으므로 next()로 입력받기 가능
			System.out.println();
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String user = "kh_ksh";
			String pw = "kh1234";
			
			conn = DriverManager.getConnection(url, user, pw);

								
			String sql = "SELECT EMP_NAME, "
					+ " TO_CHAR(HIRE_DATE, 'YYYY\"년\" MM\"월\" DD\"일\"') 입사일, "
					+ "	DECODE(SUBSTR(EMP_NO,8,1), '1', 'M', '2', 'F') 성별 "
					+ " FROM EMPLOYEE"
					+ " WHERE HIRE_DATE < TO_DATE('"+inputDate+"')"
					+ " ORDER BY 2";
			// \" -> escape문자. 문자열 내부에 쌍 따옴표 작성시 '\ 백 슬래시'			
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			List<Employee> list = new ArrayList<>();
			
		
			while (rs.next()) {
				String empName = rs.getString("EMP_NAME");
				String hireDate = rs.getString("입사일");
				String gender = rs.getString("성별");
				
				list.add(new Employee(empName, hireDate, gender));
			}
			
		
			if(list.isEmpty()) { 
				System.out.println("조회되는 정보가 없습니다.");
			} else {
				for( Employee emp : list) {
					System.out.println("사원명 : " + emp.getEmpName() + " / 입사일 : " + emp.getHireDate() + " / 성별 " + emp.getGender() );
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
