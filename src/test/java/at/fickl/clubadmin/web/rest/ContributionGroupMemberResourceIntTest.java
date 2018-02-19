package at.fickl.clubadmin.web.rest;

import at.fickl.clubadmin.MemberadminApp;

import at.fickl.clubadmin.domain.ContributionGroupMember;
import at.fickl.clubadmin.domain.ContributionGroup;
import at.fickl.clubadmin.domain.Member;
import at.fickl.clubadmin.repository.ContributionGroupMemberRepository;
import at.fickl.clubadmin.service.ContributionGroupMemberService;
import at.fickl.clubadmin.repository.search.ContributionGroupMemberSearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupMemberDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupMemberMapper;
import at.fickl.clubadmin.web.rest.errors.ExceptionTranslator;
import at.fickl.clubadmin.service.dto.ContributionGroupMemberCriteria;
import at.fickl.clubadmin.service.ContributionGroupMemberQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static at.fickl.clubadmin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContributionGroupMemberResource REST controller.
 *
 * @see ContributionGroupMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MemberadminApp.class)
public class ContributionGroupMemberResourceIntTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ContributionGroupMemberRepository contributionGroupMemberRepository;

    @Autowired
    private ContributionGroupMemberMapper contributionGroupMemberMapper;

    @Autowired
    private ContributionGroupMemberService contributionGroupMemberService;

    @Autowired
    private ContributionGroupMemberSearchRepository contributionGroupMemberSearchRepository;

    @Autowired
    private ContributionGroupMemberQueryService contributionGroupMemberQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContributionGroupMemberMockMvc;

    private ContributionGroupMember contributionGroupMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContributionGroupMemberResource contributionGroupMemberResource = new ContributionGroupMemberResource(contributionGroupMemberService, contributionGroupMemberQueryService);
        this.restContributionGroupMemberMockMvc = MockMvcBuilders.standaloneSetup(contributionGroupMemberResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContributionGroupMember createEntity(EntityManager em) {
        ContributionGroupMember contributionGroupMember = new ContributionGroupMember()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return contributionGroupMember;
    }

    @Before
    public void initTest() {
        contributionGroupMemberSearchRepository.deleteAll();
        contributionGroupMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createContributionGroupMember() throws Exception {
        int databaseSizeBeforeCreate = contributionGroupMemberRepository.findAll().size();

        // Create the ContributionGroupMember
        ContributionGroupMemberDTO contributionGroupMemberDTO = contributionGroupMemberMapper.toDto(contributionGroupMember);
        restContributionGroupMemberMockMvc.perform(post("/api/contribution-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the ContributionGroupMember in the database
        List<ContributionGroupMember> contributionGroupMemberList = contributionGroupMemberRepository.findAll();
        assertThat(contributionGroupMemberList).hasSize(databaseSizeBeforeCreate + 1);
        ContributionGroupMember testContributionGroupMember = contributionGroupMemberList.get(contributionGroupMemberList.size() - 1);
        assertThat(testContributionGroupMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testContributionGroupMember.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the ContributionGroupMember in Elasticsearch
        ContributionGroupMember contributionGroupMemberEs = contributionGroupMemberSearchRepository.findOne(testContributionGroupMember.getId());
        assertThat(contributionGroupMemberEs).isEqualToIgnoringGivenFields(testContributionGroupMember);
    }

    @Test
    @Transactional
    public void createContributionGroupMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contributionGroupMemberRepository.findAll().size();

        // Create the ContributionGroupMember with an existing ID
        contributionGroupMember.setId(1L);
        ContributionGroupMemberDTO contributionGroupMemberDTO = contributionGroupMemberMapper.toDto(contributionGroupMember);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributionGroupMemberMockMvc.perform(post("/api/contribution-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContributionGroupMember in the database
        List<ContributionGroupMember> contributionGroupMemberList = contributionGroupMemberRepository.findAll();
        assertThat(contributionGroupMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembers() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList
        restContributionGroupMemberMockMvc.perform(get("/api/contribution-group-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getContributionGroupMember() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get the contributionGroupMember
        restContributionGroupMemberMockMvc.perform(get("/api/contribution-group-members/{id}", contributionGroupMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contributionGroupMember.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where startDate equals to DEFAULT_START_DATE
        defaultContributionGroupMemberShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the contributionGroupMemberList where startDate equals to UPDATED_START_DATE
        defaultContributionGroupMemberShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultContributionGroupMemberShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the contributionGroupMemberList where startDate equals to UPDATED_START_DATE
        defaultContributionGroupMemberShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where startDate is not null
        defaultContributionGroupMemberShouldBeFound("startDate.specified=true");

        // Get all the contributionGroupMemberList where startDate is null
        defaultContributionGroupMemberShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where startDate greater than or equals to DEFAULT_START_DATE
        defaultContributionGroupMemberShouldBeFound("startDate.greaterOrEqualThan=" + DEFAULT_START_DATE);

        // Get all the contributionGroupMemberList where startDate greater than or equals to UPDATED_START_DATE
        defaultContributionGroupMemberShouldNotBeFound("startDate.greaterOrEqualThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where startDate less than or equals to DEFAULT_START_DATE
        defaultContributionGroupMemberShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the contributionGroupMemberList where startDate less than or equals to UPDATED_START_DATE
        defaultContributionGroupMemberShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }


    @Test
    @Transactional
    public void getAllContributionGroupMembersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where endDate equals to DEFAULT_END_DATE
        defaultContributionGroupMemberShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the contributionGroupMemberList where endDate equals to UPDATED_END_DATE
        defaultContributionGroupMemberShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultContributionGroupMemberShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the contributionGroupMemberList where endDate equals to UPDATED_END_DATE
        defaultContributionGroupMemberShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where endDate is not null
        defaultContributionGroupMemberShouldBeFound("endDate.specified=true");

        // Get all the contributionGroupMemberList where endDate is null
        defaultContributionGroupMemberShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where endDate greater than or equals to DEFAULT_END_DATE
        defaultContributionGroupMemberShouldBeFound("endDate.greaterOrEqualThan=" + DEFAULT_END_DATE);

        // Get all the contributionGroupMemberList where endDate greater than or equals to UPDATED_END_DATE
        defaultContributionGroupMemberShouldNotBeFound("endDate.greaterOrEqualThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllContributionGroupMembersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);

        // Get all the contributionGroupMemberList where endDate less than or equals to DEFAULT_END_DATE
        defaultContributionGroupMemberShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the contributionGroupMemberList where endDate less than or equals to UPDATED_END_DATE
        defaultContributionGroupMemberShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }


    @Test
    @Transactional
    public void getAllContributionGroupMembersByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        ContributionGroup group = ContributionGroupResourceIntTest.createEntity(em);
        em.persist(group);
        em.flush();
        contributionGroupMember.setGroup(group);
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);
        Long groupId = group.getId();

        // Get all the contributionGroupMemberList where group equals to groupId
        defaultContributionGroupMemberShouldBeFound("groupId.equals=" + groupId);

        // Get all the contributionGroupMemberList where group equals to groupId + 1
        defaultContributionGroupMemberShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }


    @Test
    @Transactional
    public void getAllContributionGroupMembersByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        Member member = MemberResourceIntTest.createEntity(em);
        em.persist(member);
        em.flush();
        contributionGroupMember.setMember(member);
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);
        Long memberId = member.getId();

        // Get all the contributionGroupMemberList where member equals to memberId
        defaultContributionGroupMemberShouldBeFound("memberId.equals=" + memberId);

        // Get all the contributionGroupMemberList where member equals to memberId + 1
        defaultContributionGroupMemberShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContributionGroupMemberShouldBeFound(String filter) throws Exception {
        restContributionGroupMemberMockMvc.perform(get("/api/contribution-group-members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContributionGroupMemberShouldNotBeFound(String filter) throws Exception {
        restContributionGroupMemberMockMvc.perform(get("/api/contribution-group-members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingContributionGroupMember() throws Exception {
        // Get the contributionGroupMember
        restContributionGroupMemberMockMvc.perform(get("/api/contribution-group-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContributionGroupMember() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);
        contributionGroupMemberSearchRepository.save(contributionGroupMember);
        int databaseSizeBeforeUpdate = contributionGroupMemberRepository.findAll().size();

        // Update the contributionGroupMember
        ContributionGroupMember updatedContributionGroupMember = contributionGroupMemberRepository.findOne(contributionGroupMember.getId());
        // Disconnect from session so that the updates on updatedContributionGroupMember are not directly saved in db
        em.detach(updatedContributionGroupMember);
        updatedContributionGroupMember
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        ContributionGroupMemberDTO contributionGroupMemberDTO = contributionGroupMemberMapper.toDto(updatedContributionGroupMember);

        restContributionGroupMemberMockMvc.perform(put("/api/contribution-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupMemberDTO)))
            .andExpect(status().isOk());

        // Validate the ContributionGroupMember in the database
        List<ContributionGroupMember> contributionGroupMemberList = contributionGroupMemberRepository.findAll();
        assertThat(contributionGroupMemberList).hasSize(databaseSizeBeforeUpdate);
        ContributionGroupMember testContributionGroupMember = contributionGroupMemberList.get(contributionGroupMemberList.size() - 1);
        assertThat(testContributionGroupMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testContributionGroupMember.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the ContributionGroupMember in Elasticsearch
        ContributionGroupMember contributionGroupMemberEs = contributionGroupMemberSearchRepository.findOne(testContributionGroupMember.getId());
        assertThat(contributionGroupMemberEs).isEqualToIgnoringGivenFields(testContributionGroupMember);
    }

    @Test
    @Transactional
    public void updateNonExistingContributionGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = contributionGroupMemberRepository.findAll().size();

        // Create the ContributionGroupMember
        ContributionGroupMemberDTO contributionGroupMemberDTO = contributionGroupMemberMapper.toDto(contributionGroupMember);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContributionGroupMemberMockMvc.perform(put("/api/contribution-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the ContributionGroupMember in the database
        List<ContributionGroupMember> contributionGroupMemberList = contributionGroupMemberRepository.findAll();
        assertThat(contributionGroupMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContributionGroupMember() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);
        contributionGroupMemberSearchRepository.save(contributionGroupMember);
        int databaseSizeBeforeDelete = contributionGroupMemberRepository.findAll().size();

        // Get the contributionGroupMember
        restContributionGroupMemberMockMvc.perform(delete("/api/contribution-group-members/{id}", contributionGroupMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contributionGroupMemberExistsInEs = contributionGroupMemberSearchRepository.exists(contributionGroupMember.getId());
        assertThat(contributionGroupMemberExistsInEs).isFalse();

        // Validate the database is empty
        List<ContributionGroupMember> contributionGroupMemberList = contributionGroupMemberRepository.findAll();
        assertThat(contributionGroupMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContributionGroupMember() throws Exception {
        // Initialize the database
        contributionGroupMemberRepository.saveAndFlush(contributionGroupMember);
        contributionGroupMemberSearchRepository.save(contributionGroupMember);

        // Search the contributionGroupMember
        restContributionGroupMemberMockMvc.perform(get("/api/_search/contribution-group-members?query=id:" + contributionGroupMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionGroupMember.class);
        ContributionGroupMember contributionGroupMember1 = new ContributionGroupMember();
        contributionGroupMember1.setId(1L);
        ContributionGroupMember contributionGroupMember2 = new ContributionGroupMember();
        contributionGroupMember2.setId(contributionGroupMember1.getId());
        assertThat(contributionGroupMember1).isEqualTo(contributionGroupMember2);
        contributionGroupMember2.setId(2L);
        assertThat(contributionGroupMember1).isNotEqualTo(contributionGroupMember2);
        contributionGroupMember1.setId(null);
        assertThat(contributionGroupMember1).isNotEqualTo(contributionGroupMember2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionGroupMemberDTO.class);
        ContributionGroupMemberDTO contributionGroupMemberDTO1 = new ContributionGroupMemberDTO();
        contributionGroupMemberDTO1.setId(1L);
        ContributionGroupMemberDTO contributionGroupMemberDTO2 = new ContributionGroupMemberDTO();
        assertThat(contributionGroupMemberDTO1).isNotEqualTo(contributionGroupMemberDTO2);
        contributionGroupMemberDTO2.setId(contributionGroupMemberDTO1.getId());
        assertThat(contributionGroupMemberDTO1).isEqualTo(contributionGroupMemberDTO2);
        contributionGroupMemberDTO2.setId(2L);
        assertThat(contributionGroupMemberDTO1).isNotEqualTo(contributionGroupMemberDTO2);
        contributionGroupMemberDTO1.setId(null);
        assertThat(contributionGroupMemberDTO1).isNotEqualTo(contributionGroupMemberDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contributionGroupMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contributionGroupMemberMapper.fromId(null)).isNull();
    }
}
