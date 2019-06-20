package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.DroitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Droit} and its DTO {@link DroitDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserStoryMapper.class})
public interface DroitMapper extends EntityMapper<DroitDTO, Droit> {


    @Mapping(target = "removeUserStories", ignore = true)

    default Droit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Droit droit = new Droit();
        droit.setId(id);
        return droit;
    }
}
