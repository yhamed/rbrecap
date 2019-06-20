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
 * Criteria class for the {@link com.mycompany.myapp.domain.Tag} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.TagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter key;

    private StringFilter name;

    private LongFilter userStoryId;

    public TagCriteria(){
    }

    public TagCriteria(TagCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.key = other.key == null ? null : other.key.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.userStoryId = other.userStoryId == null ? null : other.userStoryId.copy();
    }

    @Override
    public TagCriteria copy() {
        return new TagCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getKey() {
        return key;
    }

    public void setKey(StringFilter key) {
        this.key = key;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
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
        final TagCriteria that = (TagCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(key, that.key) &&
            Objects.equals(name, that.name) &&
            Objects.equals(userStoryId, that.userStoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        key,
        name,
        userStoryId
        );
    }

    @Override
    public String toString() {
        return "TagCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (key != null ? "key=" + key + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (userStoryId != null ? "userStoryId=" + userStoryId + ", " : "") +
            "}";
    }

}
