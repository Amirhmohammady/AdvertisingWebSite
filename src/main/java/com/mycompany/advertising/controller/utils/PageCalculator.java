package com.mycompany.advertising.controller.utils;

/**
 * Created by Amir on 6/3/2022.
 */
public class PageCalculator {
    public static PagesIndex getMyPage(int totalpages, int currentpage, int pagelength) {
        PagesIndex result = new PagesIndex();
        if (totalpages < pagelength) {
            result.firstPage = 1;
            result.lastPage = totalpages;
        } else if (currentpage <= pagelength / 2) {
            result.firstPage = 1;
            result.lastPage = pagelength;
        } else if (currentpage + pagelength / 2 > totalpages) {
            result.firstPage = totalpages - pagelength + 1;
            result.lastPage = totalpages;
        } else {
            result.lastPage = currentpage + pagelength / 2;
            result.firstPage = result.lastPage - pagelength + 1;
        }
        return result;
    }
}

class PagesIndex{
    public int firstPage, lastPage;
}