package com.wjwcloud.iot.model.page;


public final class PageRowsUtil {
    private static final int FIFTEEN = 15;
    private static Integer pageRows = 15;

    private PageRowsUtil() {
    }

    public static Integer getPageRows() {
        return pageRows;
    }

    public static void setPageRows(Integer pageRows) {
        pageRows = pageRows;
    }
}
