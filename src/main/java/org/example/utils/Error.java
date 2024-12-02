package org.example.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Error {
    private final Map<String, Map<String, List<String>>> content;

    public Error(Map<String, List<String>> errors) {
        this.content = new HashMap<>();

        this.content.put("errors", errors);
    }
}