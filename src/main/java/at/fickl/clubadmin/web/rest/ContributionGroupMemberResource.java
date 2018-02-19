package at.fickl.clubadmin.web.rest;

import com.codahale.metrics.annotation.Timed;
import at.fickl.clubadmin.service.ContributionGroupMemberService;
import at.fickl.clubadmin.web.rest.errors.BadRequestAlertException;
import at.fickl.clubadmin.web.rest.util.HeaderUtil;
import at.fickl.clubadmin.service.dto.ContributionGroupMemberDTO;
import at.fickl.clubadmin.service.dto.ContributionGroupMemberCriteria;
import at.fickl.clubadmin.service.ContributionGroupMemberQueryService;
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
 * REST controller for managing ContributionGroupMember.
 */
@RestController
@RequestMapping("/api")
public class ContributionGroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(ContributionGroupMemberResource.class);

    private static final String ENTITY_NAME = "contributionGroupMember";

    private final ContributionGroupMemberService contributionGroupMemberService;

    private final ContributionGroupMemberQueryService contributionGroupMemberQueryService;

    public ContributionGroupMemberResource(ContributionGroupMemberService contributionGroupMemberService, ContributionGroupMemberQueryService contributionGroupMemberQueryService) {
        this.contributionGroupMemberService = contributionGroupMemberService;
        this.contributionGroupMemberQueryService = contributionGroupMemberQueryService;
    }

    /**
     * POST  /contribution-group-members : Create a new contributionGroupMember.
     *
     * @param contributionGroupMemberDTO the contributionGroupMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contributionGroupMemberDTO, or with status 400 (Bad Request) if the contributionGroupMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contribution-group-members")
    @Timed
    public ResponseEntity<ContributionGroupMemberDTO> createContributionGroupMember(@RequestBody ContributionGroupMemberDTO contributionGroupMemberDTO) throws URISyntaxException {
        log.debug("REST request to save ContributionGroupMember : {}", contributionGroupMemberDTO);
        if (contributionGroupMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new contributionGroupMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContributionGroupMemberDTO result = contributionGroupMemberService.save(contributionGroupMemberDTO);
        return ResponseEntity.created(new URI("/api/contribution-group-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contribution-group-members : Updates an existing contributionGroupMember.
     *
     * @param contributionGroupMemberDTO the contributionGroupMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contributionGroupMemberDTO,
     * or with status 400 (Bad Request) if the contributionGroupMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the contributionGroupMemberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contribution-group-members")
    @Timed
    public ResponseEntity<ContributionGroupMemberDTO> updateContributionGroupMember(@RequestBody ContributionGroupMemberDTO contributionGroupMemberDTO) throws URISyntaxException {
        log.debug("REST request to update ContributionGroupMember : {}", contributionGroupMemberDTO);
        if (contributionGroupMemberDTO.getId() == null) {
            return createContributionGroupMember(contributionGroupMemberDTO);
        }
        ContributionGroupMemberDTO result = contributionGroupMemberService.save(contributionGroupMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contributionGroupMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contribution-group-members : get all the contributionGroupMembers.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of contributionGroupMembers in body
     */
    @GetMapping("/contribution-group-members")
    @Timed
    public ResponseEntity<List<ContributionGroupMemberDTO>> getAllContributionGroupMembers(ContributionGroupMemberCriteria criteria) {
        log.debug("REST request to get ContributionGroupMembers by criteria: {}", criteria);
        List<ContributionGroupMemberDTO> entityList = contributionGroupMemberQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /contribution-group-members/:id : get the "id" contributionGroupMember.
     *
     * @param id the id of the contributionGroupMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contributionGroupMemberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contribution-group-members/{id}")
    @Timed
    public ResponseEntity<ContributionGroupMemberDTO> getContributionGroupMember(@PathVariable Long id) {
        log.debug("REST request to get ContributionGroupMember : {}", id);
        ContributionGroupMemberDTO contributionGroupMemberDTO = contributionGroupMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contributionGroupMemberDTO));
    }

    /**
     * DELETE  /contribution-group-members/:id : delete the "id" contributionGroupMember.
     *
     * @param id the id of the contributionGroupMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contribution-group-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteContributionGroupMember(@PathVariable Long id) {
        log.debug("REST request to delete ContributionGroupMember : {}", id);
        contributionGroupMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contribution-group-members?query=:query : search for the contributionGroupMember corresponding
     * to the query.
     *
     * @param query the query of the contributionGroupMember search
     * @return the result of the search
     */
    @GetMapping("/_search/contribution-group-members")
    @Timed
    public List<ContributionGroupMemberDTO> searchContributionGroupMembers(@RequestParam String query) {
        log.debug("REST request to search ContributionGroupMembers for query {}", query);
        return contributionGroupMemberService.search(query);
    }

}
