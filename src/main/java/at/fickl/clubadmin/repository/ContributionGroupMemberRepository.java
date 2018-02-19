package at.fickl.clubadmin.repository;

import at.fickl.clubadmin.domain.ContributionGroupMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ContributionGroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContributionGroupMemberRepository extends JpaRepository<ContributionGroupMember, Long>, JpaSpecificationExecutor<ContributionGroupMember> {

}
