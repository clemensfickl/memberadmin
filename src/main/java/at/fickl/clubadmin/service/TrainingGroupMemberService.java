package at.fickl.clubadmin.service;

import at.fickl.clubadmin.service.dto.TrainingGroupMemberDTO;
import java.util.List;

/**
 * Service Interface for managing TrainingGroupMember.
 */
public interface TrainingGroupMemberService {

    /**
     * Save a trainingGroupMember.
     *
     * @param trainingGroupMemberDTO the entity to save
     * @return the persisted entity
     */
    TrainingGroupMemberDTO save(TrainingGroupMemberDTO trainingGroupMemberDTO);

    /**
     * Get all the trainingGroupMembers.
     *
     * @return the list of entities
     */
    List<TrainingGroupMemberDTO> findAll();

    /**
     * Get the "id" trainingGroupMember.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TrainingGroupMemberDTO findOne(Long id);

    /**
     * Delete the "id" trainingGroupMember.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the trainingGroupMember corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<TrainingGroupMemberDTO> search(String query);
}
