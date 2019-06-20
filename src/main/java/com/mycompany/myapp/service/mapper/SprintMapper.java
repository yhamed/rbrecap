package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.SprintDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sprint} and its DTO {@link SprintDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SprintMapper extends EntityMapper<SprintDTO, Sprint> {


    @Mapping(target = "userStories", ignore = true)
    @Mapping(target = "removeUserStory", ignore = true)
    Sprint toEntity(SprintDTO sprintDTO);

    default Sprint fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sprint sprint = new Sprint();
        sprint.setId(id);
        return sprint;
    }
}
