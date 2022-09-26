package edu.kh.jdbc.board.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.kh.jdbc.board.model.vo.Board;

public class BoardDAO {
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	private Properties prop;
	
	public BoardDAO() {
		try {
			prop = new Properties();
			prop.loadFromXML(new FileInputStream("board-query.xml"));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	/** 게시글 목록 조회
	 * (게시글 번호, 제목, 작성자명, 작성일, 조회수, 댓글 수)
	 * @param conn
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> selectAllBoard(Connection conn) throws Exception {
		List<Board> boardList = new ArrayList<>();
		
		try {
			
			String sql = prop.getProperty("selectAll");
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			
			while(rs.next()) {
				int boardNo = rs.getInt("BOARD_NO");
				String boardTitle = rs.getString("BOARD_TITLE");
				String boardContent = rs.getString("BOARD_CONTENT");
				String memberName = rs.getString("MEMBER_NM");
				int readCount = rs.getInt("READ_COUNT");
				String createDate = rs.getString("CREATE_DT");
				int commentCount = rs.getInt("COMMENT_COUNT");
				
				Board board = new Board();
				board.setBoardNo(boardNo);
				board.setBoardTitle(boardTitle);
				board.setBoardContent(boardContent);
				board.setMemberName(memberName);
				board.setReadCount(readCount);
				board.setCreateDate(createDate);
				board.setCommentCount(commentCount);
				
				boardList.add(board);
			}
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return boardList;
	}


	/** 게시글 상세 조회 
	 * @param conn
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board selectBoard(Connection conn, int boardNo) throws Exception {
		Board board = new Board(); // 객체 생성했므로 null이 아님
		
		try {
			String sql = prop.getProperty("selectBoard");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			rs = pstmt.executeQuery();
			
			
			if(rs.next()) {
				
				board.setBoardNo( rs.getInt("BOARD_NO") );
				board.setBoardTitle( rs.getString("BOARD_TITLE"));
				board.setBoardContent(rs.getString("BOARD_CONTENT"));
				board.setMemberNo(rs.getInt("MEMBER_NO"));
				board.setMemberName( rs.getString("MEMBER_NM"));
				board.setReadCount( rs.getInt("READ_COUNT") );
				board.setCreateDate( rs.getString("CREATE_DT") );
				
			}
			
			
		} finally {
			close(rs);
			close(pstmt);
		}
		return board;
	}


	/** 조회수 증가 DAO
	 * @param conn
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int increaseReadCount(Connection conn, int boardNo) throws Exception {
		int result = 0;
		
		try {								
			String sql = prop.getProperty("increseReadCount");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return result;
	}


	/** 게시글 수정 DAO
	 * @param conn
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Connection conn, Board board) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("updateBoard");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardTitle());
			pstmt.setString(2, board.getBoardContent());
			pstmt.setInt(3, board.getBoardNo());
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
			
		}
		return result;
	}


	/** 게시글 삭제 DAO
	 * @param conn
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int deleteBoard(Connection conn, int boardNo) throws Exception {
		int result = 0;
		try {
			String sql = prop.getProperty("deleteBoard");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		return result;
	}


	
	/** 새 게시글 등록 DAO
	 * @param conn
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Connection conn, Board board) throws Exception {
		int result = 0;
		// try - finally에서 finally 구문 사용을 위해서 결과값 저장변수 선언
		// 예외의 발생과 무관하게 result값을 반환하기 위함
		
		try {
			String sql = prop.getProperty("insertBoard");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getBoardNo());
			pstmt.setString(2, board.getBoardTitle());
			pstmt.setString(3, board.getBoardContent());
			pstmt.setInt(4, board.getMemberNo());
		
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}


	/** 게시글 번호 생성 DAO
	 * @param conn
	 * @return boardNo
	 * @throws Exception
	 */
	public int nextBoardNo(Connection conn) throws Exception {
		int boardNo = 0;
		
		try {
			
			String sql = prop.getProperty("nextBoardNo");
			
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql); //select 수행
			
			if(rs.next()) { // select 조회 결과 1행
				boardNo = rs.getInt(1);
				// rs.getInt("SEQ_BOARD_NO.NEXTVAL") : 에러 -> 조회 되는 컬럼명 NEXTVAL
			}
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return boardNo;
	}


	/** 게시물 검색 DAO
	 * @param conn
	 * @param condition
	 * @param query
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> searchBoard(Connection conn, int condition, String query) throws Exception {
		List<Board> boardList = new ArrayList<>();
		try {
			
			String sql = prop.getProperty("searchBoard1")+prop.getProperty("searchBoard2_"+condition)+prop.getProperty("searchBoard3");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, query);
			if(condition  == 3) pstmt.setString(2, query);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int boardNo = rs.getInt("BOARD_NO");
				String boardTitle = rs.getString("BOARD_TITLE");
				String boardContent = rs.getString("BOARD_CONTENT");
				String memberName = rs.getString("MEMBER_NM");
				int readCount = rs.getInt("READ_COUNT");
				String createDate = rs.getString("CREATE_DT");
				int commentCount = rs.getInt("COMMENT_COUNT");
				
				Board board = new Board();
				board.setBoardNo(boardNo);
				board.setBoardTitle(boardTitle);
				board.setBoardContent(boardContent);
				board.setMemberName(memberName);
				board.setReadCount(readCount);
				board.setCreateDate(createDate);
				board.setCommentCount(commentCount);
				
				boardList.add(board);
			}
			
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return boardList;
	}

}
