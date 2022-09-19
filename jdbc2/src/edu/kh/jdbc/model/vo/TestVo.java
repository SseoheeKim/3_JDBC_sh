package edu.kh.jdbc.model.vo;

public class TestVo {
	private int testNo;
	private String testTitle;
	private String testContent;
	
	public TestVo() { }

	public TestVo(int testNo, String testTitle, String testContent) {
		super();
		this.testNo = testNo;
		this.testTitle = testTitle;
		this.testContent = testContent;
	}


	public int getTestNo() {
		return testNo;
	}

	public void setTestNo(int testNo) {
		this.testNo = testNo;
	}

	public String getTestTitle() {
		return testTitle;
	}

	public void setTestTitle(String testTitle) {
		this.testTitle = testTitle;
	}

	public String getTestContent() {
		return testContent;
	}

	public void setTestContent(String testContent) {
		this.testContent = testContent;
	}

	@Override // 객체의 모든 정보를 한눈에 확인할 수 있는 toString()메서드(Object클래스)
	public String toString() {
		return "TestVo [testNo=" + testNo + ", testTitle=" + testTitle + ", testContent=" + testContent + "]";
	}
	
}
