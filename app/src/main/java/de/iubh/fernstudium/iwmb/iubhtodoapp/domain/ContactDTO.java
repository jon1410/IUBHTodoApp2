package de.iubh.fernstudium.iwmb.iubhtodoapp.domain;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.Objects;

public class ContactDTO extends BaseObservable {

    private int id;
    private String name;

    public ContactDTO() {
    }

    public ContactDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactDTO that = (ContactDTO) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "ContactDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
