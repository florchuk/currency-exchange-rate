package org.example.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class Page<T> extends Content<T> {
    private final Integer page;

    private final Integer pageSize;

    private final String sortName;

    private final String sortOrder;

    private final Long total;

    private final Boolean previousPage;

    private final Boolean nextPage;

    public Page(List<T> content, Integer page, Integer pageSize, String sortName, String sortOrder, Long total) {
        super(content);

        this.sortName = sortName;
        this.sortOrder = sortOrder;
        this.previousPage = page > 1;
        this.page = page;
        this.nextPage =((long) page * (long) pageSize) < total;
        this.pageSize = pageSize;
        this.total = total;
    }
}