package edu.kh.jdbc.member.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.main.view.MainView;
import edu.kh.jdbc.member.model.service.MemberService;
import edu.kh.jdbc.member.vo.Member;

/** 로그인 후 회원 메뉴 입/출력 클래스
 * @author user1
 */
public class MemberView {
	private Scanner sc = new Scanner(System.in);
	
	// 회원 관련 서비스를 제공하는 객체 생성
	private MemberService service = new MemberService();
	
	// 로그인 회원 정보 저장용 변수
	private Member loginMember = null;
	
	// 메뉴 번호를 입력받기 위한(do-while) 변수
	private int input = -1;
	
	/**
	 * 회원 기능 메뉴 메서드
	 * @param loginMember 
	 */
	public void memberMenu(Member loginMember) {
		// int input = -1; 필드로 이동 
		this.loginMember = loginMember; // 전달받은 로그인 회원 정보를 필드에 저장

		do {
			
			try {
				System.out.println("\n***[회원 기능 메뉴]***");
				System.out.println("1. 내 정보 조회");
				System.out.println("2. 회원 목록 조회(아이디, 이름, 성별)");
				System.out.println("3. 내 정보 수정(이름, 성별)");
				System.out.println("4. 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)");
				System.out.println("5. 회원 탈퇴");
				System.out.println("0. 메인 메뉴로 이동");
				
				System.out.print("메뉴 선택 >> ");
				input = sc.nextInt();
				sc.nextLine();
				System.out.println();
				
				switch(input) {
				case 1: selectMyinfo(); break;
				case 2: selectAll(); break;
				case 3: updateMyinfo(); break;
				case 4: updatePw(); break;
				case 5: secession(); break;
				case 0: System.out.println("메인 메뉴로 돌아갑니다."); break;
				default : System.out.println("메뉴의 번호를 입력하세요.");
				}
				
			} catch(InputMismatchException e) {
				System.out.println("입력형식이 올바르지 않습니다.");
				sc.nextLine();
			}
			
		} while (input != 0);
	}


	/**
	 * 회원 탈퇴
	 */
	private void secession() {
		try {
			System.out.println("\n[회원 탈퇴]");
			System.out.print("현재 비밀번호 : ");
			String memberPw = sc.next();
			
			while(true) {
				System.out.print("정말 탈퇴하시겠습니까? (Y/N) ");
				char ch = sc.next().toUpperCase().charAt(0);
				// 기본자료형은 비교연산자로 비교 가능
				
				if(ch == 'Y') {
					int result = service.secession(memberPw, loginMember.getMemberNo());
					
					if( result > 0 ) {
						System.out.println("\n정상적으로 탈퇴 처리되었습니다.");
						input = 0; // 탈퇴 성공 시 메인 메뉴로 이동
						// 메인메뉴로 이동했을 때 로그인상태가 되기 때문에 로그아웃까지 필요!
						MainView.loginMember = null; // 로그아웃
					} else {
						System.out.println("\n비밀번호가 일치하지 않습니다.");
					}
					
					break; // while문 종료
					
				} else if(ch == 'N') {
					System.out.println("\n탈퇴 취소되었습니다.");
					break;
				} else {
					System.out.println("\nY 또는 N만 입력해주세요.");
				}
				
			}
			
			
			
		} catch(Exception e) {
			System.out.println("\n회원 탈퇴 중 예외 발생");
		}
	}


	/**
	 * 비밀번호 변경(현재 비밀번호, 새 비밀번호, 새 비밀번호 확인)
	 */
	private void updatePw() {
		
		try {
			System.out.println("\n[비밀번호 변경하기]");
			System.out.print("현재 비밀번호 : ");
			String curruntPw = sc.next();
			
			String newPw1 = null;
			String newPw2 = null;
			while (true) {
				System.out.print("새 비밀번호 : ");
				newPw1 = sc.next();
			
				System.out.print("새 비밀번호 확인: ");
				newPw2 = sc.next();
				
				if(newPw1.equals(newPw2)) {
					break;
				} else {
					System.out.println("새 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
				}
			}
			
			// 서비스 호출 후 결과 반환 받기(현재 비밀번호, 새 비밀번호, 로그인 회원번호)
			int result = service.updatePw(curruntPw, newPw1, loginMember.getMemberNo());
			
			
			if (result > 0) {
				System.out.println("\n비밀번호가 변경되었습니다.");
			} else {
				System.out.println("\n현재 비밀번호가 일치하지 않습니다.");
			}
			
		} catch(Exception e) {
			System.out.println("\n비밀번호 변경 중 예외 발생");
			e.printStackTrace();
		}
	}


	
	/**
	 * 내 정보 수정(이름, 성별)
	 */
	private void updateMyinfo() {
		try {
			
			System.out.println("\n[회원 정보 수정]");
			System.out.print("이름 : ");
			String memberName = sc.nextLine();
			
			String memberGender = null;
			while(true) {
				System.out.print("성별(M/F) : ");
				memberGender = sc.next().toUpperCase();
				
				if(memberGender.equals("M") || memberGender.equals("F")) {
					break;
				} else {
					System.out.println("M 또는 F만 입력해주세요.");
				}
			}
			
			// 서비스로 전달할 멤버 객체 생성
			Member member = new Member();
			member.setMemberNo(loginMember.getMemberNo());
			member.setMemberName(memberName);
			member.setMemberGender(memberGender);
			
			// 회원 정보 수정 서비스 메서드 호출 후 결과 반환 받기
			int result = service.updateMyinfo(member);
		
			if(result > 0) {
				// loginMember(Java)에 저장된 값과 DB에 수정된 값을 동기화 하는 작업 필요!!
				loginMember.setMemberName(memberName);
				loginMember.setMemberGender(memberGender);
				System.out.println("\n회원 정보가 수정되었습니다");
			} else {
				System.out.println("\n회원 정보 수정이 실패했습니다.");
			}
			
		} catch(Exception e) {
			System.out.println("\n회원 정보 수정 중 예외 발생");
			e.printStackTrace();
		}
		
	}


	/**
	 * 회원 목록 조회(아이디, 이름, 성별)
	 */
	private void selectAll() {
		System.out.println("\n[회원 목록 조회]");
		
		// DB에서 회원 목록을 조회(탈퇴회원 제외), 가입일 내림차순으로 조회
		try {
			
			// 회원 목록 조회 서비스 호출 후 결과 반환 받기
			
			List<Member> memberList = service.selectAll();
			
			if(memberList.isEmpty()) {
				System.out.println("\n조회 결과가 없습니다.");
			} else {
				System.out.println(" 아이디 /  이름  / 성별 ");
				for ( Member m :memberList ) {
					System.out.printf(" %s / %s  /  %s \n", m.getMemberId(), m.getMemberName(), m.getMemberGender());
				}
			}
			
			
		} catch(Exception e) {
			System.out.println("\n회원 목록 조회 중 예외 발생");
			e.printStackTrace();
		}
		
		
	}


	/**
	 * 내 정보 조회
	 */
	private void selectMyinfo() {
		System.out.println("\n[내 정보 조회]");
		System.out.println("회원 번호 : " + loginMember.getMemberNo());
		System.out.println("아이디 : " + loginMember.getMemberId());
		System.out.println("이름 : " + loginMember.getMemberName());
		
		System.out.print("성별 : " );
		if (loginMember.getMemberGender().equals("M")) {
			System.out.println("남");
		} else {
			System.out.println("여");
		}
		
		System.out.println("가입일 : " + loginMember.getEnrollDate());
	}


	
	
}
