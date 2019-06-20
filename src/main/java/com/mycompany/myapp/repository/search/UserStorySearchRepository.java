package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.UserStory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link UserStory} entity.
 */
public interface UserStorySearchRepository extends ElasticsearchRepository<UserStory, Long> {
}
