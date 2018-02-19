package at.fickl.clubadmin.repository;

import at.fickl.clubadmin.domain.TrainingGroupMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TrainingGroupMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingGroupMemberRepository extends JpaRepository<TrainingGroupMember, Long>, JpaSpecificationExecutor<TrainingGroupMember> {

}
