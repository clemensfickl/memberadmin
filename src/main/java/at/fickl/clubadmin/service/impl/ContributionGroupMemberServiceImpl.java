package at.fickl.clubadmin.service.impl;

import at.fickl.clubadmin.service.ContributionGroupMemberService;
import at.fickl.clubadmin.domain.ContributionGroupMember;
import at.fickl.clubadmin.repository.ContributionGroupMemberRepository;
import at.fickl.clubadmin.repository.search.ContributionGroupMemberSearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupMemberDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupMemberMapper;
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
 * Service Implementation for managing ContributionGroupMember.
 */
@Service
@Transactional
public class ContributionGroupMemberServiceImpl implements ContributionGroupMemberService {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupMemberServiceImpl.class);

    private final ContributionGroupMemberRepository contributionGroupMemberRepository;

    private final ContributionGroupMemberMapper contributionGroupMemberMapper;

    private final ContributionGroupMemberSearchRepository contributionGroupMemberSearchRepository;

    public ContributionGroupMemberServiceImpl(ContributionGroupMemberRepository contributionGroupMemberRepository, ContributionGroupMemberMapper contributionGroupMemberMapper, ContributionGroupMemberSearchRepository contributionGroupMemberSearchRepository) {
        this.contributionGroupMemberRepository = contributionGroupMemberRepository;
        this.contributionGroupMemberMapper = contributionGroupMemberMapper;
        this.contributionGroupMemberSearchRepository = contributionGroupMemberSearchRepository;
    }

    /**
     * Save a contributionGroupMember.
     *
     * @param contributionGroupMemberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ContributionGroupMemberDTO save(ContributionGroupMemberDTO contributionGroupMemberDTO) {
        log.debug("Request to save ContributionGroupMember : {}", contributionGroupMemberDTO);
        ContributionGroupMember contributionGroupMember = contributionGroupMemberMapper.toEntity(contributionGroupMemberDTO);
        contributionGroupMember = contributionGroupMemberRepository.save(contributionGroupMember);
        ContributionGroupMemberDTO result = contributionGroupMemberMapper.toDto(contributionGroupMember);
        contributionGroupMemberSearchRepository.save(contributionGroupMember);
        return result;
    }

    /**
     * Get all the contributionGroupMembers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionGroupMemberDTO> findAll() {
        log.debug("Request to get all ContributionGroupMembers");
        return contributionGroupMemberRepository.findAll().stream()
            .map(contributionGroupMemberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one contributionGroupMember by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ContributionGroupMemberDTO findOne(Long id) {
        log.debug("Request to get ContributionGroupMember : {}", id);
        ContributionGroupMember contributionGroupMember = contributionGroupMemberRepository.findOne(id);
        return contributionGroupMemberMapper.toDto(contributionGroupMember);
    }

    /**
     * Delete the contributionGroupMember by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContributionGroupMember : {}", id);
        contributionGroupMemberRepository.delete(id);
        contributionGroupMemberSearchRepository.delete(id);
    }

    /**
     * Search for the contributionGroupMember corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ContributionGroupMemberDTO> search(String query) {
        log.debug("Request to search ContributionGroupMembers for query {}", query);
        return StreamSupport
            .stream(contributionGroupMemberSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(contributionGroupMemberMapper::toDto)
            .collect(Collectors.toList());
    }
}
