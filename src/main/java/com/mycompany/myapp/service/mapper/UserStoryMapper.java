package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.UserStoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserStory} and its DTO {@link UserStoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {SprintMapper.class})
public interface UserStoryMapper extends EntityMapper<UserStoryDTO, UserStory> {

    @Mapping(source = "sprint.id", target = "sprintId")
    UserStoryDTO toDto(UserStory userStory);

    @Mapping(source = "sprintId", target = "sprint")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "removeTags", ignore = true)
    @Mapping(target = "droits", ignore = true)
    @Mapping(target = "removeDroits", ignore = true)
    UserStory toEntity(UserStoryDTO userStoryDTO);

    default UserStory fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserStory userStory = new UserStory();
        userStory.setId(id);
        return userStory;
    }
}
