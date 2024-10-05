package org.example.utils;

import lombok.Getter;

import java.util.List;

@Getter
public class Content<T> {
    private final List<T> content;

    public Content(List<T> content) {
        this.content = content;
    }
}