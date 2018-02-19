package at.fickl.clubadmin.repository.search;

import at.fickl.clubadmin.domain.ContributionGroupMember;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ContributionGroupMember entity.
 */
public interface ContributionGroupMemberSearchRepository extends ElasticsearchRepository<ContributionGroupMember, Long> {
}
