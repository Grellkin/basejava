package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;

public class OrganizationSection extends AbstractSection<List<Organization>> {
    private static final long serialVersionUID = 1L;

    public OrganizationSection(List<Organization> info) {
        super(info);
    }

    public OrganizationSection(Organization...organizations){
        super(Arrays.asList(organizations));
    }

    @Override
    public String toString() {
        StringBuilder strings = new StringBuilder();
        for (Organization st :
                content) {
            strings.append(st.toString()).append("\n");
        }
        return strings.toString();
    }
}
