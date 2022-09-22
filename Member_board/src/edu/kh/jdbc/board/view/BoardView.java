package edu.kh.jdbc.board.view;


import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.board.model.service.BoardService;
import edu.kh.jdbc.board.model.service.CommentService;
import edu.kh.jdbc.board.model.vo.Board;
import edu.kh.jdbc.board.model.vo.Comment;
import edu.kh.jdbc.main.view.MainView;
import edu.kh.jdbc.member.vo.Member;
import oracle.jpub.runtime.MutableArray;


public class BoardView {
	
	public static Scanner sc = new Scanner(System.in);
	private BoardService bService = new BoardService();
	private CommentService cService = new CommentService();
	
	
	/** 게시판 기능 메뉴
	 * @param loginMember
	 */
	public void boardMenu() {
		int input = -1;

		do {
			try {
				System.out.println("\n***[게시판 기능]***\n");
				System.out.println("1. 게시글 목록 조회"); 
				System.out.println("2. 게시글 상세 조회(댓글 기능)");
				System.out.println("3. 게시글 작성");
				System.out.println("4. 게시글 검색");
				System.out.println("0. 로그인 메뉴로 이동");
				
				System.out.print("\n메뉴 선택 >> ");
				input = sc.nextInt();
				sc.nextLine(); // 입력버퍼의 개행문자 제거
				
				System.out.println();
				
				switch(input) {
				case 1 : selectAllBoard(); break; // 게시글 목록 조회 
				case 2 : selectBoard(); break; // 게시글 상세 조회
				case 3 : break;
				case 4 : break;
				case 0 : System.out.println("\n메인메뉴로 이동합니다."); break;
				default : System.out.println("\n메뉴에 있는 번호를 선택하세요.");
				
				}
				
			} catch (InputMismatchException e) {
				System.out.println("\n입력 형식이 올바르지 않습니다.");
				sc.nextLine();
			}
			
			
		} while (input != 0); 
		
		
		
				/*
				 * 
				 
				 
				 *     
				 *     2-3. 댓글 작성
				 *     2-4. 댓글 수정 (자신의 댓글만)
				 *     2-5. 댓글 삭제 (자신의 댓글만)
				 * 
				 *	   // 자신이 작성한 글 일때만 수정/삭제 키워드 노출
				 *     2-1. 게시글 수정 (자신의 게시글만)
				 *     2-2. 게시글 삭제 (자신의 게시글만)
				 *     
				 * 3. 게시글 작성(제목, 내용 INSERT) 
				 *    -> 작성 성공 시 상세 조회 수행
				 * 
				 * 4. 게시글 검색(제목, 내용, 제목+내용, 작성자)
				 * 
				 * */
	
	}



	
	/**
	 * 게시글 목록 조회(작성일 내림차순)
	 */
	private void selectAllBoard() {
		System.out.println("\n[게시글 목록 조회]\n");
		try {
			
			List<Board> boardList = bService.selectAllBoard();
			// DAO에서 생성된 newArrayList<>(); 구문으로 인해 반환되는 조회결과는 null이 될 수 없다!
			
			if(boardList.isEmpty()) {
				System.out.println("게시글이 존재하지 않습니다.");
			} else {
				for(Board b : boardList) {
				System.out.printf(" %3d  %s[%d]    %s     %s    %d  \n" ,
								b.getBoardNo(),  b.getBoardTitle() , b.getCommentCount(), 
								b.getMemberName(), b.getCreateDate(), b.getReadCount());
				}	
			}
			
		}catch(Exception e) {
			System.out.println("\n[게시글 목록 조회 중 예외 발생]");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 게시글 상세 조회(게시글 번호 입력 받기)
	 */
	private void selectBoard() {
		System.out.println("\n[게시글 상세 조회]\n");
		try {
			System.out.print("게시글 번호를 입력하세요. ");
			int boardNo = sc.nextInt();
			sc.nextLine();
			
			Board board = bService.selectBoard(boardNo, MainView.loginMember.getMemberNo());
														// -> 로그인 한 자신의 게시글을 조회했을 때 조회수 증가 방지
			
	        if (board != null) {
	            System.out.println("--------------------------------------------------------");
	            System.out.printf("글번호 : %d \n제목 : %s\n", board.getBoardNo(), board.getBoardTitle());
	            System.out.printf("작성자 : %s | 작성일 : %s  \n조회수 : %d\n", 
	                  board.getMemberName(), board.getCreateDate(), board.getReadCount());
	            System.out.println("--------------------------------------------------------\n");
	            System.out.println(board.getBoardContent());
	            System.out.println("\n--------------------------------------------------------");

	          
	             // 댓글 목록
	             if(!board.getCommentList().isEmpty()) {
	                for(Comment c : board.getCommentList()) {
	                   System.out.printf("댓글번호: %d   작성자: %s  작성일: %s\n%s\n",
	                         c.getCommentNo(), c.getMemberName(), c.getCreateDate(), c.getCommentContent());
	                   System.out.println(" --------------------------------------------------------");
	                }
	             }
	             
	             // 댓글 등록, 수정, 삭제
	             // 수정/삭제 메뉴 
	             //	             subBoardMenu(board);
	             
	             
	          } else {
	             System.out.println("[\n해당 번호의 게시글이 존재하지 않습니다.]\n");
	          }
					
			
			
		} catch(Exception e) {
			System.out.println("\n[게시글 상세 조회 중 예외 발생");
			e.printStackTrace();
		}
	}
}

