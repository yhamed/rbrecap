package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserStory.
 */
@Entity
@Table(name = "user_story")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userstory")
public class UserStory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Min(value = 1)
    @Max(value = 8)
    @Column(name = "chiffrage")
    private Integer chiffrage;

    @ManyToOne
    @JsonIgnoreProperties("userStories")
    private Sprint sprint;

    @OneToMany(mappedBy = "userStory")
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(mappedBy = "userStories")
    @JsonIgnore
    private Set<Droit> droits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UserStory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public UserStory description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getChiffrage() {
        return chiffrage;
    }

    public UserStory chiffrage(Integer chiffrage) {
        this.chiffrage = chiffrage;
        return this;
    }

    public void setChiffrage(Integer chiffrage) {
        this.chiffrage = chiffrage;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public UserStory sprint(Sprint sprint) {
        this.sprint = sprint;
        return this;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public UserStory tags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public UserStory addTags(Tag tag) {
        this.tags.add(tag);
        tag.setUserStory(this);
        return this;
    }

    public UserStory removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.setUserStory(null);
        return this;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Droit> getDroits() {
        return droits;
    }

    public UserStory droits(Set<Droit> droits) {
        this.droits = droits;
        return this;
    }

    public UserStory addDroits(Droit droit) {
        this.droits.add(droit);
        droit.getUserStories().add(this);
        return this;
    }

    public UserStory removeDroits(Droit droit) {
        this.droits.remove(droit);
        droit.getUserStories().remove(this);
        return this;
    }

    public void setDroits(Set<Droit> droits) {
        this.droits = droits;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserStory)) {
            return false;
        }
        return id != null && id.equals(((UserStory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserStory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", chiffrage=" + getChiffrage() +
            "}";
    }
}
