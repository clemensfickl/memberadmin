package at.fickl.clubadmin.repository.search;

import at.fickl.clubadmin.domain.Member;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Member entity.
 */
public interface MemberSearchRepository extends ElasticsearchRepository<Member, Long> {
}
