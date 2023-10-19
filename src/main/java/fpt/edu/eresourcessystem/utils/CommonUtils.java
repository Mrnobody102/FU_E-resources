package fpt.edu.eresourcessystem.utils;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {

    // Paging format
    public static List<Integer> pagingFormat(int totalPage, int pageIndex) {
        int delta = 2;
        List<Integer> pages = new ArrayList<>();
        for (int i = 1; i <= totalPage; i++) {
            if (i == 1 || i == totalPage || (i >= pageIndex - delta && i <= pageIndex + delta)) {
                pages.add(i);
            } else if (i == pageIndex - (delta + 1) || i == pageIndex + (delta + 1)) {
                pages.add(-1); // "..."
            }
        }
        return pages;
    }
}
