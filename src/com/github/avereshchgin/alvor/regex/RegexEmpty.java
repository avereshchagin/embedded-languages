package com.github.avereshchgin.alvor.regex;

import java.util.ArrayList;
import java.util.List;

public class RegexEmpty extends RegexNode {

    public List<String> getReferences() {
        return new ArrayList<String>();
    }

    public String toString() {
        return "";
    }
}
