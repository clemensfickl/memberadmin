package at.fickl.clubadmin.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import at.fickl.clubadmin.domain.ContributionGroup;
import at.fickl.clubadmin.domain.*; // for static metamodels
import at.fickl.clubadmin.repository.ContributionGroupRepository;
import at.fickl.clubadmin.repository.search.ContributionGroupSearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupCriteria;

import at.fickl.clubadmin.service.dto.ContributionGroupDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupMapper;

/**
 * Service for executing complex queries for ContributionGroup entities in the database.
 * The main input is a {@link ContributionGroupCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContributionGroupDTO} or a {@link Page} of {@link ContributionGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContributionGroupQueryService extends QueryService<ContributionGroup> {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupQueryService.class);


    private final ContributionGroupRepository contributionGroupRepository;

    private final ContributionGroupMapper contributionGroupMapper;

    private final ContributionGroupSearchRepository contributionGroupSearchRepository;

    public ContributionGroupQueryService(ContributionGroupRepository contributionGroupRepository, ContributionGroupMapper contributionGroupMapper, ContributionGroupSearchRepository contributionGroupSearchRepository) {
        this.contributionGroupRepository = contributionGroupRepository;
        this.contributionGroupMapper = contributionGroupMapper;
        this.contributionGroupSearchRepository = contributionGroupSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContributionGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContributionGroupDTO> findByCriteria(ContributionGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ContributionGroup> specification = createSpecification(criteria);
        return contributionGroupMapper.toDto(contributionGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContributionGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContributionGroupDTO> findByCriteria(ContributionGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ContributionGroup> specification = createSpecification(criteria);
        final Page<ContributionGroup> result = contributionGroupRepository.findAll(specification, page);
        return result.map(contributionGroupMapper::toDto);
    }

    /**
     * Function to convert ContributionGroupCriteria to a {@link Specifications}
     */
    private Specifications<ContributionGroup> createSpecification(ContributionGroupCriteria criteria) {
        Specifications<ContributionGroup> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContributionGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ContributionGroup_.name));
            }
        }
        return specification;
    }

}
