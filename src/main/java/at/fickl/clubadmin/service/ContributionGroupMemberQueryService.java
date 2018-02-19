package at.fickl.clubadmin.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import at.fickl.clubadmin.domain.ContributionGroupMember;
import at.fickl.clubadmin.domain.*; // for static metamodels
import at.fickl.clubadmin.repository.ContributionGroupMemberRepository;
import at.fickl.clubadmin.repository.search.ContributionGroupMemberSearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupMemberCriteria;

import at.fickl.clubadmin.service.dto.ContributionGroupMemberDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupMemberMapper;

/**
 * Service for executing complex queries for ContributionGroupMember entities in the database.
 * The main input is a {@link ContributionGroupMemberCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContributionGroupMemberDTO} or a {@link Page} of {@link ContributionGroupMemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContributionGroupMemberQueryService extends QueryService<ContributionGroupMember> {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupMemberQueryService.class);


    private final ContributionGroupMemberRepository contributionGroupMemberRepository;

    private final ContributionGroupMemberMapper contributionGroupMemberMapper;

    private final ContributionGroupMemberSearchRepository contributionGroupMemberSearchRepository;

    public ContributionGroupMemberQueryService(ContributionGroupMemberRepository contributionGroupMemberRepository, ContributionGroupMemberMapper contributionGroupMemberMapper, ContributionGroupMemberSearchRepository contributionGroupMemberSearchRepository) {
        this.contributionGroupMemberRepository = contributionGroupMemberRepository;
        this.contributionGroupMemberMapper = contributionGroupMemberMapper;
        this.contributionGroupMemberSearchRepository = contributionGroupMemberSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContributionGroupMemberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContributionGroupMemberDTO> findByCriteria(ContributionGroupMemberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ContributionGroupMember> specification = createSpecification(criteria);
        return contributionGroupMemberMapper.toDto(contributionGroupMemberRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContributionGroupMemberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContributionGroupMemberDTO> findByCriteria(ContributionGroupMemberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ContributionGroupMember> specification = createSpecification(criteria);
        final Page<ContributionGroupMember> result = contributionGroupMemberRepository.findAll(specification, page);
        return result.map(contributionGroupMemberMapper::toDto);
    }

    /**
     * Function to convert ContributionGroupMemberCriteria to a {@link Specifications}
     */
    private Specifications<ContributionGroupMember> createSpecification(ContributionGroupMemberCriteria criteria) {
        Specifications<ContributionGroupMember> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ContributionGroupMember_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), ContributionGroupMember_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), ContributionGroupMember_.endDate));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGroupId(), ContributionGroupMember_.group, ContributionGroup_.id));
            }
            if (criteria.getMemberId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMemberId(), ContributionGroupMember_.member, Member_.id));
            }
        }
        return specification;
    }

}
