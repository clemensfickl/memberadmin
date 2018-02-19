package at.fickl.clubadmin.web.rest;

import at.fickl.clubadmin.MemberadminApp;

import at.fickl.clubadmin.domain.ContributionGroupEntry;
import at.fickl.clubadmin.domain.ContributionGroup;
import at.fickl.clubadmin.repository.ContributionGroupEntryRepository;
import at.fickl.clubadmin.service.ContributionGroupEntryService;
import at.fickl.clubadmin.repository.search.ContributionGroupEntrySearchRepository;
import at.fickl.clubadmin.service.dto.ContributionGroupEntryDTO;
import at.fickl.clubadmin.service.mapper.ContributionGroupEntryMapper;
import at.fickl.clubadmin.web.rest.errors.ExceptionTranslator;
import at.fickl.clubadmin.service.dto.ContributionGroupEntryCriteria;
import at.fickl.clubadmin.service.ContributionGroupEntryQueryService;

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
import java.math.BigDecimal;
import java.util.List;

import static at.fickl.clubadmin.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContributionGroupEntryResource REST controller.
 *
 * @see ContributionGroupEntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MemberadminApp.class)
public class ContributionGroupEntryResourceIntTest {

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Autowired
    private ContributionGroupEntryRepository contributionGroupEntryRepository;

    @Autowired
    private ContributionGroupEntryMapper contributionGroupEntryMapper;

    @Autowired
    private ContributionGroupEntryService contributionGroupEntryService;

    @Autowired
    private ContributionGroupEntrySearchRepository contributionGroupEntrySearchRepository;

    @Autowired
    private ContributionGroupEntryQueryService contributionGroupEntryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restContributionGroupEntryMockMvc;

    private ContributionGroupEntry contributionGroupEntry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ContributionGroupEntryResource contributionGroupEntryResource = new ContributionGroupEntryResource(contributionGroupEntryService, contributionGroupEntryQueryService);
        this.restContributionGroupEntryMockMvc = MockMvcBuilders.standaloneSetup(contributionGroupEntryResource)
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
    public static ContributionGroupEntry createEntity(EntityManager em) {
        ContributionGroupEntry contributionGroupEntry = new ContributionGroupEntry()
            .year(DEFAULT_YEAR)
            .amount(DEFAULT_AMOUNT);
        return contributionGroupEntry;
    }

    @Before
    public void initTest() {
        contributionGroupEntrySearchRepository.deleteAll();
        contributionGroupEntry = createEntity(em);
    }

