package at.fickl.clubadmin.service.impl;

import at.fickl.clubadmin.service.MemberService;
import at.fickl.clubadmin.domain.Member;
import at.fickl.clubadmin.repository.MemberRepository;
import at.fickl.clubadmin.repository.search.MemberSearchRepository;
import at.fickl.clubadmin.service.dto.MemberDTO;
import at.fickl.clubadmin.service.mapper.MemberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Member.
 */
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberRepository memberRepository;

    private final MemberMapper memberMapper;

    private final MemberSearchRepository memberSearchRepository;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper, MemberSearchRepository memberSearchRepository) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.memberSearchRepository = memberSearchRepository;
    }

    /**
     * Save a member.
     *
     * @param memberDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MemberDTO save(MemberDTO memberDTO) {
        log.debug("Request to save Member : {}", memberDTO);
        Member member = memberMapper.toEntity(memberDTO);
        member = memberRepository.save(member);
        MemberDTO result = memberMapper.toDto(member);
        memberSearchRepository.save(member);
        return result;
    }

    /**
     * Get all the members.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MemberDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Members");
        return memberRepository.findAll(pageable)
            .map(memberMapper::toDto);
    }

    /**
     * Get one member by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public MemberDTO findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        Member member = memberRepository.findOne(id);
        return memberMapper.toDto(member);
    }

    /**
     * Delete the member by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.delete(id);
        memberSearchRepository.delete(id);
    }

    /**
     * Search for the member corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MemberDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Members for query {}", query);
        Page<Member> result = memberSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(memberMapper::toDto);
    }
}
