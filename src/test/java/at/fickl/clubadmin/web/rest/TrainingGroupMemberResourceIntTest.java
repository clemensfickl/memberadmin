package at.fickl.clubadmin.web.rest;

import at.fickl.clubadmin.MemberadminApp;

import at.fickl.clubadmin.domain.TrainingGroupMember;
import at.fickl.clubadmin.domain.TrainingGroup;
import at.fickl.clubadmin.domain.Member;
import at.fickl.clubadmin.repository.TrainingGroupMemberRepository;
import at.fickl.clubadmin.service.TrainingGroupMemberService;
import at.fickl.clubadmin.repository.search.TrainingGroupMemberSearchRepository;
import at.fickl.clubadmin.service.dto.TrainingGroupMemberDTO;
import at.fickl.clubadmin.service.mapper.TrainingGroupMemberMapper;
import at.fickl.clubadmin.web.rest.errors.ExceptionTranslator;
import at.fickl.clubadmin.service.dto.TrainingGroupMemberCriteria;
import at.fickl.clubadmin.service.TrainingGroupMemberQueryService;

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
 * Test class for the TrainingGroupMemberResource REST controller.
 *
 * @see TrainingGroupMemberResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MemberadminApp.class)
public class TrainingGroupMemberResourceIntTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private TrainingGroupMemberRepository trainingGroupMemberRepository;

    @Autowired
    private TrainingGroupMemberMapper trainingGroupMemberMapper;

    @Autowired
    private TrainingGroupMemberService trainingGroupMemberService;

    @Autowired
    private TrainingGroupMemberSearchRepository trainingGroupMemberSearchRepository;

    @Autowired
    private TrainingGroupMemberQueryService trainingGroupMemberQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTrainingGroupMemberMockMvc;

    private TrainingGroupMember trainingGroupMember;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TrainingGroupMemberResource trainingGroupMemberResource = new TrainingGroupMemberResource(trainingGroupMemberService, trainingGroupMemberQueryService);
        this.restTrainingGroupMemberMockMvc = MockMvcBuilders.standaloneSetup(trainingGroupMemberResource)
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
    public static TrainingGroupMember createEntity(EntityManager em) {
        TrainingGroupMember trainingGroupMember = new TrainingGroupMember()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return trainingGroupMember;
    }

    @Before
    public void initTest() {
        trainingGroupMemberSearchRepository.deleteAll();
        trainingGroupMember = createEntity(em);
    }

    @Test
    @Transactional
    public void createTrainingGroupMember() throws Exception {
        int databaseSizeBeforeCreate = trainingGroupMemberRepository.findAll().size();

        // Create the TrainingGroupMember
        TrainingGroupMemberDTO trainingGroupMemberDTO = trainingGroupMemberMapper.toDto(trainingGroupMember);
        restTrainingGroupMemberMockMvc.perform(post("/api/training-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingGroupMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the TrainingGroupMember in the database
        List<TrainingGroupMember> trainingGroupMemberList = trainingGroupMemberRepository.findAll();
        assertThat(trainingGroupMemberList).hasSize(databaseSizeBeforeCreate + 1);
        TrainingGroupMember testTrainingGroupMember = trainingGroupMemberList.get(trainingGroupMemberList.size() - 1);
        assertThat(testTrainingGroupMember.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTrainingGroupMember.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the TrainingGroupMember in Elasticsearch
        TrainingGroupMember trainingGroupMemberEs = trainingGroupMemberSearchRepository.findOne(testTrainingGroupMember.getId());
        assertThat(trainingGroupMemberEs).isEqualToIgnoringGivenFields(testTrainingGroupMember);
    }

    @Test
    @Transactional
    public void createTrainingGroupMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = trainingGroupMemberRepository.findAll().size();

        // Create the TrainingGroupMember with an existing ID
        trainingGroupMember.setId(1L);
        TrainingGroupMemberDTO trainingGroupMemberDTO = trainingGroupMemberMapper.toDto(trainingGroupMember);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrainingGroupMemberMockMvc.perform(post("/api/training-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingGroupMemberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrainingGroupMember in the database
        List<TrainingGroupMember> trainingGroupMemberList = trainingGroupMemberRepository.findAll();
        assertThat(trainingGroupMemberList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembers() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList
        restTrainingGroupMemberMockMvc.perform(get("/api/training-group-members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTrainingGroupMember() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get the trainingGroupMember
        restTrainingGroupMemberMockMvc.perform(get("/api/training-group-members/{id}", trainingGroupMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(trainingGroupMember.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where startDate equals to DEFAULT_START_DATE
        defaultTrainingGroupMemberShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the trainingGroupMemberList where startDate equals to UPDATED_START_DATE
        defaultTrainingGroupMemberShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultTrainingGroupMemberShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the trainingGroupMemberList where startDate equals to UPDATED_START_DATE
        defaultTrainingGroupMemberShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where startDate is not null
        defaultTrainingGroupMemberShouldBeFound("startDate.specified=true");

        // Get all the trainingGroupMemberList where startDate is null
        defaultTrainingGroupMemberShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where startDate greater than or equals to DEFAULT_START_DATE
        defaultTrainingGroupMemberShouldBeFound("startDate.greaterOrEqualThan=" + DEFAULT_START_DATE);

        // Get all the trainingGroupMemberList where startDate greater than or equals to UPDATED_START_DATE
        defaultTrainingGroupMemberShouldNotBeFound("startDate.greaterOrEqualThan=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where startDate less than or equals to DEFAULT_START_DATE
        defaultTrainingGroupMemberShouldNotBeFound("startDate.lessThan=" + DEFAULT_START_DATE);

        // Get all the trainingGroupMemberList where startDate less than or equals to UPDATED_START_DATE
        defaultTrainingGroupMemberShouldBeFound("startDate.lessThan=" + UPDATED_START_DATE);
    }


    @Test
    @Transactional
    public void getAllTrainingGroupMembersByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where endDate equals to DEFAULT_END_DATE
        defaultTrainingGroupMemberShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the trainingGroupMemberList where endDate equals to UPDATED_END_DATE
        defaultTrainingGroupMemberShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultTrainingGroupMemberShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the trainingGroupMemberList where endDate equals to UPDATED_END_DATE
        defaultTrainingGroupMemberShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where endDate is not null
        defaultTrainingGroupMemberShouldBeFound("endDate.specified=true");

        // Get all the trainingGroupMemberList where endDate is null
        defaultTrainingGroupMemberShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where endDate greater than or equals to DEFAULT_END_DATE
        defaultTrainingGroupMemberShouldBeFound("endDate.greaterOrEqualThan=" + DEFAULT_END_DATE);

        // Get all the trainingGroupMemberList where endDate greater than or equals to UPDATED_END_DATE
        defaultTrainingGroupMemberShouldNotBeFound("endDate.greaterOrEqualThan=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllTrainingGroupMembersByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);

        // Get all the trainingGroupMemberList where endDate less than or equals to DEFAULT_END_DATE
        defaultTrainingGroupMemberShouldNotBeFound("endDate.lessThan=" + DEFAULT_END_DATE);

        // Get all the trainingGroupMemberList where endDate less than or equals to UPDATED_END_DATE
        defaultTrainingGroupMemberShouldBeFound("endDate.lessThan=" + UPDATED_END_DATE);
    }


    @Test
    @Transactional
    public void getAllTrainingGroupMembersByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        TrainingGroup group = TrainingGroupResourceIntTest.createEntity(em);
        em.persist(group);
        em.flush();
        trainingGroupMember.setGroup(group);
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);
        Long groupId = group.getId();

        // Get all the trainingGroupMemberList where group equals to groupId
        defaultTrainingGroupMemberShouldBeFound("groupId.equals=" + groupId);

        // Get all the trainingGroupMemberList where group equals to groupId + 1
        defaultTrainingGroupMemberShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }


    @Test
    @Transactional
    public void getAllTrainingGroupMembersByMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        Member member = MemberResourceIntTest.createEntity(em);
        em.persist(member);
        em.flush();
        trainingGroupMember.setMember(member);
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);
        Long memberId = member.getId();

        // Get all the trainingGroupMemberList where member equals to memberId
        defaultTrainingGroupMemberShouldBeFound("memberId.equals=" + memberId);

        // Get all the trainingGroupMemberList where member equals to memberId + 1
        defaultTrainingGroupMemberShouldNotBeFound("memberId.equals=" + (memberId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTrainingGroupMemberShouldBeFound(String filter) throws Exception {
        restTrainingGroupMemberMockMvc.perform(get("/api/training-group-members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTrainingGroupMemberShouldNotBeFound(String filter) throws Exception {
        restTrainingGroupMemberMockMvc.perform(get("/api/training-group-members?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTrainingGroupMember() throws Exception {
        // Get the trainingGroupMember
        restTrainingGroupMemberMockMvc.perform(get("/api/training-group-members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTrainingGroupMember() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);
        trainingGroupMemberSearchRepository.save(trainingGroupMember);
        int databaseSizeBeforeUpdate = trainingGroupMemberRepository.findAll().size();

        // Update the trainingGroupMember
        TrainingGroupMember updatedTrainingGroupMember = trainingGroupMemberRepository.findOne(trainingGroupMember.getId());
        // Disconnect from session so that the updates on updatedTrainingGroupMember are not directly saved in db
        em.detach(updatedTrainingGroupMember);
        updatedTrainingGroupMember
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        TrainingGroupMemberDTO trainingGroupMemberDTO = trainingGroupMemberMapper.toDto(updatedTrainingGroupMember);

        restTrainingGroupMemberMockMvc.perform(put("/api/training-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingGroupMemberDTO)))
            .andExpect(status().isOk());

        // Validate the TrainingGroupMember in the database
        List<TrainingGroupMember> trainingGroupMemberList = trainingGroupMemberRepository.findAll();
        assertThat(trainingGroupMemberList).hasSize(databaseSizeBeforeUpdate);
        TrainingGroupMember testTrainingGroupMember = trainingGroupMemberList.get(trainingGroupMemberList.size() - 1);
        assertThat(testTrainingGroupMember.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTrainingGroupMember.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the TrainingGroupMember in Elasticsearch
        TrainingGroupMember trainingGroupMemberEs = trainingGroupMemberSearchRepository.findOne(testTrainingGroupMember.getId());
        assertThat(trainingGroupMemberEs).isEqualToIgnoringGivenFields(testTrainingGroupMember);
    }

    @Test
    @Transactional
    public void updateNonExistingTrainingGroupMember() throws Exception {
        int databaseSizeBeforeUpdate = trainingGroupMemberRepository.findAll().size();

        // Create the TrainingGroupMember
        TrainingGroupMemberDTO trainingGroupMemberDTO = trainingGroupMemberMapper.toDto(trainingGroupMember);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTrainingGroupMemberMockMvc.perform(put("/api/training-group-members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(trainingGroupMemberDTO)))
            .andExpect(status().isCreated());

        // Validate the TrainingGroupMember in the database
        List<TrainingGroupMember> trainingGroupMemberList = trainingGroupMemberRepository.findAll();
        assertThat(trainingGroupMemberList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTrainingGroupMember() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);
        trainingGroupMemberSearchRepository.save(trainingGroupMember);
        int databaseSizeBeforeDelete = trainingGroupMemberRepository.findAll().size();

        // Get the trainingGroupMember
        restTrainingGroupMemberMockMvc.perform(delete("/api/training-group-members/{id}", trainingGroupMember.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean trainingGroupMemberExistsInEs = trainingGroupMemberSearchRepository.exists(trainingGroupMember.getId());
        assertThat(trainingGroupMemberExistsInEs).isFalse();

        // Validate the database is empty
        List<TrainingGroupMember> trainingGroupMemberList = trainingGroupMemberRepository.findAll();
        assertThat(trainingGroupMemberList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTrainingGroupMember() throws Exception {
        // Initialize the database
        trainingGroupMemberRepository.saveAndFlush(trainingGroupMember);
        trainingGroupMemberSearchRepository.save(trainingGroupMember);

        // Search the trainingGroupMember
        restTrainingGroupMemberMockMvc.perform(get("/api/_search/training-group-members?query=id:" + trainingGroupMember.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trainingGroupMember.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingGroupMember.class);
        TrainingGroupMember trainingGroupMember1 = new TrainingGroupMember();
        trainingGroupMember1.setId(1L);
        TrainingGroupMember trainingGroupMember2 = new TrainingGroupMember();
        trainingGroupMember2.setId(trainingGroupMember1.getId());
        assertThat(trainingGroupMember1).isEqualTo(trainingGroupMember2);
        trainingGroupMember2.setId(2L);
        assertThat(trainingGroupMember1).isNotEqualTo(trainingGroupMember2);
        trainingGroupMember1.setId(null);
        assertThat(trainingGroupMember1).isNotEqualTo(trainingGroupMember2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrainingGroupMemberDTO.class);
        TrainingGroupMemberDTO trainingGroupMemberDTO1 = new TrainingGroupMemberDTO();
        trainingGroupMemberDTO1.setId(1L);
        TrainingGroupMemberDTO trainingGroupMemberDTO2 = new TrainingGroupMemberDTO();
        assertThat(trainingGroupMemberDTO1).isNotEqualTo(trainingGroupMemberDTO2);
        trainingGroupMemberDTO2.setId(trainingGroupMemberDTO1.getId());
        assertThat(trainingGroupMemberDTO1).isEqualTo(trainingGroupMemberDTO2);
        trainingGroupMemberDTO2.setId(2L);
        assertThat(trainingGroupMemberDTO1).isNotEqualTo(trainingGroupMemberDTO2);
        trainingGroupMemberDTO1.setId(null);
        assertThat(trainingGroupMemberDTO1).isNotEqualTo(trainingGroupMemberDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(trainingGroupMemberMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(trainingGroupMemberMapper.fromId(null)).isNull();
    }
}
