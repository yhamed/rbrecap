package com.mycompany.myapp.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Droit} entity.
 */
public class DroitDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Boolean isSwitch;


    private Set<UserStoryDTO> userStories = new HashSet<>();

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

    public Boolean isIsSwitch() {
        return isSwitch;
    }

    public void setIsSwitch(Boolean isSwitch) {
        this.isSwitch = isSwitch;
    }

    public Set<UserStoryDTO> getUserStories() {
        return userStories;
    }

    public void setUserStories(Set<UserStoryDTO> userStories) {
        this.userStories = userStories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DroitDTO droitDTO = (DroitDTO) o;
        if (droitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), droitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DroitDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isSwitch='" + isIsSwitch() + "'" +
            "}";
    }
}
