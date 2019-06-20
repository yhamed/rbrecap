package com.mycompany.myapp.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserStory} entity.
 */
public class UserStoryDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    @Min(value = 1)
    @Max(value = 8)
    private Integer chiffrage;


    private Long sprintId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getChiffrage() {
        return chiffrage;
    }

    public void setChiffrage(Integer chiffrage) {
        this.chiffrage = chiffrage;
    }

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserStoryDTO userStoryDTO = (UserStoryDTO) o;
        if (userStoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userStoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserStoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", chiffrage=" + getChiffrage() +
            ", sprint=" + getSprintId() +
            "}";
    }
}
