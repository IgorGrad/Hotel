package hr.lemax.hotel.common.enums;

public class Messages {

    public static class Error {
        public static class Hotel {
            public final static String HOTEL_NAME_REQUIRED = "The hotel name is required";
            public final static String HOTEL_NAME_LENGTH = "The street name cannot be shorter than 2 and longer than 1024 characters";
            public final static String HOTEL_PRICE_REQUIRED = "The hotel price is required";
            public final static String HOTEL_PRICE_POSITIVE = "The hotel price must be positive";
            public final static String HOTEL_LONGITUDE_REQUIRED = "The hotel longitude is required";
            public final static String HOTEL_LATITUDE_REQUIRED = "The hotel latitude is required";
            public final static String GEOLOCATION_INVALID = "The coordinates are invalid";
        }

        public static class User {
            public final static String USER_LONGITUDE_REQUIRED = "The user longitude is required";
            public final static String USER_LATITUDE_REQUIRED = "The user latitude is required";
            public final static String USER_GEOLOCATION_INVALID = "The user coordinates are invalid";
        }
    }
}
