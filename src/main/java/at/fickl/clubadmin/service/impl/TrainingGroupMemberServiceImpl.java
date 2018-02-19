package at.fickl.clubadmin.service.impl;

import at.fickl.clubadmin.service.TrainingGroupMemberService;
import at.fickl.clubadmin.domain.TrainingGroupMember;
import at.fickl.clubadmin.repository.TrainingGroupMemberRepository;
import at.fickl.clubadmin.repository.search.TrainingGroupMemberSearchRepository;
import at.fickl.clubadmin.service.dto.TrainingGroupMemberDTO;
import at.fickl.clubadmin.service.mapper.TrainingGroupMemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TrainingGroupMember.
 */
@Service
@Transactional
public class TrainingGroupMemberServiceImpl implements TrainingGroupMemberService {

    private final Logger log = LoggerFactory.getLogger(TrainingGroupMemberServiceImpl.class);

    private final TrainingGroupMemberRepository trainingGroupMemberRepository;

    private final TrainingGroupMemberMapper trainingGroupMemberMapper;

    private final TrainingGroupMemberSearchRepository trainingGroupMemberSearchRepository;

    public TrainingGroupMemberServiceImpl(TrainingGroupMemberRepository trainingGroupMemberRepository, TrainingGroupMemberMapper trainingGroupMemberMapper, TrainingGroupMemberSearchRepository trainingGroupMemberSearchRepository) {
        this.trainingGroupMemberRepository = trainingGroupMemberRepository;
        this.trainingGroupMemberMapper = trainingGroupMemberMapper;
        this.trainingGroupMemberSearchRepository = trainingGroupMemberSearchRepository;
    }

    /**
     * Save a trainingGroupMember.
     *
     * @param trainingGroupMemberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TrainingGroupMemberDTO save(TrainingGroupMemberDTO trainingGroupMemberDTO) {
        log.debug("Request to save TrainingGroupMember : {}", trainingGroupMemberDTO);
        TrainingGroupMember trainingGroupMember = trainingGroupMemberMapper.toEntity(trainingGroupMemberDTO);
        trainingGroupMember = trainingGroupMemberRepository.save(trainingGroupMember);
        TrainingGroupMemberDTO result = trainingGroupMemberMapper.toDto(trainingGroupMember);
        trainingGroupMemberSearchRepository.save(trainingGroupMember);
        return result;
    }

    /**
     * Get all the trainingGroupMembers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TrainingGroupMemberDTO> findAll() {
        log.debug("Request to get all TrainingGroupMembers");
        return trainingGroupMemberRepository.findAll().stream()
            .map(trainingGroupMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one trainingGroupMember by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TrainingGroupMemberDTO findOne(Long id) {
        log.debug("Request to get TrainingGroupMember : {}", id);
        TrainingGroupMember trainingGroupMember = trainingGroupMemberRepository.findOne(id);
        return trainingGroupMemberMapper.toDto(trainingGroupMember);
    }

    /**
     * Delete the trainingGroupMember by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TrainingGroupMember : {}", id);
        trainingGroupMemberRepository.delete(id);
        trainingGroupMemberSearchRepository.delete(id);
    }

    /**
     * Search for the trainingGroupMember corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<TrainingGroupMemberDTO> search(String query) {
        log.debug("Request to search TrainingGroupMembers for query {}", query);
        return StreamSupport
            .stream(trainingGroupMemberSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(trainingGroupMemberMapper::toDto)
            .collect(Collectors.toList());
    }
}
