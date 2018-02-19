package at.fickl.clubadmin.service;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import at.fickl.clubadmin.domain.ContributionGroupEntry;
import at.fickl.clubadmin.domain.*; // for static metamodels
import at.fickl.clubadmin.repository.ContributionGroupEntryRepository;
import at.fickl.clubadmin.repository.search.ContributionGroupEntrySearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupEntryCriteria;

import at.fickl.clubadmin.service.dto.ContributionGroupEntryDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupEntryMapper;

/**
 * Service for executing complex queries for ContributionGroupEntry entities in the database.
 * The main input is a {@link ContributionGroupEntryCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContributionGroupEntryDTO} or a {@link Page} of {@link ContributionGroupEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContributionGroupEntryQueryService extends QueryService<ContributionGroupEntry> {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupEntryQueryService.class);


    private final ContributionGroupEntryRepository contributionGroupEntryRepository;

    private final ContributionGroupEntryMapper contributionGroupEntryMapper;

    private final ContributionGroupEntrySearchRepository contributionGroupEntrySearchRepository;

    public ContributionGroupEntryQueryService(ContributionGroupEntryRepository contributionGroupEntryRepository, ContributionGroupEntryMapper contributionGroupEntryMapper, ContributionGroupEntrySearchRepository contributionGroupEntrySearchRepository) {
        this.contributionGroupEntryRepository = contributionGroupEntryRepository;
        this.contributionGroupEntryMapper = contributionGroupEntryMapper;
        this.contributionGroupEntrySearchRepository = contributionGroupEntrySearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContributionGroupEntryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContributionGroupEntryDTO> findByCriteria(ContributionGroupEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ContributionGroupEntry> specification = createSpecification(criteria);
        return contributionGroupEntryMapper.toDto(contributionGroupEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContributionGroupEntryDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContributionGroupEntryDTO> findByCriteria(ContributionGroupEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ContributionGroupEntry> specification = createSpecification(criteria);
        final Page<ContributionGroupEntry> result = contributionGroupEntryRepository.findAll(specification, page);
        return result.map(contributionGroupEntryMapper::toDto);
    }

    /**
     * Function to convert ContributionGroupEntryCriteria to a {@link Specifications}
     */
    private Specifications<ContributionGroupEntry> createSpecification(ContributionGroupEntryCriteria criteria) {
        Specifications<ContributionGroupEntry> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContributionGroupEntry_.id));
            }
            if (criteria.getYear() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getYear(), ContributionGroupEntry_.year));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), ContributionGroupEntry_.amount));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGroupId(), ContributionGroupEntry_.group, ContributionGroup_.id));
            }
        }
        return specification;
    }

}
