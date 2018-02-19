package at.fickl.clubadmin.repository.search;

import at.fickl.clubadmin.domain.ContributionGroup;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContributionGroup entity.
 */
public interface ContributionGroupSearchRepository extends ElasticsearchRepository<ContributionGroup, Long> {
}
