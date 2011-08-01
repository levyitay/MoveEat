package com.Rest2Go;

public class Consts {
	
	
	public final String APP_NAME = "MoveEat"; 
	
	public class Extras{

		protected static final String SearchResult = "com.Rest2Go.SearchResultList";
		
	}
	public class PROTOCOL{
		
		public static final int 	RESTRUANTS_REQ = 0;
		public static final int		RESTRUANT_DETAILS_REQ = 1;
		public static final int		RESTRUANT_MENU_REQ = 2;
		public static final int		DISH_DETAILS_REQ = 3;
		public static final int		USER_COMMENT = 4;
	}
	public class DB_COLUMNS {
		public static final String SECONDARY_PHONE = "SECONDARY_PHONE";
		public static final String PHONE = "PHONE";
		public static final String ADDRESS = "ADDRESS";
		public static final String NAME = "NAME";
		public static final String ID = "ID";
		public static final String ICON_URL = "LOGO_URL";
		public static final String IMAGE_URL = "PRIMARY_IMAGE_URL";
	}
	
	public final static long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	public final static long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	public final static String IMAGE_PATH = "http://moveeat.byethost18.com/Images/RestPictures/";
	
	
	public static final String REST_ID = "com.MoveEat.rest_id";

}