    @Test
    @Transactional
    public void createContributionGroupEntry() throws Exception {
        int databaseSizeBeforeCreate = contributionGroupEntryRepository.findAll().size();

        // Create the ContributionGroupEntry
        ContributionGroupEntryDTO contributionGroupEntryDTO = contributionGroupEntryMapper.toDto(contributionGroupEntry);
        restContributionGroupEntryMockMvc.perform(post("/api/contribution-group-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the ContributionGroupEntry in the database
        List<ContributionGroupEntry> contributionGroupEntryList = contributionGroupEntryRepository.findAll();
        assertThat(contributionGroupEntryList).hasSize(databaseSizeBeforeCreate + 1);
        ContributionGroupEntry testContributionGroupEntry = contributionGroupEntryList.get(contributionGroupEntryList.size() - 1);
        assertThat(testContributionGroupEntry.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testContributionGroupEntry.getAmount()).isEqualTo(DEFAULT_AMOUNT);

        // Validate the ContributionGroupEntry in Elasticsearch
        ContributionGroupEntry contributionGroupEntryEs = contributionGroupEntrySearchRepository.findOne(testContributionGroupEntry.getId());
        assertThat(contributionGroupEntryEs).isEqualToIgnoringGivenFields(testContributionGroupEntry);
    }

    @Test
    @Transactional
    public void createContributionGroupEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = contributionGroupEntryRepository.findAll().size();

        // Create the ContributionGroupEntry with an existing ID
        contributionGroupEntry.setId(1L);
        ContributionGroupEntryDTO contributionGroupEntryDTO = contributionGroupEntryMapper.toDto(contributionGroupEntry);

        // An entity with an existing ID cannot be created, so this API call must fail
        restContributionGroupEntryMockMvc.perform(post("/api/contribution-group-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupEntryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContributionGroupEntry in the database
        List<ContributionGroupEntry> contributionGroupEntryList = contributionGroupEntryRepository.findAll();
        assertThat(contributionGroupEntryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntries() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList
        restContributionGroupEntryMockMvc.perform(get("/api/contribution-group-entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionGroupEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getContributionGroupEntry() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get the contributionGroupEntry
        restContributionGroupEntryMockMvc.perform(get("/api/contribution-group-entries/{id}", contributionGroupEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contributionGroupEntry.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where year equals to DEFAULT_YEAR
        defaultContributionGroupEntryShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the contributionGroupEntryList where year equals to UPDATED_YEAR
        defaultContributionGroupEntryShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByYearIsInShouldWork() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultContributionGroupEntryShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the contributionGroupEntryList where year equals to UPDATED_YEAR
        defaultContributionGroupEntryShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where year is not null
        defaultContributionGroupEntryShouldBeFound("year.specified=true");

        // Get all the contributionGroupEntryList where year is null
        defaultContributionGroupEntryShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where year greater than or equals to DEFAULT_YEAR
        defaultContributionGroupEntryShouldBeFound("year.greaterOrEqualThan=" + DEFAULT_YEAR);

        // Get all the contributionGroupEntryList where year greater than or equals to UPDATED_YEAR
        defaultContributionGroupEntryShouldNotBeFound("year.greaterOrEqualThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where year less than or equals to DEFAULT_YEAR
        defaultContributionGroupEntryShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the contributionGroupEntryList where year less than or equals to UPDATED_YEAR
        defaultContributionGroupEntryShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }


    @Test
    @Transactional
    public void getAllContributionGroupEntriesByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where amount equals to DEFAULT_AMOUNT
        defaultContributionGroupEntryShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the contributionGroupEntryList where amount equals to UPDATED_AMOUNT
        defaultContributionGroupEntryShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultContributionGroupEntryShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the contributionGroupEntryList where amount equals to UPDATED_AMOUNT
        defaultContributionGroupEntryShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);

        // Get all the contributionGroupEntryList where amount is not null
        defaultContributionGroupEntryShouldBeFound("amount.specified=true");

        // Get all the contributionGroupEntryList where amount is null
        defaultContributionGroupEntryShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    public void getAllContributionGroupEntriesByGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        ContributionGroup group = ContributionGroupResourceIntTest.createEntity(em);
        em.persist(group);
        em.flush();
        contributionGroupEntry.setGroup(group);
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);
        Long groupId = group.getId();

        // Get all the contributionGroupEntryList where group equals to groupId
        defaultContributionGroupEntryShouldBeFound("groupId.equals=" + groupId);

        // Get all the contributionGroupEntryList where group equals to groupId + 1
        defaultContributionGroupEntryShouldNotBeFound("groupId.equals=" + (groupId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultContributionGroupEntryShouldBeFound(String filter) throws Exception {
        restContributionGroupEntryMockMvc.perform(get("/api/contribution-group-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionGroupEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultContributionGroupEntryShouldNotBeFound(String filter) throws Exception {
        restContributionGroupEntryMockMvc.perform(get("/api/contribution-group-entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingContributionGroupEntry() throws Exception {
        // Get the contributionGroupEntry
        restContributionGroupEntryMockMvc.perform(get("/api/contribution-group-entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContributionGroupEntry() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);
        contributionGroupEntrySearchRepository.save(contributionGroupEntry);
        int databaseSizeBeforeUpdate = contributionGroupEntryRepository.findAll().size();

        // Update the contributionGroupEntry
        ContributionGroupEntry updatedContributionGroupEntry = contributionGroupEntryRepository.findOne(contributionGroupEntry.getId());
        // Disconnect from session so that the updates on updatedContributionGroupEntry are not directly saved in db
        em.detach(updatedContributionGroupEntry);
        updatedContributionGroupEntry
            .year(UPDATED_YEAR)
            .amount(UPDATED_AMOUNT);
        ContributionGroupEntryDTO contributionGroupEntryDTO = contributionGroupEntryMapper.toDto(updatedContributionGroupEntry);

        restContributionGroupEntryMockMvc.perform(put("/api/contribution-group-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupEntryDTO)))
            .andExpect(status().isOk());

        // Validate the ContributionGroupEntry in the database
        List<ContributionGroupEntry> contributionGroupEntryList = contributionGroupEntryRepository.findAll();
        assertThat(contributionGroupEntryList).hasSize(databaseSizeBeforeUpdate);
        ContributionGroupEntry testContributionGroupEntry = contributionGroupEntryList.get(contributionGroupEntryList.size() - 1);
        assertThat(testContributionGroupEntry.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testContributionGroupEntry.getAmount()).isEqualTo(UPDATED_AMOUNT);

        // Validate the ContributionGroupEntry in Elasticsearch
        ContributionGroupEntry contributionGroupEntryEs = contributionGroupEntrySearchRepository.findOne(testContributionGroupEntry.getId());
        assertThat(contributionGroupEntryEs).isEqualToIgnoringGivenFields(testContributionGroupEntry);
    }

    @Test
    @Transactional
    public void updateNonExistingContributionGroupEntry() throws Exception {
        int databaseSizeBeforeUpdate = contributionGroupEntryRepository.findAll().size();

        // Create the ContributionGroupEntry
        ContributionGroupEntryDTO contributionGroupEntryDTO = contributionGroupEntryMapper.toDto(contributionGroupEntry);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restContributionGroupEntryMockMvc.perform(put("/api/contribution-group-entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(contributionGroupEntryDTO)))
            .andExpect(status().isCreated());

        // Validate the ContributionGroupEntry in the database
        List<ContributionGroupEntry> contributionGroupEntryList = contributionGroupEntryRepository.findAll();
        assertThat(contributionGroupEntryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteContributionGroupEntry() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);
        contributionGroupEntrySearchRepository.save(contributionGroupEntry);
        int databaseSizeBeforeDelete = contributionGroupEntryRepository.findAll().size();

        // Get the contributionGroupEntry
        restContributionGroupEntryMockMvc.perform(delete("/api/contribution-group-entries/{id}", contributionGroupEntry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean contributionGroupEntryExistsInEs = contributionGroupEntrySearchRepository.exists(contributionGroupEntry.getId());
        assertThat(contributionGroupEntryExistsInEs).isFalse();

        // Validate the database is empty
        List<ContributionGroupEntry> contributionGroupEntryList = contributionGroupEntryRepository.findAll();
        assertThat(contributionGroupEntryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchContributionGroupEntry() throws Exception {
        // Initialize the database
        contributionGroupEntryRepository.saveAndFlush(contributionGroupEntry);
        contributionGroupEntrySearchRepository.save(contributionGroupEntry);

        // Search the contributionGroupEntry
        restContributionGroupEntryMockMvc.perform(get("/api/_search/contribution-group-entries?query=id:" + contributionGroupEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contributionGroupEntry.getId().intValue())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionGroupEntry.class);
        ContributionGroupEntry contributionGroupEntry1 = new ContributionGroupEntry();
        contributionGroupEntry1.setId(1L);
        ContributionGroupEntry contributionGroupEntry2 = new ContributionGroupEntry();
        contributionGroupEntry2.setId(contributionGroupEntry1.getId());
        assertThat(contributionGroupEntry1).isEqualTo(contributionGroupEntry2);
        contributionGroupEntry2.setId(2L);
        assertThat(contributionGroupEntry1).isNotEqualTo(contributionGroupEntry2);
        contributionGroupEntry1.setId(null);
        assertThat(contributionGroupEntry1).isNotEqualTo(contributionGroupEntry2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContributionGroupEntryDTO.class);
        ContributionGroupEntryDTO contributionGroupEntryDTO1 = new ContributionGroupEntryDTO();
        contributionGroupEntryDTO1.setId(1L);
        ContributionGroupEntryDTO contributionGroupEntryDTO2 = new ContributionGroupEntryDTO();
        assertThat(contributionGroupEntryDTO1).isNotEqualTo(contributionGroupEntryDTO2);
        contributionGroupEntryDTO2.setId(contributionGroupEntryDTO1.getId());
        assertThat(contributionGroupEntryDTO1).isEqualTo(contributionGroupEntryDTO2);
        contributionGroupEntryDTO2.setId(2L);
        assertThat(contributionGroupEntryDTO1).isNotEqualTo(contributionGroupEntryDTO2);
        contributionGroupEntryDTO1.setId(null);
        assertThat(contributionGroupEntryDTO1).isNotEqualTo(contributionGroupEntryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(contributionGroupEntryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(contributionGroupEntryMapper.fromId(null)).isNull();
    }
}
