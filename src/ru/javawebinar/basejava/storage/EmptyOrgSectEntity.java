package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Link;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;

import java.util.ArrayList;
import java.util.List;

public class EmptyOrgSectEntity {
    public OrganizationSection section = new OrganizationSection();
    public List<Organization.Position> positionList;
    public Link link;
    private List<Organization> listOrg;
    EmptyOrgSectEntity(){
        listOrg = new ArrayList<>();
        section.setContent(listOrg);
    }

    public void reInit(){
        Organization organization = new Organization();
        listOrg.add(organization);
        link = new Link();
        organization.setOrganizationName(link);
        positionList = new ArrayList<>();
        organization.setPositions(positionList);
    }

}
