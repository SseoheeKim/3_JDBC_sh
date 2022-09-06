package edu.kh.jdbc1.model.vo;

// JDBCExample4, 5
public class Employee {
	
	private String empName;
	private String jobName;
	private int salary;
	private int anuualIncome;
	private String hireDate;
	private String gender; 
 /* private char gender  
    : DB에서는 문자, 문자열이 따로 나뉘지 않기 때문에
    자바에서 쓰는 char형태의 문자를 받아오려면 생각이 필요*/
	
	public Employee() {	}

	public Employee(String empName, String jobName, int salary, int anuualIncome) {
		super();
		this.empName = empName;
		this.jobName = jobName;
		this.salary = salary;
		this.anuualIncome = anuualIncome;
	}
	

	public Employee(String empName, String hireDate, String gender) {
		super();
		this.empName = empName;
		this.hireDate = hireDate;
		this.gender = gender;
	}

	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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

	public int getAnuualIncome() {
		return anuualIncome;
	}

	public void setAnuualIncome(int anuualIncome) {
		this.anuualIncome = anuualIncome;
	}

	@Override
	public String toString() {
		return empName + " / " + jobName + " / " + salary + " / " + anuualIncome ;
	}
}
