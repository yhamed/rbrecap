package com.mycompany.myapp.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Sprint} entity.
 */
public class SprintDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 0)
    private Integer sprintNumber;

    private Boolean active;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSprintNumber() {
        return sprintNumber;
    }

    public void setSprintNumber(Integer sprintNumber) {
        this.sprintNumber = sprintNumber;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SprintDTO sprintDTO = (SprintDTO) o;
        if (sprintDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sprintDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SprintDTO{" +
            "id=" + getId() +
            ", sprintNumber=" + getSprintNumber() +
            ", active='" + isActive() + "'" +
            "}";
    }
}
