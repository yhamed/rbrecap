package com.mycompany.myapp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Sprint.
 */
@Entity
@Table(name = "sprint")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "sprint")
public class Sprint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "sprint_number", nullable = false, unique = true)
    private Integer sprintNumber;

    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "sprint")
    private Set<UserStory> userStories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSprintNumber() {
        return sprintNumber;
    }

    public Sprint sprintNumber(Integer sprintNumber) {
        this.sprintNumber = sprintNumber;
        return this;
    }

    public void setSprintNumber(Integer sprintNumber) {
        this.sprintNumber = sprintNumber;
    }

    public Boolean isActive() {
        return active;
    }

    public Sprint active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<UserStory> getUserStories() {
        return userStories;
    }

    public Sprint userStories(Set<UserStory> userStories) {
        this.userStories = userStories;
        return this;
    }

    public Sprint addUserStory(UserStory userStory) {
        this.userStories.add(userStory);
        userStory.setSprint(this);
        return this;
    }

    public Sprint removeUserStory(UserStory userStory) {
        this.userStories.remove(userStory);
        userStory.setSprint(null);
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
        if (!(o instanceof Sprint)) {
            return false;
        }
        return id != null && id.equals(((Sprint) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Sprint{" +
            "id=" + getId() +
            ", sprintNumber=" + getSprintNumber() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
