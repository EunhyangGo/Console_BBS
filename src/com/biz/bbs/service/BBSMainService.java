package com.biz.bbs.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.biz.bbs.dao.BBSMainDao;
import com.biz.bbs.dao.BBSMainDaoImp;
import com.biz.bbs.vo.BBSMainVO;

/*
 * Dao와 연계해서 CRUD에 대한 구체적인 실행을 실시하는 class
 */
public class BBSMainService {

	/*
	 * member 변수들을 생성하는데
	 */

	// dao.selectAll()에서 return한 bbsMainVO들을 담을 list
	List<BBSMainVO> bbsMainList;
	// *List : ArrayList, LinkedList를 대표하는 interface이다.

	// 어떤 클래스에 대한 객체를 선언할때
	// 만약 해당 클래스들을 대표하는 interface가 있으면
	// interface를 자료형으로 하여 선언을 한다.

	BBSMainDao mainDao;
	Scanner scan;

	public BBSMainService() {

		bbsMainList = new ArrayList();
		mainDao = new BBSMainDaoImp();
		scan = new Scanner(System.in);

	}
	

	public void bbsMenu() {
		while (true) {
			System.out.println("=======================================");
			System.out.println("1.리스트보기 2.추가  3.수정  4.삭제  0.종료");
			System.out.println("---------------------------------------");
			System.out.print("선택>> ");
			String strM = scan.nextLine();

			int intM = Integer.valueOf(strM);
			/*
			 * 버전1
			if (intM == 0)
				return;
			if (intM == 1)
				this.viewBBsList();
			if (intM == 2)
				this.insertBBS(); // 데이터 추가
			if (intM == 3)
				// 수정할 데이터를 확인
				this.viewBBsList();
				this.updateBBS(); // 데이터 수정
			if (intM == 4) {
				this.deleteBBS();
			
				// 삭제할 데이터를 확인
				this.viewBBsList();
				this.deleteBBS();
		}
*/
			if(intM == 0) return;
			else if(intM == 2 ) this.insertBBS();
			else this.viewBBsList();
			
			if(intM == 1 ) this.viewBBsText();
			if(intM == 3 ) this.updateBBS();
			if(intM == 4 ) this.deleteBBS();
		}
	}
	private void viewBBsText() {
		// TODO 게시판 내용 보기
		System.out.print("확인할 번호(Enter:취소)");
		String strId = scan.nextLine();
		
		if(strId.equals("")) {
			System.out.println("취소되었습니다.");
			return;
		}
		long longId = Long.valueOf(strId); 
		
		BBSMainVO vo = mainDao.findById(longId);
		System.out.println("===================================");
		System.out.println("작성일자: " + vo.getB_date());
		System.out.println("작성자: " + vo.getB_auth() );
		System.out.println("제목 : " + vo.getB_subject());
		System.out.println("내용 : " + vo.getB_text());
		System.out.println("===================================");
		
		
	}

	/*
	 * 키보드에서 작성자, 제목, 내용을 입력받고 현재 컴퓨터 날짜를 작성일자로 하여 DB에 저장하기
	 */

	private void insertBBS() {
		// TODO Auto-generated method stub

		System.out.println("작성자 >> ");
		String strAuth = scan.nextLine();

		System.out.println("제목 >> ");
		String strSubject = scan.nextLine();

		System.out.println("내용 >> ");
		String strText = scan.nextLine();

		// 작성일자 생성
		// Old 버전
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

		Date toDay = new Date();

		String strDate = sf.format(toDay);

		// New(1.8이상)버전
		LocalDate ld = LocalDate.now();
		strDate = ld.toString();

/*
		// 변수를 vo에 담기
		BBSMainVO vo = new BBSMainVO();
		vo.setB_date(strDate);
		vo.setB_auth(strAuth);
		vo.setB_subject(strSubject);
		vo.setB_text(strText);
*/
		// 게시판 2.0으로 변경
		BBSMainVO vo = new BBSMainVO(
				strDate,
				strAuth,
				strSubject,
				strText
				);
		// vo를 dao에게 전달
		mainDao.insert(vo);

	}

