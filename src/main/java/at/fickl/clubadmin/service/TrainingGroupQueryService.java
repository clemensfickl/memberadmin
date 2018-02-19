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

import at.fickl.clubadmin.domain.TrainingGroup;
import at.fickl.clubadmin.domain.*; // for static metamodels
import at.fickl.clubadmin.repository.TrainingGroupRepository;
import at.fickl.clubadmin.repository.search.TrainingGroupSearchRepository;
import at.fickl.clubadmin.service.dto.TrainingGroupCriteria;

import at.fickl.clubadmin.service.dto.TrainingGroupDTO;
import at.fickl.clubadmin.service.mapper.TrainingGroupMapper;

/**
 * Service for executing complex queries for TrainingGroup entities in the database.
 * The main input is a {@link TrainingGroupCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TrainingGroupDTO} or a {@link Page} of {@link TrainingGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrainingGroupQueryService extends QueryService<TrainingGroup> {

    private final Logger log = LoggerFactory.getLogger(TrainingGroupQueryService.class);


    private final TrainingGroupRepository trainingGroupRepository;

    private final TrainingGroupMapper trainingGroupMapper;

    private final TrainingGroupSearchRepository trainingGroupSearchRepository;

    public TrainingGroupQueryService(TrainingGroupRepository trainingGroupRepository, TrainingGroupMapper trainingGroupMapper, TrainingGroupSearchRepository trainingGroupSearchRepository) {
        this.trainingGroupRepository = trainingGroupRepository;
        this.trainingGroupMapper = trainingGroupMapper;
        this.trainingGroupSearchRepository = trainingGroupSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TrainingGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TrainingGroupDTO> findByCriteria(TrainingGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TrainingGroup> specification = createSpecification(criteria);
        return trainingGroupMapper.toDto(trainingGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TrainingGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrainingGroupDTO> findByCriteria(TrainingGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TrainingGroup> specification = createSpecification(criteria);
        final Page<TrainingGroup> result = trainingGroupRepository.findAll(specification, page);
        return result.map(trainingGroupMapper::toDto);
    }

    /**
     * Function to convert TrainingGroupCriteria to a {@link Specifications}
     */
    private Specifications<TrainingGroup> createSpecification(TrainingGroupCriteria criteria) {
        Specifications<TrainingGroup> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TrainingGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TrainingGroup_.name));
            }
        }
        return specification;
    }

}
