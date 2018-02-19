package at.fickl.clubadmin.web.rest;

import com.codahale.metrics.annotation.Timed;
import at.fickl.clubadmin.service.ContributionGroupService;
import at.fickl.clubadmin.web.rest.errors.BadRequestAlertException;
import at.fickl.clubadmin.web.rest.util.HeaderUtil;
import at.fickl.clubadmin.service.dto.ContributionGroupDTO;
import at.fickl.clubadmin.service.dto.ContributionGroupCriteria;
import at.fickl.clubadmin.service.ContributionGroupQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ContributionGroup.
 */
@RestController
@RequestMapping("/api")
public class ContributionGroupResource {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupResource.class);

    private static final String ENTITY_NAME = "contributionGroup";

    private final ContributionGroupService contributionGroupService;

    private final ContributionGroupQueryService contributionGroupQueryService;

    public ContributionGroupResource(ContributionGroupService contributionGroupService, ContributionGroupQueryService contributionGroupQueryService) {
        this.contributionGroupService = contributionGroupService;
        this.contributionGroupQueryService = contributionGroupQueryService;
    }

    /**
     * POST  /contribution-groups : Create a new contributionGroup.
     *
     * @param contributionGroupDTO the contributionGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contributionGroupDTO, or with status 400 (Bad Request) if the contributionGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contribution-groups")
    @Timed
    public ResponseEntity<ContributionGroupDTO> createContributionGroup(@RequestBody ContributionGroupDTO contributionGroupDTO) throws URISyntaxException {
        log.debug("REST request to save ContributionGroup : {}", contributionGroupDTO);
        if (contributionGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new contributionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributionGroupDTO result = contributionGroupService.save(contributionGroupDTO);
        return ResponseEntity.created(new URI("/api/contribution-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contribution-groups : Updates an existing contributionGroup.
     *
     * @param contributionGroupDTO the contributionGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contributionGroupDTO,
     * or with status 400 (Bad Request) if the contributionGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the contributionGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contribution-groups")
    @Timed
    public ResponseEntity<ContributionGroupDTO> updateContributionGroup(@RequestBody ContributionGroupDTO contributionGroupDTO) throws URISyntaxException {
        log.debug("REST request to update ContributionGroup : {}", contributionGroupDTO);
        if (contributionGroupDTO.getId() == null) {
            return createContributionGroup(contributionGroupDTO);
        }
        ContributionGroupDTO result = contributionGroupService.save(contributionGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contributionGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contribution-groups : get all the contributionGroups.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of contributionGroups in body
     */
    @GetMapping("/contribution-groups")
    @Timed
    public ResponseEntity<List<ContributionGroupDTO>> getAllContributionGroups(ContributionGroupCriteria criteria) {
        log.debug("REST request to get ContributionGroups by criteria: {}", criteria);
        List<ContributionGroupDTO> entityList = contributionGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /contribution-groups/:id : get the "id" contributionGroup.
     *
     * @param id the id of the contributionGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contributionGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contribution-groups/{id}")
    @Timed
    public ResponseEntity<ContributionGroupDTO> getContributionGroup(@PathVariable Long id) {
        log.debug("REST request to get ContributionGroup : {}", id);
        ContributionGroupDTO contributionGroupDTO = contributionGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contributionGroupDTO));
    }

    /**
     * DELETE  /contribution-groups/:id : delete the "id" contributionGroup.
     *
     * @param id the id of the contributionGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contribution-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteContributionGroup(@PathVariable Long id) {
        log.debug("REST request to delete ContributionGroup : {}", id);
        contributionGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contribution-groups?query=:query : search for the contributionGroup corresponding
     * to the query.
     *
     * @param query the query of the contributionGroup search
     * @return the result of the search
     */
    @GetMapping("/_search/contribution-groups")
    @Timed
    public List<ContributionGroupDTO> searchContributionGroups(@RequestParam String query) {
        log.debug("REST request to search ContributionGroups for query {}", query);
        return contributionGroupService.search(query);
    }

}
