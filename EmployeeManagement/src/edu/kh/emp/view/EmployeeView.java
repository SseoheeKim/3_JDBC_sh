package edu.kh.emp.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
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
				System.out.println("9. 주민 번호가 일치하는 사원 정보 조회");
				System.out.println("10. 직급별 급여 평균 조회");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 >> ");
				input = sc.nextInt();
				
				System.out.println();
				
				
				switch(input) {
				case 1 : insertEmp(); break;
				case 2 : selectAll(); break;
				case 3 : selectEmpId(); break;
				case 4 : updateEmp(); break;
				case 5 : deleteEmp(); break;
				case 6 : selectDeptEmp(); break;
				case 7 : selectSalaryEmp(); break;
				case 8 : selectDeptTotalSalary(); break;
				case 9 : selectEmpNo(); break;
				case 10 : selectJobAvgSalary(); break;
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
	 * 새로운 사원 정보 추가
	 */
	public void insertEmp() {
		System.out.println(" [ 새로운 사원 정보 추가 ] ");
		
		int empId = inputEmpId(); // 사번
		// inputEmpId() 메서드는 값만 반환하기 때문에 변수 empId에 그 값을 대입해서 이후 사용
		
		System.out.print("이름 : ");
		String empName = sc.next();
		
		System.out.print("주민등록번호 : ");
		String empNo = sc.next();
		
		System.out.print("이메일 : ");
		String email = sc.next();
		
		System.out.print("전화번호(-제외) : ");
		String phone = sc.next();
		
		System.out.print("부서코드(D1~D9) : ");
		String deptCode = sc.next();
		
		System.out.print("직급코드(J1~J7) : ");
		String jobCode = sc.next();
		
		System.out.print("급여등급(S1~S6) : ");
		String salLevel= sc.next();
		
		System.out.print("급여 : ");
		int salary = sc.nextInt();
		
		System.out.print("보너스 : ");
		double bonus = sc.nextDouble();
		
		System.out.print("사수 번호: ");
		int managerId = sc.nextInt();
		
		// 입력받은 값을 Emloyee객체에 담아서 DAO로 전달
		Employee emp = new Employee(empId, empName, empNo, email, phone, 
									salary, deptCode, jobCode, salLevel, bonus, managerId);
		
		int result = dao.insertEmp(emp);
		// insert, update, delete 와 같은 DML구문은 수행 후 ''테이블에 반영된 행의 개수''를 반환
		// --> 조건이 잘못된 경우 반영된 행이 없으므로 0을 반환
		
		if(result > 0 ) { // DML구문 성공 시
			System.out.println("사원 정보 추가 성공!");
		} else { // DML구문 실패 시
			System.out.println("사원 정보 추가 실패ㅠㅠ");
		}
		
	}


	/**
	 * 전체 사원 정보조회
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
	
	
	
	/**
	 * 사번이 일치하는 사원 정보 조회
	 */
	public void selectEmpId() {
		System.out.println(" [사번이 일치하는 사원 정보 조회] ");
		
		// 사번 입력받기 ->> 중복되기 때문에 새로운 메서드 생성
		int empId = inputEmpId();
		
		// 입력받은 사번을 DAO의 selectEmpId() 메서드로 전달하여 조회된 사원정보 반환
		Employee emp = dao.selectEmpId(empId);
		printOne(emp); // 조회 결과 출력
	}
	
	
	/** 사원 1명의 정보를 출력
	 * @param emp
	 */
	public void printOne(Employee emp) {
		if(emp == null) {
			System.out.println("조회된 사원 정보가 없습니다.");
			
		} else {
			System.out.println("사번 |  이름  | 주민 등록 번호 |        이메일        |   전화 번호   | 부서 | 직책 | 급여" );
			System.out.println("------------------------------------------------------------------------------------------------");
			System.out.printf(" %2d  |  %4s  | %s |  %20s  | %s | %s | %s | %d\n",
						emp.getEmpId(), emp.getEmpName(), emp.getEmpNo(), emp.getEmail(), 
						emp.getPhone(), emp.getDepartmentTitle(), emp.getJobName(), emp.getSalary());
		}
	}
	

	/** 사번 입력 메서드(중복되는 코드)
	 * @return empId
	 */
	public int inputEmpId() {
		System.out.print("사번 입력 >> ");
		int empId = sc.nextInt(); sc.nextLine();
		return empId;
	}
	
	
	/** 
	 * 주민 번호가 일치하는 사원 정보 조회
	 */
	public void selectEmpNo() {
		System.out.println(" [ 주민 번호가 일치하는 사원 정보 조회 ] ");
		System.out.print("주민등록번호 입력 >> ");
		String empNo = sc.next();
		
		Employee emp = dao.selectEmpNo(empNo);
		printOne(emp);
	}
	
	
	/**
	 * 사번이 일치하는 사원 정보 수정
	 */
	public void updateEmp() {
		System.out.println(" [ 사원 정보 수정] ");
		
		int empId = inputEmpId();
		
		System.out.print("이메일 : ");
		String email = sc.next();
		
		System.out.print("전화번호(-제외) : ");
		String phone = sc.next();
		
		System.out.print("급여 : ");
		int salary = sc.nextInt();
		
		// 기본생성자를 생성하여 setter를 이용해서 초기화
		Employee emp = new Employee();
		emp.setEmpId(empId);
		emp.setEmail(email);
		emp.setPhone(phone);
		emp.setSalary(salary);
		
		int result = dao.updateEmp(emp); // update 결과는 반영된 행의 개수 반환
		
		if(result > 0) {
			System.out.println("사원 정보가 수정되었습니다.");
		} else {
			System.out.println("일치하는 사번정보가 없습니다.");
		}
	}

	
	
	/**
	 * 사번이 일치하는 사원 정보 삭제
	 */
	public void deleteEmp() {
		System.out.println(" [ 사원 정보 삭제] ");
		int empId = inputEmpId();
		
		System.out.print("정말 삭제하시겠습니까?(Y/N)");
		char check = sc.next().toUpperCase().charAt(0);
		// Y/N 대소문자 구분없이 입력
		
		
		int result = dao.deleteEmp(empId);
		if(check == 'Y') {
			if(result > 0 ) {
				System.out.println("사원 정보가 삭제되었습니다.");
			} else {
				System.out.println("사번이 일치하는 사원이 존재하지 않습니다.");
			}
		} else {
			System.out.println("취소되었습니다.");
		}
		
	}

	
	
	/**
	 * 입력 받은 부서와 일치하는 모든 사원 정보 조회
	 */
	public void selectDeptEmp() {
		System.out.println(" [ 입력 받은 부서와 일치하는 모든 사원 정보 조회 ] ");
		
		System.out.print("부서명 입력 : ");
		String deptTitle = sc.next();
		
		List<Employee> empList  = dao.selectDeptEmp(deptTitle);
		
		
		
	}


	/**
	 * 입력 받은 급여 이상을 받는 모든 사원 정보 조회
	 */
	public void selectSalaryEmp() {
		System.out.println("입력 받은 급여 이상을 받는 모든 사원 정보 조회");
		
		System.out.println("급여 입력 : ");
		int inputSalary = sc.nextInt();
		
		List<Employee> empList = dao.selectSalaryEmp(inputSalary);
		printAll(empList);
	}
    
	
	/**
	 * 부서별 급여 합 전체 조회
	 * -> DB 조회 결과를 HashMap<String, Integer>에 옮겨 담아서 반환하고
	 * 부서코드, 급여 합 조회
	 */
	public void selectDeptTotalSalary() {
		 System.out.println(" [ 부서별 급여 합 전체 조회 ]");
		 
		 Map<String, Integer> dept = dao.selectDeptTotalSalary();
		 
		 System.out.println(" 부서별     |     급여 합 ");
		 System.out.println("-----------------------------------");
		 
		 for( String key : dept.keySet()) {
			 System.out.printf(" %6s  |   %d \n", key, dept.get(key));
		 }
	}

	
	/**
	 * 직급별 급여 평균 조회
	 * -> DB 조회 결과를 HashMap<String, Double>에 옮겨 담아서 반환하고
	 * 	 직급명, 급여 평균 조회(소수점 한자리)
	 */
	public void selectJobAvgSalary() {
		System.out.println(" [ 직급별 급여 평균 조회 ] ");
		
		Map<String, Double> job = dao.selectJobAvgSalary();
		
		
		System.out.println("  직급별   |   급여 평균  ");
		System.out.println("-----------------------------------");
		for( String key: job.keySet()) {
			System.out.printf(" %s  |   %.1f \n", key, job.get(key));
		}
		
	}
	
}

