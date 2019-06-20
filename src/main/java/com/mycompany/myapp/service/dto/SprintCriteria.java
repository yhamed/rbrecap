package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Sprint} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SprintResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sprints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SprintCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter sprintNumber;

    private BooleanFilter active;

    private LongFilter userStoryId;

    public SprintCriteria(){
    }

    public SprintCriteria(SprintCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.sprintNumber = other.sprintNumber == null ? null : other.sprintNumber.copy();
        this.active = other.active == null ? null : other.active.copy();
        this.userStoryId = other.userStoryId == null ? null : other.userStoryId.copy();
    }

    @Override
    public SprintCriteria copy() {
        return new SprintCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getSprintNumber() {
        return sprintNumber;
    }

    public void setSprintNumber(IntegerFilter sprintNumber) {
        this.sprintNumber = sprintNumber;
    }

    public BooleanFilter getActive() {
        return active;
    }

    public void setActive(BooleanFilter active) {
        this.active = active;
    }

    public LongFilter getUserStoryId() {
        return userStoryId;
    }

    public void setUserStoryId(LongFilter userStoryId) {
        this.userStoryId = userStoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SprintCriteria that = (SprintCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sprintNumber, that.sprintNumber) &&
            Objects.equals(active, that.active) &&
            Objects.equals(userStoryId, that.userStoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sprintNumber,
        active,
        userStoryId
        );
    }

    @Override
    public String toString() {
        return "SprintCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sprintNumber != null ? "sprintNumber=" + sprintNumber + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (userStoryId != null ? "userStoryId=" + userStoryId + ", " : "") +
            "}";
    }

}
