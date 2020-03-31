package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;

public class ListSection extends AbstractSection<List<String>> {
    private static final long serialVersionUID = 1L;

    public ListSection(List<String> info) {
        super(info);
    }

    public ListSection(String...strings){
        super(Arrays.asList(strings));
    }

    @Override
    public String toString() {
        StringBuilder strings = new StringBuilder();
        for (String st :
                content) {
            strings.append("--> ").append(st).append("\n");
        }
        return strings.toString();
    }


}
