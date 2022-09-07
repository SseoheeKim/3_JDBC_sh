package edu.kh.emp.model.vo;

// VO (Value Object, 값 저장용 객체)
// == DB조회 결과 한 행을 조회
public class Employee {
	private int empId; // 사원 번호(사번)
	private String empName; // 사원 이름
	private String empNo; // 주민등록번호
	private String email; // 이메일
	private String phone; // 전화번호
	private String departmentTitle; // 부서명
	private String jobName; // 직급명
	private int salary; // 급여
	
	// 새로운 사원 정보 추가를 위해 필드추가
	private String deptCode;
	private String jobCode;
	private String salLevel;
	private double bonus;
	private int managerId;
	
	public Employee() { }

	
	// 새로운 사원 정보 추가를 위한 생성자 
	public Employee(int empId, String empName, String empNo, String email, String phone, int salary, String deptCode,
			String jobCode, String salLevel, double bonus, int managerId) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empNo = empNo;
		this.email = email;
		this.phone = phone;
		this.salary = salary;
		this.deptCode = deptCode;
		this.jobCode = jobCode;
		this.salLevel = salLevel;
		this.bonus = bonus;
		this.managerId = managerId;
	}


	public Employee(int empId, String empName, String empNo, String email, String phone, String departmentTitle,
			String jobName, int salary) {
		super();
		this.empId = empId;
		this.empName = empName;
		this.empNo = empNo;
		this.email = email;
		this.phone = phone;
		this.departmentTitle = departmentTitle;
		this.jobName = jobName;
		this.salary = salary;
	}

	
	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getSalLevel() {
		return salLevel;
	}

	public void setSalLevel(String salLevel) {
		this.salLevel = salLevel;
	}

	public double getBonus() {
		return bonus;
	}

	public void setBonus(double bonus) {
		this.bonus = bonus;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartmentTitle() {
		return departmentTitle;
	}

	public void setDepartmentTitle(String departmentTitle) {
		this.departmentTitle = departmentTitle;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", empName=" + empName + ", empNo=" + empNo + ", email=" + email
				+ ", phone=" + phone + ", departmentTitle=" + departmentTitle + ", jobName=" + jobName + ", salary="
				+ salary + ", deptCode=" + deptCode + ", jobCode=" + jobCode + ", salLevel=" + salLevel + ", bonus="
				+ bonus + ", managerId=" + managerId + "]";
	}
	
}
