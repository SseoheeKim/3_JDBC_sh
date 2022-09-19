package edu.kh.jdbc.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

// XML파일을 만들기 위한 클래스
public class CreateXMLFile {
	public static void main(String[] args) {
		/* XML(eXtensible Markup Language)
		 - 단순화 된 데이터 기술 형식
		 
		 - XML에 저장되는 데이터는 Key : Value 형식(Map)
			- Key, Value 모두 문자열(String)
			- Map<String, String>
		*/
		
		
		/* Properties 컬렉션 객체
		   - XML 파일을 읽고, 쓰기 위한 IO 관련 클래스 필요
		   - Map의 후손 클래스로 Key:Value 모두 문자열(String)형식
		   - XML파일을 읽고, 쓰는데 특화된 메서드 제공
		 */
		
		
		try {
			Scanner sc = new Scanner(System.in);
			
			System.out.print("생성할 파일 이름 : ");
			String fileName = sc.nextLine();
			
			// Properties 객체 생성
			Properties prop = new Properties();
		
			// FileOutputStream 생성(파일명.확장자)
			FileOutputStream fos = new FileOutputStream(fileName + ".xml");
			
			
			// Properties 객체를 이용해서 XML 파일 생성
			prop.storeToXML(fos, fileName+".xml file");
			
			System.out.println(fileName + ".xml 파일 생성 완료");
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			
		}
	}
}
