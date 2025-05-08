package com.bernerus.boxy.api.v1;

public final class Endpoints {
    private static final String BASE_PATH = "/api/v1";
    public static final String CALCULATE_BOX_SIZE = Box.BASE_PATH + Box.CALCULATE_BOX_SIZE_SUB_PATH;
    
    public static class Box {
        public static final String BASE_PATH = Endpoints.BASE_PATH + "/box";
        public static final String CALCULATE_BOX_SIZE_SUB_PATH = "/calculate-size";
    }
    
    private Endpoints() {
        //Not to be instantiated
    }
}
