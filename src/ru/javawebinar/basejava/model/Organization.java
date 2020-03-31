package ru.javawebinar.basejava.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.basejava.util.DateUtil.NOW;
import static ru.javawebinar.basejava.util.DateUtil.of;

public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Link organizationName;
    private final List<Position> positions;

    public Organization(String companyName, String url, Position... positions) {
        this.organizationName = new Link(companyName, url);
        this.positions = Arrays.asList(positions);
    }

    public Organization(Link homePage, List<Position> positions) {
        this.organizationName = homePage;
        this.positions = positions;
    }

    public Link getOrganizationName() {
        return organizationName;
    }

    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!organizationName.equals(that.organizationName)) return false;
        return positions.equals(that.positions);
    }

    @Override
    public int hashCode() {
        int result = organizationName.hashCode();
        result = 31 * result + positions.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "organizationName=" + organizationName +
                ", positions=" + positions +
                '}';
    }

    public static class Position {
        protected final LocalDate dateOfStart;
        protected final LocalDate dateOfEnd;
        protected final String position;
        protected final String info;

        public Position(LocalDate dateOfStart, LocalDate dateOfEnd, String position, String info) {
            Objects.requireNonNull(dateOfStart);
            Objects.requireNonNull(dateOfEnd);
            Objects.requireNonNull(position);
            this.dateOfStart = dateOfStart;
            this.dateOfEnd = dateOfEnd;
            this.position = position;
            this.info = info;
        }

        public Position(int startYear, Month startMonth, String title, String description) {
            this(of(startYear, startMonth), NOW, title, description);
        }

        public Position(int startYear, Month startMonth, int endYear, Month endMonth, String title, String description) {
            this(of(startYear, startMonth), of(endYear, endMonth), title, description);
        }

        public String getPosition() {
            return position;
        }

        public LocalDate getDateOfStart() {
            return dateOfStart;
        }

        public LocalDate getDateOfEnd() {
            return dateOfEnd;
        }

        public String getInfo() {
            return info;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position that = (Position) o;

            if (!dateOfStart.equals(that.dateOfStart)) return false;
            if (!dateOfEnd.equals(that.dateOfEnd)) return false;
            if (!position.equals(that.position)) return false;
            return info != null ? info.equals(that.info) : that.info == null;
        }

        @Override
        public int hashCode() {
            int result = dateOfStart.hashCode();
            result = 31 * result + dateOfEnd.hashCode();
            result = 31 * result + position.hashCode();
            result = 31 * result + (info != null ? info.hashCode() : 0);
            return result;
        }

    }
}


