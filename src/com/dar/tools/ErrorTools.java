package com.dar.tools;

import javax.servlet.http.HttpServletResponse;

public class ErrorTools {

	public static final int MISSED_ARGS = 1; 
	
	public static final int EXIST_ACCOUNT= 2;
	public static final int MAIL_PASSWORD_ERROR = 3;
	public static final int WRONG_CODE = 4; 
	public static final int WRONG_TOKEN = 5;
	public static final int USER_BANNED = 6; 
	public static final int TO_CONFIRM = 7; 
	public static final int UNAUTHORIZED_ACTION = 8;
	public static final int WRONG_MAIL = 9; 
	
	public static final int EMPTY_GAME = 10; 
	public static final int PLATFORM_ERROR=11; 
	
	public static final int WRONG_RELATION = 12;
	public static final int GAME_NOT_FOUND = 13;
	public static final int USER_NOT_FOUND = 14;
	public static final int GAME_USER_NOT_FOUND = 15;
	
	public static final int WRONG_MSG = 16; 
	
	public static final int EXIST_ECHANGE = 17; 
	public static final int EXCHANGE_NOT_FOUND = 18; 
	public static final int WRONG_DATE = 19; 
	
	public static final int JSON_ERROR = 1000; 
	public static final int FALSE_MAIL = 1001; 
	public static final int BDD_ERROR = 1002; 
	public static final int SERVER_MAIL = 1003;
	public static final int WRONG_FORMAT = 1004; 
	public static final int UNIREST_ERROR = 1005; 
	

	
	public static void setStatut(int code, HttpServletResponse reponse){
		if(code == EXIST_ACCOUNT || code == MISSED_ARGS  || code == WRONG_CODE
				|| 	code == WRONG_RELATION || code == GAME_NOT_FOUND || code == USER_NOT_FOUND
				|| code == GAME_USER_NOT_FOUND || code == WRONG_MSG || code == EXIST_ECHANGE 
				|| code == EXCHANGE_NOT_FOUND || code == WRONG_DATE || code ==FALSE_MAIL
				|| code == WRONG_FORMAT){
			reponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		else if(code == UNIREST_ERROR || code == SERVER_MAIL || code == BDD_ERROR
				|| code == JSON_ERROR){
			reponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		else if(code == MAIL_PASSWORD_ERROR || code == WRONG_TOKEN || code == USER_BANNED || code == TO_CONFIRM || code == UNAUTHORIZED_ACTION
				|| code == WRONG_MAIL || code == EMPTY_GAME || code == PLATFORM_ERROR){
			reponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
}
