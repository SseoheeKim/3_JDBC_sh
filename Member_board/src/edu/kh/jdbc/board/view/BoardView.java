package edu.kh.jdbc.board.view;


import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.jdbc.board.model.service.BoardService;
import edu.kh.jdbc.member.vo.Member;

public class BoardView {
	
	private Scanner sc = new Scanner(System.in);
	private BoardService service = new BoardService();
	
	public void boardMenu(Member loginMember) {
		
		int input = -1;
		
		try {
			do {
				
				System.out.println("\n***[게시판 메뉴]***");
				System.out.println("1. 게시글 목록 조회(작성일 내림차순)"); 
				System.out.println("2. 게시글 상세 조회");
				System.out.println("3. 게시글 작성");
				System.out.println("4. 게시글 검색(제목, 내용, 제목+내용, 작성자)");
				System.out.println("0. 메인 메뉴로 이동");
				
				System.out.print("메뉴 선택 >> ");
				input = sc.nextInt();
				sc.nextLine();
				System.out.println();
				
				switch(input) {
				case 1 : break;
				case 2 : break;
				case 3 : break;
				case 4 : break;
				case 0 : System.out.println("\n메인메뉴로 이동합니다."); break;
				default : System.out.println("\n메뉴에 있는 번호를 선택하세요.");
					
				}
				
			
				/*
				 * 
				 * 게시판 기능 (Board View, Service, DAO, board-query.xml)
				 * 
				 * 1. 게시글 목록 조회(작성일 내림차순)
				 *      (게시글 번호, 제목, 작성자명, 작성일, 조회수, 댓글 수)
				 * 
				 * 2. 게시글 상세 조회(게시글 번호 입력 받음)
				 *    (게시글 번호, 제목, 내용, 작성자명, 작성일, 조회수, 
				 *     댓글 목록(작성일 오름차순 )
				 *     
				 *     
				 *     2-1. 게시글 수정 (자신의 게시글만)
				 *     2-2. 게시글 삭제 (자신의 게시글만)
				 *     
				 *     2-3. 댓글 작성
				 *     2-4. 댓글 수정 (자신의 댓글만)
				 *     2-5. 댓글 삭제 (자신의 댓글만)
				 * 
				 * 3. 게시글 작성(제목, 내용 INSERT) 
				 *    -> 작성 성공 시 상세 조회 수행
				 * 
				 * 4. 게시글 검색(제목, 내용, 제목+내용, 작성자)
				 * 
				 * */
			} while (input != 0);
			
		} catch (InputMismatchException e) {
			System.out.println("\n입력 형식이 올바르지 않습니다.");
			sc.nextLine();
		}
	}
	
	
	
	
	
	
}
