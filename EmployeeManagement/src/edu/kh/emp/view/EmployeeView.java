package edu.kh.emp.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.emp.model.dao.EmployeeDAO;
import edu.kh.emp.model.vo.Employee;

// 화면용 클래스( 입력(Scanner) / 출력(print()) 담당 )
public class EmployeeView {
	
	private Scanner sc = new Scanner(System.in);
	private EmployeeDAO  dao = new EmployeeDAO();
	
	public void displayMenu() {
		
		int input = 0;
		
		do {
			try {
				System.out.println("---------------------------------------------------------");
				System.out.println("-------- 사원 관리 프로그램 --------");
				System.out.println("1. 새로운 사원 정보 추가");
				System.out.println("2. 전체 사원 정보 조회");
				System.out.println("3. 사번이 일치하는 사원 정보 조회");
				System.out.println("4. 사번이 일치하는 사원 정보 수정");
				System.out.println("5. 사번이 일치하는 사원 정보 삭제");
				System.out.println("6. 입력 받은 부서와 일치 모든 사원 정보 조회");
				System.out.println("7. 입력 받은 급여 이상을 받는 모든 사원 정보 조회");
				System.out.println("8. 부서별 급여 합 전체 조회");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 >> ");
				input = sc.nextInt();
				
				System.out.println();
				
				
				switch(input) {
				case 1 :  break;
				case 2 : selectAll(); break;
				case 3 :  break;
				case 4 :  break;
				case 5 :  break;
				case 6 :  break;
				case 7 :  break;
				case 8 :  break;
				case 0 : System.out.println("프로그램을 종료합니다."); break;
				default : System.out.println("메뉴에 존재하는 번호만 입력하세요.");
				
				}
				
			} catch(InputMismatchException e) {
				System.out.println("숫자로 입력해주세요.!!");
				input = -1; // 첫 반복에서 잘못 입력해서 종료되는 상황을 방지
				sc.nextLine(); // 입력버퍼에 남은, 잘못 입력된 문자열을 제거하여 무한반복 방지
			}
			
		} while(input != 0);
	}
	
	
	/**
	 * 2. 전체 사원 정보조회
	 */
	public void selectAll()	{
		System.out.println("[ 전체 사원 정보조회 ]");
		// DB에서 전체 사원 정보를 조회하여 List<Employee> 형태로 반환하는
		// dao.selectAll() 메서드 호출
		List<Employee> empList = dao.selectAll();
		printAll(empList);
	}
	
	/** 전달받은 사원 List 모두 출력
	 * @param empList
	 */
	public void printAll(List<Employee> empList) {
		if(empList.isEmpty()) {
			System.out.println("조회된 사원 정보가 없습니다.");
			
		} else {
			System.out.println("사번 |   이름  | 주민 등록 번호 |        이메일        |   전화 번호   | 부서 | 직책 | 급여" );
			System.out.println("------------------------------------------------------------------------------------------------");
			for(Employee emp : empList) { 
				System.out.printf(" %2d  | %4s | %s | %20s | %s | %s | %s | %d\n",
						emp.getEmpId(), emp.getEmpName(), emp.getEmpNo(), emp.getEmail(), 
						emp.getPhone(), emp.getDepartmentTitle(), emp.getJobName(), emp.getSalary());
			}
		}
		
	}
}
