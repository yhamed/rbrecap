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
 * Criteria class for the {@link com.mycompany.myapp.domain.UserStory} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.UserStoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /user-stories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UserStoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter chiffrage;

    private LongFilter sprintId;

    private LongFilter tagsId;

    private LongFilter droitsId;

    public UserStoryCriteria(){
    }

    public UserStoryCriteria(UserStoryCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.chiffrage = other.chiffrage == null ? null : other.chiffrage.copy();
        this.sprintId = other.sprintId == null ? null : other.sprintId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
        this.droitsId = other.droitsId == null ? null : other.droitsId.copy();
    }

    @Override
    public UserStoryCriteria copy() {
        return new UserStoryCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getChiffrage() {
        return chiffrage;
    }

    public void setChiffrage(IntegerFilter chiffrage) {
        this.chiffrage = chiffrage;
    }

    public LongFilter getSprintId() {
        return sprintId;
    }

    public void setSprintId(LongFilter sprintId) {
        this.sprintId = sprintId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }

    public LongFilter getDroitsId() {
        return droitsId;
    }

    public void setDroitsId(LongFilter droitsId) {
        this.droitsId = droitsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserStoryCriteria that = (UserStoryCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(chiffrage, that.chiffrage) &&
            Objects.equals(sprintId, that.sprintId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(droitsId, that.droitsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        chiffrage,
        sprintId,
        tagsId,
        droitsId
        );
    }

    @Override
    public String toString() {
        return "UserStoryCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (chiffrage != null ? "chiffrage=" + chiffrage + ", " : "") +
                (sprintId != null ? "sprintId=" + sprintId + ", " : "") +
                (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
                (droitsId != null ? "droitsId=" + droitsId + ", " : "") +
            "}";
    }

}
