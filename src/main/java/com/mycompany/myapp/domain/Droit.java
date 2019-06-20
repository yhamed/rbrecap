package com.mycompany.myapp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Droit.
 */
@Entity
@Table(name = "droit")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "droit")
public class Droit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "is_switch")
    private Boolean isSwitch;

    @ManyToMany
    @JoinTable(name = "droit_user_stories",
               joinColumns = @JoinColumn(name = "droit_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_stories_id", referencedColumnName = "id"))
    private Set<UserStory> userStories = new HashSet<>();

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

    public Droit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isIsSwitch() {
        return isSwitch;
    }

    public Droit isSwitch(Boolean isSwitch) {
        this.isSwitch = isSwitch;
        return this;
    }

    public void setIsSwitch(Boolean isSwitch) {
        this.isSwitch = isSwitch;
    }

    public Set<UserStory> getUserStories() {
        return userStories;
    }

    public Droit userStories(Set<UserStory> userStories) {
        this.userStories = userStories;
        return this;
    }

    public Droit addUserStories(UserStory userStory) {
        this.userStories.add(userStory);
        userStory.getDroits().add(this);
        return this;
    }

    public Droit removeUserStories(UserStory userStory) {
        this.userStories.remove(userStory);
        userStory.getDroits().remove(this);
        return this;
    }

    public void setUserStories(Set<UserStory> userStories) {
        this.userStories = userStories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Droit)) {
            return false;
        }
        return id != null && id.equals(((Droit) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Droit{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isSwitch='" + isIsSwitch() + "'" +
            "}";
    }
}
