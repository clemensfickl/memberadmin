package at.fickl.clubadmin.web.rest;

import com.codahale.metrics.annotation.Timed;
import at.fickl.clubadmin.service.TrainingGroupMemberService;
import at.fickl.clubadmin.web.rest.errors.BadRequestAlertException;
import at.fickl.clubadmin.web.rest.util.HeaderUtil;
import at.fickl.clubadmin.service.dto.TrainingGroupMemberDTO;
import at.fickl.clubadmin.service.dto.TrainingGroupMemberCriteria;
import at.fickl.clubadmin.service.TrainingGroupMemberQueryService;
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
 * REST controller for managing TrainingGroupMember.
 */
@RestController
@RequestMapping("/api")
public class TrainingGroupMemberResource {

    private final Logger log = LoggerFactory.getLogger(TrainingGroupMemberResource.class);

    private static final String ENTITY_NAME = "trainingGroupMember";

    private final TrainingGroupMemberService trainingGroupMemberService;

    private final TrainingGroupMemberQueryService trainingGroupMemberQueryService;

    public TrainingGroupMemberResource(TrainingGroupMemberService trainingGroupMemberService, TrainingGroupMemberQueryService trainingGroupMemberQueryService) {
        this.trainingGroupMemberService = trainingGroupMemberService;
        this.trainingGroupMemberQueryService = trainingGroupMemberQueryService;
    }

    /**
     * POST  /training-group-members : Create a new trainingGroupMember.
     *
     * @param trainingGroupMemberDTO the trainingGroupMemberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trainingGroupMemberDTO, or with status 400 (Bad Request) if the trainingGroupMember has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/training-group-members")
    @Timed
    public ResponseEntity<TrainingGroupMemberDTO> createTrainingGroupMember(@RequestBody TrainingGroupMemberDTO trainingGroupMemberDTO) throws URISyntaxException {
        log.debug("REST request to save TrainingGroupMember : {}", trainingGroupMemberDTO);
        if (trainingGroupMemberDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingGroupMember cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingGroupMemberDTO result = trainingGroupMemberService.save(trainingGroupMemberDTO);
        return ResponseEntity.created(new URI("/api/training-group-members/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /training-group-members : Updates an existing trainingGroupMember.
     *
     * @param trainingGroupMemberDTO the trainingGroupMemberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trainingGroupMemberDTO,
     * or with status 400 (Bad Request) if the trainingGroupMemberDTO is not valid,
     * or with status 500 (Internal Server Error) if the trainingGroupMemberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/training-group-members")
    @Timed
    public ResponseEntity<TrainingGroupMemberDTO> updateTrainingGroupMember(@RequestBody TrainingGroupMemberDTO trainingGroupMemberDTO) throws URISyntaxException {
        log.debug("REST request to update TrainingGroupMember : {}", trainingGroupMemberDTO);
        if (trainingGroupMemberDTO.getId() == null) {
            return createTrainingGroupMember(trainingGroupMemberDTO);
        }
        TrainingGroupMemberDTO result = trainingGroupMemberService.save(trainingGroupMemberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trainingGroupMemberDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /training-group-members : get all the trainingGroupMembers.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of trainingGroupMembers in body
     */
    @GetMapping("/training-group-members")
    @Timed
    public ResponseEntity<List<TrainingGroupMemberDTO>> getAllTrainingGroupMembers(TrainingGroupMemberCriteria criteria) {
        log.debug("REST request to get TrainingGroupMembers by criteria: {}", criteria);
        List<TrainingGroupMemberDTO> entityList = trainingGroupMemberQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /training-group-members/:id : get the "id" trainingGroupMember.
     *
     * @param id the id of the trainingGroupMemberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trainingGroupMemberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/training-group-members/{id}")
    @Timed
    public ResponseEntity<TrainingGroupMemberDTO> getTrainingGroupMember(@PathVariable Long id) {
        log.debug("REST request to get TrainingGroupMember : {}", id);
        TrainingGroupMemberDTO trainingGroupMemberDTO = trainingGroupMemberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trainingGroupMemberDTO));
    }

    /**
     * DELETE  /training-group-members/:id : delete the "id" trainingGroupMember.
     *
     * @param id the id of the trainingGroupMemberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/training-group-members/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrainingGroupMember(@PathVariable Long id) {
        log.debug("REST request to delete TrainingGroupMember : {}", id);
        trainingGroupMemberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/training-group-members?query=:query : search for the trainingGroupMember corresponding
     * to the query.
     *
     * @param query the query of the trainingGroupMember search
     * @return the result of the search
     */
    @GetMapping("/_search/training-group-members")
    @Timed
    public List<TrainingGroupMemberDTO> searchTrainingGroupMembers(@RequestParam String query) {
        log.debug("REST request to search TrainingGroupMembers for query {}", query);
        return trainingGroupMemberService.search(query);
    }

}
