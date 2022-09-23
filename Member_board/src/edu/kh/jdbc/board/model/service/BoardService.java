package edu.kh.jdbc.board.model.service;

import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.board.model.dao.BoardDAO;
import edu.kh.jdbc.board.model.dao.CommentDAO;
import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.board.model.vo.Comment;

public class BoardService {
	
	// BoardDAO 객체 생성
	private BoardDAO dao = new BoardDAO();
	
	// CommentDAO 객체 생성 -> 상세 조회 시 댓글 목록 조회용도로 사용
	private CommentDAO cDao = new CommentDAO();

	private Connection conn;
	
	
	/** 게시글 목록 조회
	 * @return boardList
	 * @throws Exception
	 */
	public List<Board> selectAllBoard() throws Exception {
		conn = getConnection();
		
		List<Board> boardList = dao.selectAllBoard(conn);
		
		// select 수행 -> commit.rollback X
		close(conn);
		
		return boardList;
	}



	/** 게시글 상세 조회
	 * @param boardNo
	 * @param memberNo
	 * @return board
	 * @throws Exception
	 */
	public Board selectBoard(int boardNo, int memberNo) throws Exception {
		conn = getConnection();
		Board board = dao.selectBoard(conn, boardNo);
		
		
		if( board != null ) { // 게시글 존재하면
			
			List<Comment> commentList = cDao.selectCommentList(conn, boardNo); // 댓글 목록을 DAO를 통해 반환받아
			board.setCommentList(commentList); // 조회된 댓글 목록을 board.commentList에 저장
			
			
			// 단, 로그인 회원과 게시글 작성자가 다를 경우에만 조회수 증가
			if( memberNo != board.getMemberNo()) {
				int result = dao.increaseReadCount(conn, boardNo);
				
				if(result > 0) {
					commit(conn);
					// 미리 조회된 조회 수를 증가된 DB의 조회 수와 동일하게 동기화
					board.setReadCount(board.getReadCount() + 1);
					
				} else			rollback(conn);
			}
		} 
		
		close(conn);
		
		return board;
	}



	/** 게시글 수정 서비스
	 * @param board
	 * @return result 
	 * @throws Exception
	 */
	public int updateBoard(Board board) throws Exception {
		Connection conn = getConnection();
		int result = dao.updateBoard(conn, board);
		if(result>0) commit(conn);
		else 		rollback(conn);
		return result;
	}



	/** 게시글 삭제 서비스
	 * @param boardNo
	 * @return result
	 * @throws Exception
	 */
	public int deleteBoard(int boardNo) throws Exception {
		Connection conn = getConnection();
		int result = dao.deleteBoard(conn, boardNo);
		if(result>0) commit(conn);
		else 		rollback(conn);
		return result;
	}



	/** 새 게시글 작성 서비스
	 * @param board
	 * @return result
	 * @throws Exception
	 */
	public int insertBoard(Board board) throws Exception {
		Connection conn = getConnection();
		int result = dao.insertBoard(conn, board);
		if(result > 0) commit(conn);
		else 		rollback(conn);
		close(conn);
		return result;
	}



	
	

}
