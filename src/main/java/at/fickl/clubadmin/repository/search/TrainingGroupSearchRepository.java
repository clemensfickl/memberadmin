package at.fickl.clubadmin.repository.search;

import at.fickl.clubadmin.domain.TrainingGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TrainingGroup entity.
 */
public interface TrainingGroupSearchRepository extends ElasticsearchRepository<TrainingGroup, Long> {
}
