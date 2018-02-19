package at.fickl.clubadmin.repository.search;

import at.fickl.clubadmin.domain.TrainingGroupMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TrainingGroupMember entity.
 */
public interface TrainingGroupMemberSearchRepository extends ElasticsearchRepository<TrainingGroupMember, Long> {
}
