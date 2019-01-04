package com.biz.bbs.dao.exec;

import com.biz.bbs.service.BBSMainService;

public class BBSExec02 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		BBSMainService bs = new BBSMainService();
		
		bs.bbsMenu();
		System.out.println(">> 게시판 종료 !!!!");
	}

}