	private void deleteBBS() {
		// TODO 게시판 삭제하기

		System.out.println("삭제할 번호 >>");
		String strId = scan.nextLine();
		
		
		if(strId.equals("")) {
			System.out.println("삭제가 취소 되었습니다");
			return;
		}
		long longId = Long.valueOf(strId);
		// 삭제하기 전에
		// 삭제할 데이터를 다시 확인시켜주자.

		// ID를 기준으로 데이터 1개 가져오기
		BBSMainVO vo = mainDao.findById(longId);
		System.out.println("=====================");
		System.out.println("삭제할 데이터 확인");
		System.out.println("---------------------");
		System.out.println("작성일자 : " + vo.getB_date());
		System.out.println("작성자 : " + vo.getB_auth());
		System.out.println("제목 : " + vo.getB_subject());
		System.out.println("내용 : " + vo.getB_text());
		System.out.println("=====================");
		System.out.print("정말 삭제할까요?(YES >>)");
		String confirm = scan.nextLine();
		if (confirm.equals("YES")) {
			mainDao.delete(longId);
			System.out.println("삭제 되었습니다");
		} else {
			System.out.println("취소 되었습니다");
		}
	}

	private void updateBBS() {
		// TODO 게시판 리스트에서 번호를 선택하면 수정 시작
		System.out.println("수정할 번호 >>");
		String strId = scan.nextLine();
		
		
		if(strId.equals("")) {
			System.out.println("수정이 취소 되었습니다.");
			return;
		}
		long longId = Long.valueOf(strId);
		BBSMainVO vo = mainDao.findById(longId);
		
		System.out.println("수정하려면 내용 입력, 수정 취소:Enter");
		System.out.println("=======================================");
		System.out.println("작성자 : " + vo.getB_auth());
		System.out.print("수정");
		String strAuth = scan.nextLine();
		
		System.out.println("제목 : " + vo.getB_subject());
		System.out.print("수정 : " );
		String strSubject = scan.nextLine();
		
		System.out.println("내용: " + vo.getB_text());
		System.out.print("수정 : ");
		String strText = scan.nextLine();
		
		// 만약 내용(작성자, 제목, 내용)을 입력하지 않고 Enter만 입력했으면 
		// 원래 내용을 그대로 유지 하도록 한다.
		
		if(strAuth.equals("") == false ) {
			vo.setB_auth(strAuth);
		}
		if(!strSubject.equals("")) {
			vo.setB_subject(strSubject);
		}
		if(!strText.equals("")) {
			vo.setB_text(strText);
		}
		mainDao.update(vo);

	}

	// 게시판 List를 보는 method() 선언
	public void viewBBsList() {
		/*
		 * 현재시각 아직 SelectAll()이 구현이 되어 있지 않지만 service입장에서는 selectAll()이 게시판 전체 리스트를
		 * return해 줄것이라는 가정을 하고 나머지 코드를 작성 할 수 있다.
		 */
		bbsMainList = mainDao.selectAll();

		System.out.println("===============================================================");
		System.out.println("나의 게시판 v1.1");
		System.out.println("===============================================================");
		System.out.printf("%5s  %-10s%-15s%s\n", "NO", "날짜  ", "작성자  ", "제목  ");
		System.out.println("---------------------------------------------------------------");

		if (bbsMainList == null) {
			System.out.println("데이터가 없습니다.");
		} else {
			for (BBSMainVO vo : bbsMainList) {
				System.out.printf("%5d  ", vo.getB_id());
				System.out.printf("%-12s", vo.getB_date());
				System.out.printf("%-15s", vo.getB_auth());
				System.out.println(vo.getB_subject());
			}
		}
	}

}
