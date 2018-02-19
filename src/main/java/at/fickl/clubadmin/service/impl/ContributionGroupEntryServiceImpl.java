package at.fickl.clubadmin.service.impl;

import at.fickl.clubadmin.service.ContributionGroupEntryService;
import at.fickl.clubadmin.domain.ContributionGroupEntry;
import at.fickl.clubadmin.repository.ContributionGroupEntryRepository;
import at.fickl.clubadmin.repository.search.ContributionGroupEntrySearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupEntryDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupEntryMapper;
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
 * Service Implementation for managing ContributionGroupEntry.
 */
@Service
@Transactional
public class ContributionGroupEntryServiceImpl implements ContributionGroupEntryService {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupEntryServiceImpl.class);

    private final ContributionGroupEntryRepository contributionGroupEntryRepository;

    private final ContributionGroupEntryMapper contributionGroupEntryMapper;

    private final ContributionGroupEntrySearchRepository contributionGroupEntrySearchRepository;

    public ContributionGroupEntryServiceImpl(ContributionGroupEntryRepository contributionGroupEntryRepository, ContributionGroupEntryMapper contributionGroupEntryMapper, ContributionGroupEntrySearchRepository contributionGroupEntrySearchRepository) {
        this.contributionGroupEntryRepository = contributionGroupEntryRepository;
        this.contributionGroupEntryMapper = contributionGroupEntryMapper;
        this.contributionGroupEntrySearchRepository = contributionGroupEntrySearchRepository;
    }

    /**
     * Save a contributionGroupEntry.
     *
     * @param contributionGroupEntryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContributionGroupEntryDTO save(ContributionGroupEntryDTO contributionGroupEntryDTO) {
        log.debug("Request to save ContributionGroupEntry : {}", contributionGroupEntryDTO);
        ContributionGroupEntry contributionGroupEntry = contributionGroupEntryMapper.toEntity(contributionGroupEntryDTO);
        contributionGroupEntry = contributionGroupEntryRepository.save(contributionGroupEntry);
        ContributionGroupEntryDTO result = contributionGroupEntryMapper.toDto(contributionGroupEntry);
        contributionGroupEntrySearchRepository.save(contributionGroupEntry);
        return result;
    }

    /**
     * Get all the contributionGroupEntries.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionGroupEntryDTO> findAll() {
        log.debug("Request to get all ContributionGroupEntries");
        return contributionGroupEntryRepository.findAll().stream()
            .map(contributionGroupEntryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contributionGroupEntry by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ContributionGroupEntryDTO findOne(Long id) {
        log.debug("Request to get ContributionGroupEntry : {}", id);
        ContributionGroupEntry contributionGroupEntry = contributionGroupEntryRepository.findOne(id);
        return contributionGroupEntryMapper.toDto(contributionGroupEntry);
    }

    /**
     * Delete the contributionGroupEntry by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContributionGroupEntry : {}", id);
        contributionGroupEntryRepository.delete(id);
        contributionGroupEntrySearchRepository.delete(id);
    }

    /**
     * Search for the contributionGroupEntry corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionGroupEntryDTO> search(String query) {
        log.debug("Request to search ContributionGroupEntries for query {}", query);
        return StreamSupport
            .stream(contributionGroupEntrySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contributionGroupEntryMapper::toDto)
            .collect(Collectors.toList());
    }
}
