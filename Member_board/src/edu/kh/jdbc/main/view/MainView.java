package edu.kh.jdbc.main.view;

import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.jdbc.main.model.service.MainService;
import edu.kh.jdbc.member.vo.Member;

// 메인 화면(입/출력)
public class MainView {
	private Scanner sc = new Scanner(System.in);
	private MainService service = new MainService();

	// 로그인 된 회원 정보를 저장한 객체를 참조하는 참조변수
	// -> 로그인X == null, 로그인O != null
	private Member loginMember = null;

	/**
	 * main메뉴 출력 메서드
	 */
	public void mainMenu() {
		int input = -1;

		do {
			try {

				if (loginMember == null) {

					System.out.println("\n*****회원제 게시판 프로그램 *****");
					System.out.println("1. 로그인");
					System.out.println("2. 회원가입");
					System.out.println("0. 프로그램 종류");

					System.out.print("\n메뉴선택 >> ");
					input = sc.nextInt();
					sc.nextLine();
					System.out.println();

					switch (input) {
					case 1: login(); break; // 로그인
					case 2: signUp(); break; // 회원가입
					case 0: System.out.println("\n[프로그램 종료]"); break;
					default: System.out.println("\n[메뉴에 작성된 번호만 입력해주세요.]");
					}
				} else { // 로그인이 되었을 때
					System.out.println("\n***[ 로그인 메뉴]***");
					System.out.println("1. 회원 기능");
					System.out.println("2. 게시판 기능");
					System.out.println("0. 로그아웃");
					System.out.println("99. 프로그램 종료");
					
					System.out.print("메뉴 선택 >> ");
					input = sc.nextInt();
					
					switch(input) {
					case 1 : break;
					case 2 : break;
					case 0 : loginMember = null; // loginMember == null (참조하는 객체가 없으면 로그아웃)
							 System.out.println("\n[로그아웃 되었습니다.]");
							 input = -1; // do-while문 종료 방지 플래그
							 break; 
					case 99 : System.out.println("[프로그램 종료]"); 
							  // input = 0; // do-while문 조건식을 false로 완전 종료
							  System.exit(0); // JVM 종료, 매개변수는 0, 0이 아닌 상태 코드는 비정상적인 종료
							  break;
					default: System.out.println("\n[메뉴에 작성된 번호만 입력해주세요.]");
					}
					
				}
				
			} catch (InputMismatchException e) {
				System.out.println("\n[입력 형식이 올바르지 않습니다.]");
				sc.nextLine();
			}

		} while (input != 0);
	}


	/**
	 * 로그인 화면
	 */
	private void login() {
		System.out.println("[로그인]");
		System.out.print("회원 아이디 : ");
		String memberId = sc.next();
		System.out.print("비밀번호 : ");
		String memberPw = sc.next();
		
		try {
			
			// 로그인 서비스 호출 후, 조회 결과를 loginMember에 저장
			loginMember = service.login(memberId, memberPw);
			
			if (loginMember != null) { // 로그인 성공 시
				System.out.println("\n" +loginMember.getMemberName() + " 님, 환영합니다!");
			} else { // 로그인 실패 시
				System.out.println("[아이디 또는 비밀번호가 일치하지 않습니다.]");
			}
		} catch (Exception e) {
			System.out.println("\n[로그인 중 에러가 발생하였습니다.");
			e.printStackTrace();
		}
	}


	/**
	 * 회원 가입 화면
	 */
	private void signUp() {
		System.out.println("[회원 가입]");
		String memberId = null;

		String memberPw1 = null;
		String memberPw2 = null; // 비밀번호 확인용

		String memberName = null;
		String memberGender = null;

		// 아이디를 입력받아 중복이 아닐 때까지 반복
		try {

			while (true) {
				System.out.print("\n아이디 입력 >> ");
				memberId = sc.next();

				// 입력 받은 아이디를 매개변수로 전달하여 중복여부를 검사하는 서비스를 호출하고 반환받기
				int result = service.idDupCheck(memberId);

				if (result == 0) {
					System.out.println("\n[사용 가능한 아이디 입니다.]");
					break;
				} else {
					System.out.println("\n[이미 존재하는 아이디 입니다.]");
				}
			}

			// 비밀번호 입력
			// 비밀번호/비밀번호 확인이 일치할 때까지 무한 반복
			while (true) {
				System.out.print("비밀번호 : ");
				memberPw1 = sc.next();

				System.out.print("비밀번호 확인 : ");
				memberPw2 = sc.next();

				if (memberPw1.equals(memberPw2)) {
					System.out.println("[일치합니다.]");
					break;
				} else {
					System.out.println("[비밀번호가 일치하지 않습니다. 다시 입력하세요.]");
				}
			}

			// 이름 입력
			System.out.print("이름 입력 : ");
			memberName = sc.next();

			// 성별 입력 시 M또는 F가 입력될 때까지 무한 반복
			while (true) {
				System.out.print("성별 입력 : ");
				memberGender = sc.next().toUpperCase(); // 입력받자마자 대문자로 변경

				if (memberGender.equals("M")
						|| memberGender.equals("F") /* || memberGender.equals("m") || memberGender.equals("f") */) {
					break;
				} else {
					System.out.println("[M 또는 F로 입력하세요.]");
				}
			}

			// -- 아이디, 비밀번호, 이름, 성별 입력 완료 --
			// --> 하나의 VO에 담아서 서비스 호출 후 반환 받기
			Member member = new Member(memberId, memberPw2, memberName, memberGender);

			int result = service.signUp(member);

			// 서비스 처리 결과에 따른 출력 화면 제어
			if (result > 0) {
				System.out.println("\n[***회원 가입 성공***]");
			} else {
				System.out.println("\n[회원 가입 실패]");
			}

		} catch (Exception e) {
			System.out.println("\n 회원 가입중 예외 발생");
			e.printStackTrace();
		}
	}

}
