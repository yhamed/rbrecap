package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserStory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the UserStory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long>, JpaSpecificationExecutor<UserStory> {

}
