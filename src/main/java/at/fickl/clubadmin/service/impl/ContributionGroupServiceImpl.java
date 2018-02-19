package at.fickl.clubadmin.service.impl;

import at.fickl.clubadmin.service.ContributionGroupService;
import at.fickl.clubadmin.domain.ContributionGroup;
import at.fickl.clubadmin.repository.ContributionGroupRepository;
import at.fickl.clubadmin.repository.search.ContributionGroupSearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupMapper;
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
 * Service Implementation for managing ContributionGroup.
 */
@Service
@Transactional
public class ContributionGroupServiceImpl implements ContributionGroupService {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupServiceImpl.class);

    private final ContributionGroupRepository contributionGroupRepository;

    private final ContributionGroupMapper contributionGroupMapper;

    private final ContributionGroupSearchRepository contributionGroupSearchRepository;

    public ContributionGroupServiceImpl(ContributionGroupRepository contributionGroupRepository, ContributionGroupMapper contributionGroupMapper, ContributionGroupSearchRepository contributionGroupSearchRepository) {
        this.contributionGroupRepository = contributionGroupRepository;
        this.contributionGroupMapper = contributionGroupMapper;
        this.contributionGroupSearchRepository = contributionGroupSearchRepository;
    }

    /**
     * Save a contributionGroup.
     *
     * @param contributionGroupDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContributionGroupDTO save(ContributionGroupDTO contributionGroupDTO) {
        log.debug("Request to save ContributionGroup : {}", contributionGroupDTO);
        ContributionGroup contributionGroup = contributionGroupMapper.toEntity(contributionGroupDTO);
        contributionGroup = contributionGroupRepository.save(contributionGroup);
        ContributionGroupDTO result = contributionGroupMapper.toDto(contributionGroup);
        contributionGroupSearchRepository.save(contributionGroup);
        return result;
    }

    /**
     * Get all the contributionGroups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionGroupDTO> findAll() {
        log.debug("Request to get all ContributionGroups");
        return contributionGroupRepository.findAll().stream()
            .map(contributionGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contributionGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ContributionGroupDTO findOne(Long id) {
        log.debug("Request to get ContributionGroup : {}", id);
        ContributionGroup contributionGroup = contributionGroupRepository.findOne(id);
        return contributionGroupMapper.toDto(contributionGroup);
    }

    /**
     * Delete the contributionGroup by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContributionGroup : {}", id);
        contributionGroupRepository.delete(id);
        contributionGroupSearchRepository.delete(id);
    }

    /**
     * Search for the contributionGroup corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionGroupDTO> search(String query) {
        log.debug("Request to search ContributionGroups for query {}", query);
        return StreamSupport
            .stream(contributionGroupSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contributionGroupMapper::toDto)
            .collect(Collectors.toList());
    }
}
