package at.fickl.clubadmin.repository.search;

import at.fickl.clubadmin.domain.ContributionGroupEntry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContributionGroupEntry entity.
 */
public interface ContributionGroupEntrySearchRepository extends ElasticsearchRepository<ContributionGroupEntry, Long> {
}
