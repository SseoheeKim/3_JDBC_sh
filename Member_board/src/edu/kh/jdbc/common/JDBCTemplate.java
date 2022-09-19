package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	/* JDBCTemplate
	 
	  - DB 연결(Connection 생성), 자동커밋 off,
	    트랜잭션 제어, JDBC객체 자원 반환(close)과 같은 
	    JDBC에서 반복 사용되는 코드들을 모아둔 클래스	
	  
	  
	  - 모든 필드, 메서드에 static 키워드를 사용해서
	    어디서든 공유될 목적으로 사용(클래스명.필드명 / 클래스명.메서드명으로 호출 가능) 
	    -> 별도 객체 생성 XXX !!!!
	    
      예)	new JDBCTemplate().getConnection()   X
	        JDBCTemplate().getConnection()       O
	*/

	
	private static Connection conn; 
	// 필드에 static키워드가 없으면 아래 static메서드에서 접근 불가
	// -> static 메서드에서 필드를 사용하려면 필드도 static 필드여야한다.
	
	
	/** DB 연결정보를 담고 있는 Connection 객체를 생성
	 * @return
	 */
	public static Connection getConnection() {
		try {
			
			// Connection객체가 없는 경우 새로운 Connection 객체 생성
			if(conn == null || conn.isClosed()) { // conn.isClosed() : Connection이 닫힌 상태면 true
				
				Properties prop = new Properties();
				prop.loadFromXML(new FileInputStream("driver.xml"));
				// XML파일에 작성된 내용이 Properties 객체에 모두 저장
				
				// Properties 객체에서 얻어온 각각의 값을 String 변수에 저장
				String driver = prop.getProperty("driver");
				String url = prop.getProperty("url");
				String user = prop.getProperty("user");
				String password = prop.getProperty("password");
				
				// Connection 객체 생성
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);
				
				// 개발자가 직접 트랜잭션을 제어할 수 있도록 자동 커밋 비활성화
				conn.setAutoCommit(false);
			}
			
		} catch(Exception e) {
			System.out.println("[Connection 생성 중 예외 발생]");
		}
		return conn;
	}	
	
	
	/** Connection 객체자원 반환 메서드
	 * @param conn
	 */
	public static void close(Connection conn) {
		try {
			// 전달받은 conn이 참조하는 Connection 객체가 존재하면서
			// Connection 객체가 close상태가 아니라면
			if (conn != null && !conn.isClosed()) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/** Statement,PreparedStatement 객체자원 반환 메서드
	 * (오버로딩, 다형성, 동적바인딩)
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		try {
			if (stmt != null && !stmt.isClosed()) stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/** ResultSet 객체자원 반환 메서드
	 * (오버로딩)
	 * @param rs
	 */
	public static void close(ResultSet rs){
		try {
			if (rs != null && !rs.isClosed()) rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/** 트랜잭션 Commit 메서드
	 * @param conn
	 */
	public static void commit(Connection conn) {
		try {
			// 연결된 Connection 객체일 경우만 Commit진행
			if(conn != null && !conn.isClosed()) conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/** 트랜잭션 Rollback 메서드
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
