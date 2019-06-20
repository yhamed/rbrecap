package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Sprint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Sprint} entity.
 */
public interface SprintSearchRepository extends ElasticsearchRepository<Sprint, Long> {
}
