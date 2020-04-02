package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.Objects;

public abstract class AbstractSection<T> implements Serializable {
    protected final T content;
    private static final long serialVersionUID = 1L;

    public AbstractSection(T content) {
        Objects.requireNonNull(content, "content must be not null");
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractSection<?> that = (AbstractSection<?>) o;

        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public String toString() {
        return "" + content;
    }
}
