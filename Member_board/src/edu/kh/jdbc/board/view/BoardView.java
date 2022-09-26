package edu.kh.jdbc.board.view;


import java.util.ArrayList;

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
	
	/** 
	 * 게시판 기능 메뉴
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
				case 3 : insertBoard(); break; // 게시글 작성
				case 4 : searchBoard(); break; // 게시글 검색 -> 검색어가 포함되는 게시글의 목록을 조회
				case 0 : System.out.println("\n메인메뉴(로그인메뉴)로 이동합니다."); break;
				default : System.out.println("\n메뉴에 있는 번호를 선택하세요.");
				
				}
				
			} catch (InputMismatchException e) {
				System.out.println("\n입력 형식이 올바르지 않습니다.");
				sc.nextLine();
			}
			
		} while (input != 0); 
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
				System.out.printf("\n%d | %s[%d] | %s | %s | %d\n" ,
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
	 * (게시글 번호 입력 받아서) 게시글 상세 조회
	 */
	private void selectBoard() {
		System.out.println("\n[게시글 상세 조회]\n");
		try {
			System.out.print("게시글 번호를 입력하세요. ");
			int boardNo = sc.nextInt();
			sc.nextLine();
			
			// 게시글 상세조회 출력
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
	             

	             subBoardMenu(board);
	             
	          } else {
	             System.out.println("[\n해당 번호의 게시글이 존재하지 않습니다.]\n");
	          }
					
		} catch(Exception e) {
			System.out.println("\n[게시글 상세 조회 중 예외 발생]");
			e.printStackTrace();
		}
	}
	
	
	/** 게시글 상세 조회시 출력되는 서브 메뉴
	 * @param board
	 */
	private void subBoardMenu(Board board) {
		
		try {
			System.out.println("\n***자유롭게 댓글을 남겨주세요****\n");
			System.out.println("1) 댓글 등록");
			System.out.println("2) 댓글 수정");
			System.out.println("3) 댓글 삭제");
			
			// 로그인한 회원과 게시글 작성자가 같은 경우에 출력되는 메뉴
			if(MainView.loginMember.getMemberNo() == board.getMemberNo()) {
				System.out.println("4) 게시글 수정");
				System.out.println("5) 게시글 삭제");
			}
			
			System.out.println("0) 게시판으로 돌아가기");
			
			System.out.print("\n서브 메뉴 선택 >> ");
			int input = sc.nextInt();
			sc.nextLine();
			
			int memberNo = MainView.loginMember.getMemberNo();
			
			switch(input) {
			case 1: insertComment(board.getBoardNo(), memberNo); break;
			case 2: updateComment(board.getCommentList(), memberNo); break;
			case 3: deleteComment(board.getCommentList(), memberNo); break;
			case 0: System.out.println("\n게시판으로 돌아갑니다.\n"); break;
			case 4: case 5: 
				if(MainView.loginMember.getMemberNo() == board.getMemberNo()) {
					// 4 또는 5 입력한 회원이 게시글 작성자인 경우
					// 유지보수를 위해 if-else if 대신 독립적 사용
					if(input == 4) {
						// 게시글 수정 호출
						updateBoard(board.getBoardNo());
					} 
					
					if(input == 5) { 
						// 게시글 삭제 호출
						deleteBoard(board.getBoardNo());

					}
					
					break; // switch 종료
				}
			default : System.out.println("\n서브 메뉴에 작성된 번호만 입력해주세요.\n");
			}
			
			// 댓글 등록, 수정, 삭제를 선택했을 때 각각의 서비스 서비스 메서드 종료 후
			// 다시 서브메뉴 메서드를 호출 -> 게시글 상세조회 화면 보여주기(재귀호출)
			if(0 > input && input < 5) {
				try {
		               board = bService.selectBoard(board.getBoardNo(), MainView.loginMember.getMemberNo());
		   
		               System.out.println(" --------------------------------------------------------");
		               System.out.printf("글번호 : %d | 제목 : %s\n", board.getBoardNo(), board.getBoardTitle());
		               System.out.printf("작성자ID : %s | 작성일 : %s | 조회수 : %d\n", 
		                     board.getMemberName(), board.getCreateDate().toString(), board.getReadCount());
		               System.out.println(" --------------------------------------------------------");
		               System.out.println(board.getBoardContent());
		               System.out.println(" --------------------------------------------------------");
		   
		            
		               // 댓글 목록
		               if(!board.getCommentList().isEmpty()) {
		                  for(Comment c : board.getCommentList()) {
		                     System.out.printf("댓글번호: %d   작성자: %s  작성일: %s\n%s\n",
		                           c.getCommentNo(), c.getMemberName(), c.getCreateDate(), c.getCommentContent());
		                     System.out.println(" --------------------------------------------------------");
		                  }
		               }
		         }catch (Exception e) {
		               e.printStackTrace();
		         }
			}
				
				
		} catch(InputMismatchException e) {
			System.out.println("\n입력 형식이 올바르지 않습니다\n");
			sc.nextLine();
		}
	}



	
	/** 게시글 수정
	 * @param boardNo
	 */
	private void updateBoard(int boardNo) {
		try {
			
			System.out.println("\n[게시글 수정]");
			
			System.out.println("수정할 게시글 제목 : ");
			String boardTitle = sc.nextLine();
			
			System.out.println("수정할 게시글 내용 : ");
			String boardContent = sc.nextLine();
			
			Board board = new Board();
			board.setBoardNo(boardNo);
			board.setBoardTitle(boardTitle);
			board.setBoardContent(boardContent);
			
			int result = bService.updateBoard(board);
			
			if(result > 0) {
				System.out.println("\n게시글이 수정되었습니다.");
			} else {
				System.out.println("\n게시글 수정 실패");
			}
		} catch(Exception e) {
			System.out.println("\n게시글 수정 중 예외 발생");
			e.printStackTrace();
		}
		
	}

	
	/** 게시글 삭제
	 * @param boardNo
	 */
	private void deleteBoard(int boardNo) {
		try {
					
			System.out.println("\n[게시글 삭제]");
			
			System.out.println("\n정말 삭제하시겠습니까? (Y/N)");
			char ch = sc.next().toUpperCase().charAt(0);
			
			if(ch == 'Y') {
				int result = bService.deleteBoard(boardNo);
				
				if(result > 0) {
					System.out.println("\n게시글이 수정되었습니다.");
				} else {
					System.out.println("\n게시글 수정 실패");
				}
			} else {
				System.out.println("\n삭제를 취소합니다.");
			}
			
			
		} catch(Exception e) {
			System.out.println("\n게시글 수정 중 예외 발생");
			e.printStackTrace();
		}
		
		
	}



	/** 댓글 등록 
	 * @param boardNo
	 * @param memberNo
	 */
	private void insertComment(int boardNo, int memberNo) {
		try {
			System.out.println("\n[댓글 등록]\n");
			String content = inputContent(); 
			// 내용 입력은 중복되기 때문에 inputContent() 메서드를 따로 만들어서 사용
			// -> 코드 중복 제거
			
			// DB INSERT에 필요한 값을 하나의 Comment 객체에 담아 저장
			Comment comment = new Comment();
			comment.setCommentContent(content);
			comment.setBoardNo(boardNo);
			comment.setMemberNo(memberNo);
			
			// 댓글 삽입 서비스 호출 후 결과 반환 받기
			int result = cService.insertComment(comment);
			
			if(result > 0) {
				System.out.println("\n댓글이 추가되었습니다.\n");
				
			} else {
				System.out.println("\n댓글 등록 실패\n");
			}
			
		} catch(Exception e) {
			System.out.println("\n댓글 등록 중 예외 발생\n");
			e.printStackTrace();
		}
	}
	
	
	
	/** 댓글 수정 
	 * @param commentList
	 * @param memberNo
	 */
	private void updateComment(List<Comment> commentList, int memberNo) {
		// 댓글 번호를 입력받아서 해당 댓글이 commentList에 있는지 검사
		// List안에 존재하면 해당 댓글이 로그인한 회원이 작성한 글인지 검사
		
		try {
			System.out.println("\n[댓글 수정]\n");
			System.out.println("수정할 댓글 번호 : ");
			int commentNo = sc.nextInt();
			sc.nextLine();
			
			boolean flag = true;
			
			for(Comment c : commentList) {
				if(c.getCommentNo() == commentNo) {
					flag = false;
					
					if(c.getMemberNo() == memberNo) {
						
						System.out.println("\n수정할 내용 : ");
						String content = inputContent();
						
						int result = cService.updateComment(commentNo, content);
						
						if(result > 0) {
							System.out.println("\n댓글이 수정되었습니다.\n");
						} else {
							System.out.println("\n댓글 수정 실패\n");
						}
						
					} else {
						System.out.println("\n자신의 댓글만 수정할 수 있습니다.\n");
					}
					break; 
				}
			} // for문 종료
			
			if (flag) {
				System.out.println("\n입력하신 번호와 일치하는 댓글이 없습니다.\n");
			}
			
		} catch (Exception e) {
			System.out.println("\n댓글 수정 중 예외 발생\n");
			e.printStackTrace();
		}
	}
	
	
	
	
	private void deleteComment(List<Comment> commentList, int memberNo) {
		
		// 댓글 번호를 입력받아서 해당 댓글이 commentList에 있는지 검사
		// List안에 존재하면 해당 댓글이 로그인한 회원이 작성한 글인지 검사
		
		try {
			System.out.println("\n[댓글 삭제]\n");
			System.out.println("삭제할 댓글 번호 : ");
			int commentNo = sc.nextInt();
			sc.nextLine();
			
			boolean flag = true;
			
			for(Comment c : commentList) {
				if(c.getCommentNo() == commentNo) {
					flag = false;
					
					if(c.getMemberNo() == memberNo) {
						
						System.out.print("댓글을 정말로 삭제하시겠습니까?(Y/N)");
						char input = sc.next().toUpperCase().charAt(0);
						
						if( input == 'Y' ) {
							int result = cService.deleteComment(commentNo);
							
							if(result > 0) {
								System.out.println("\n댓글이 삭제되었습니다.\n");
							} else {
								System.out.println("\n댓글 삭제 실패\n");
							}
						} else {
							System.out.println("\n댓글 삭제가 취소되었습니다.");
						}
						
					} else {
						System.out.println("\n자신의 댓글만 삭제할 수 있습니다.\n");
					}
					
					
					break; 
				}
			}  // for문 종료
			
			if (flag) {
				System.out.println("\n입력하신 번호와 일치하는 댓글이 없습니다.\n");
			}
		
		} catch (Exception e) {
				System.out.println("\n댓글 삭제 중 예외 발생\n");
				e.printStackTrace();
		}
	}		
	
	
	/**
	 * 내용 입력 
	 */
	private String inputContent() {
		String content = ""; // 빈 문자열 (참조하는 것은 있지만 내용 없음)
		String input = null; // 참조하는 객체가 없음
		
		System.out.println("입력 종료 시 ($exit) 입력");
		
		while(true) {
			input = sc.nextLine();
			if(input.equals("$exit")) {
				break;
			}
			content += input + "\n"; // 입력된 댓글 내용을 content("")에 누적
		}
		
		return content;
	}
	
		
	
	/**
	 * 게시글 등록 (제목, 내용 INSERT) 
	 * -> 작성 성공 시 상세 조회 수행(현재 작성한 게시글의 '시퀀스 넘버' 사용)
	 * @param memberNo 
	 */
	private void insertBoard() {
		
		try {
			System.out.println("\n[새로운 게시글 등록]");
			
			System.out.print("제목 : ");
			String boardTitle = sc.nextLine();
			
			System.out.print("내용 : ");
			String boardContent = inputContent();
			
			// Board객체에 제목, 내용, 회원번호를 담아서 서비스에 전달
			
			Board board =  new Board();
			board.setBoardTitle(boardTitle);
			board.setBoardContent(boardContent);
			board.setMemberNo(MainView.loginMember.getMemberNo());
			
			int result = bService.insertBoard(board);
			// 0 또는 생성된 게시물 번호가 반환
			
			if(result > 0) {
				System.out.println("\n\"새 게시글이 업로드 되었습니다.\"");
				// 게시글 상세조회 출력
				Board b = bService.selectBoard(result, MainView.loginMember.getMemberNo());
															// -> 로그인 한 자신의 게시글을 조회했을 때 조회수 증가 방지
				
		        if (b != null) {
		            System.out.println("--------------------------------------------------------");
		            System.out.printf("글번호 : %d \n제목 : %s\n", b.getBoardNo(), b.getBoardTitle());
		            System.out.printf("작성자 : %s | 작성일 : %s  \n조회수 : %d\n", 
		                  b.getMemberName(), b.getCreateDate(), b.getReadCount());
		            System.out.println("--------------------------------------------------------\n");
		            System.out.println(b.getBoardContent());
		            System.out.println("\n--------------------------------------------------------");

		          
		             // 댓글 목록
		             if(!b.getCommentList().isEmpty()) {
		                for(Comment c : b.getCommentList()) {
		                   System.out.printf("댓글번호: %d   작성자: %s  작성일: %s\n%s\n",
		                         c.getCommentNo(), c.getMemberName(), c.getCreateDate(), c.getCommentContent());
		                   System.out.println(" --------------------------------------------------------");
		                }
		             }
		             

		             subBoardMenu(b);
		             
		          } else {
		             System.out.println("[\n해당 번호의 게시글이 존재하지 않습니다.]\n");
		          }
				
			} else {
				System.out.println("\n\"게시글 업로드 실패\"");
			}
			
			
		} catch (Exception e) {
			System.out.println("\n게시글 작성 중 예외 발생");
			e.printStackTrace(); // 예외가 발생한 위치 출력
		}
		
	}
	
	
	
	/**
	 * 게시글 검색(제목, 내용, 제목+내용, 작성자)
	 */
	private void searchBoard() {
		
		try {
			System.out.println("\n[게시글 검색]");
			
			System.out.println("1) 제목");
			System.out.println("2) 내용");
			System.out.println("3) 제목 + 내용");
			System.out.println("4) 작성자");
			
			System.out.print("\n검색 조건 선택 :");
			int condition = sc.nextInt();
			sc.nextLine();
			
			if(1 <= condition  && condition <= 4) {
				System.out.println("검색어 입력 : ");
				String query = sc.nextLine();
				
				// 검색 서비스 호출하고 결과 반환
				List<Board> boardList = bService.searchBoard(condition, query);
				
				if(boardList.isEmpty()) {
					System.out.println("\n검색결과가 없습니다.");
				} else {
					
					for(Board b : boardList) {
					System.out.printf("\n%d | %s[%d] | %s | %s | %d\n" ,
									b.getBoardNo(),  b.getBoardTitle() , b.getCommentCount(), 
									b.getMemberName(), b.getCreateDate(), b.getReadCount());
					}	
				}
				
			} else {
				System.out.println("\n1 ~ 4번 사이의 숫자를 입력해 주세요");
			}
		
		} catch(Exception e) {
			System.out.println("\n게시물 검색 중 예외 발생\n");
			e.printStackTrace();
		}
	}
}




























