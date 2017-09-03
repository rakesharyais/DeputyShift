package com.deputyshift.Server;

public class ServerURLConstants {

    public static final String ROOT_SERVER_URL = "https://apjoqdqpi3.execute-api.us-west-2.amazonaws.com/dmc";

    public static final String BUSINESS_URL = "/business";

    public static final String START_URL = "/shift/start";

    public static final String END_URL = "/shift/end";

    public static final String GET_SHIFTS_URL = "/shifts";

    public static String getStartShiftURL(){
        return ROOT_SERVER_URL + START_URL;
    }

    public static String getEndShiftURL(){
        return ROOT_SERVER_URL + END_URL;
    }

    public static String getListShiftURL(){
        return ROOT_SERVER_URL + GET_SHIFTS_URL;
    }

    public static String getBusinessURL(){
        return ROOT_SERVER_URL + BUSINESS_URL;
    }



}
