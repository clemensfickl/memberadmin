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

import at.fickl.clubadmin.domain.TrainingGroupMember;
import at.fickl.clubadmin.domain.*; // for static metamodels
import at.fickl.clubadmin.repository.TrainingGroupMemberRepository;
import at.fickl.clubadmin.repository.search.TrainingGroupMemberSearchRepository;
import at.fickl.clubadmin.service.dto.TrainingGroupMemberCriteria;

import at.fickl.clubadmin.service.dto.TrainingGroupMemberDTO;
import at.fickl.clubadmin.service.mapper.TrainingGroupMemberMapper;

/**
 * Service for executing complex queries for TrainingGroupMember entities in the database.
 * The main input is a {@link TrainingGroupMemberCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TrainingGroupMemberDTO} or a {@link Page} of {@link TrainingGroupMemberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrainingGroupMemberQueryService extends QueryService<TrainingGroupMember> {

    private final Logger log = LoggerFactory.getLogger(TrainingGroupMemberQueryService.class);


    private final TrainingGroupMemberRepository trainingGroupMemberRepository;

    private final TrainingGroupMemberMapper trainingGroupMemberMapper;

    private final TrainingGroupMemberSearchRepository trainingGroupMemberSearchRepository;

    public TrainingGroupMemberQueryService(TrainingGroupMemberRepository trainingGroupMemberRepository, TrainingGroupMemberMapper trainingGroupMemberMapper, TrainingGroupMemberSearchRepository trainingGroupMemberSearchRepository) {
        this.trainingGroupMemberRepository = trainingGroupMemberRepository;
        this.trainingGroupMemberMapper = trainingGroupMemberMapper;
        this.trainingGroupMemberSearchRepository = trainingGroupMemberSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TrainingGroupMemberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TrainingGroupMemberDTO> findByCriteria(TrainingGroupMemberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TrainingGroupMember> specification = createSpecification(criteria);
        return trainingGroupMemberMapper.toDto(trainingGroupMemberRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TrainingGroupMemberDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrainingGroupMemberDTO> findByCriteria(TrainingGroupMemberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TrainingGroupMember> specification = createSpecification(criteria);
        final Page<TrainingGroupMember> result = trainingGroupMemberRepository.findAll(specification, page);
        return result.map(trainingGroupMemberMapper::toDto);
    }

    /**
     * Function to convert TrainingGroupMemberCriteria to a {@link Specifications}
     */
    private Specifications<TrainingGroupMember> createSpecification(TrainingGroupMemberCriteria criteria) {
        Specifications<TrainingGroupMember> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TrainingGroupMember_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), TrainingGroupMember_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), TrainingGroupMember_.endDate));
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getGroupId(), TrainingGroupMember_.group, TrainingGroup_.id));
            }
            if (criteria.getMemberId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMemberId(), TrainingGroupMember_.member, Member_.id));
            }
        }
        return specification;
    }

}
