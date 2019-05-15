package com.bjpowernode.crm.workbench.domain;

import java.util.List;

/**
 * DESCRIPTION:
 * user:
 * date:2019/5/14  11:26
 */
public class PageDataVO<T> {
    private int total;
    private List<T> datalist;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<T> datalist) {
        this.datalist = datalist;
    }
}
