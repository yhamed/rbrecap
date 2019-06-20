package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Droit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Droit} entity.
 */
public interface DroitSearchRepository extends ElasticsearchRepository<Droit, Long> {
}
