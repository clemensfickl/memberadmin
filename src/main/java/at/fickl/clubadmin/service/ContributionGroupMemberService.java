package at.fickl.clubadmin.service;

import at.fickl.clubadmin.service.dto.ContributionGroupMemberDTO;
import java.util.List;

/**
 * Service Interface for managing ContributionGroupMember.
 */
public interface ContributionGroupMemberService {

    /**
     * Save a contributionGroupMember.
     *
     * @param contributionGroupMemberDTO the entity to save
     * @return the persisted entity
     */
    ContributionGroupMemberDTO save(ContributionGroupMemberDTO contributionGroupMemberDTO);

    /**
     * Get all the contributionGroupMembers.
     *
     * @return the list of entities
     */
    List<ContributionGroupMemberDTO> findAll();

    /**
     * Get the "id" contributionGroupMember.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ContributionGroupMemberDTO findOne(Long id);

    /**
     * Delete the "id" contributionGroupMember.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the contributionGroupMember corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<ContributionGroupMemberDTO> search(String query);
}
