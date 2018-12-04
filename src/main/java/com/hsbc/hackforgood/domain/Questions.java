package com.hsbc.hackforgood.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Questions.
 */
@Entity
@Table(name = "questions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "questions")
public class Questions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "choice_x")
    private String choice_x;

    @Column(name = "choice_y")
    private String choice_y;

    @Column(name = "choice_z")
    private String choice_z;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Questions type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Questions name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public Questions title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChoice_x() {
        return choice_x;
    }

    public Questions choice_x(String choice_x) {
        this.choice_x = choice_x;
        return this;
    }

    public void setChoice_x(String choice_x) {
        this.choice_x = choice_x;
    }

    public String getChoice_y() {
        return choice_y;
    }

    public Questions choice_y(String choice_y) {
        this.choice_y = choice_y;
        return this;
    }

    public void setChoice_y(String choice_y) {
        this.choice_y = choice_y;
    }

    public String getChoice_z() {
        return choice_z;
    }

    public Questions choice_z(String choice_z) {
        this.choice_z = choice_z;
        return this;
    }

    public void setChoice_z(String choice_z) {
        this.choice_z = choice_z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Questions questions = (Questions) o;
        if (questions.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, questions.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Questions{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", name='" + name + "'" +
            ", title='" + title + "'" +
            ", choice_x='" + choice_x + "'" +
            ", choice_y='" + choice_y + "'" +
            ", choice_z='" + choice_z + "'" +
            '}';
    }
}
