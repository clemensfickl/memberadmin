package at.fickl.clubadmin.web.rest;

import com.codahale.metrics.annotation.Timed;
import at.fickl.clubadmin.service.TrainingGroupService;
import at.fickl.clubadmin.web.rest.errors.BadRequestAlertException;
import at.fickl.clubadmin.web.rest.util.HeaderUtil;
import at.fickl.clubadmin.service.dto.TrainingGroupDTO;
import at.fickl.clubadmin.service.dto.TrainingGroupCriteria;
import at.fickl.clubadmin.service.TrainingGroupQueryService;
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
 * REST controller for managing TrainingGroup.
 */
@RestController
@RequestMapping("/api")
public class TrainingGroupResource {

    private final Logger log = LoggerFactory.getLogger(TrainingGroupResource.class);

    private static final String ENTITY_NAME = "trainingGroup";

    private final TrainingGroupService trainingGroupService;

    private final TrainingGroupQueryService trainingGroupQueryService;

    public TrainingGroupResource(TrainingGroupService trainingGroupService, TrainingGroupQueryService trainingGroupQueryService) {
        this.trainingGroupService = trainingGroupService;
        this.trainingGroupQueryService = trainingGroupQueryService;
    }

    /**
     * POST  /training-groups : Create a new trainingGroup.
     *
     * @param trainingGroupDTO the trainingGroupDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trainingGroupDTO, or with status 400 (Bad Request) if the trainingGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/training-groups")
    @Timed
    public ResponseEntity<TrainingGroupDTO> createTrainingGroup(@RequestBody TrainingGroupDTO trainingGroupDTO) throws URISyntaxException {
        log.debug("REST request to save TrainingGroup : {}", trainingGroupDTO);
        if (trainingGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new trainingGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrainingGroupDTO result = trainingGroupService.save(trainingGroupDTO);
        return ResponseEntity.created(new URI("/api/training-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /training-groups : Updates an existing trainingGroup.
     *
     * @param trainingGroupDTO the trainingGroupDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trainingGroupDTO,
     * or with status 400 (Bad Request) if the trainingGroupDTO is not valid,
     * or with status 500 (Internal Server Error) if the trainingGroupDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/training-groups")
    @Timed
    public ResponseEntity<TrainingGroupDTO> updateTrainingGroup(@RequestBody TrainingGroupDTO trainingGroupDTO) throws URISyntaxException {
        log.debug("REST request to update TrainingGroup : {}", trainingGroupDTO);
        if (trainingGroupDTO.getId() == null) {
            return createTrainingGroup(trainingGroupDTO);
        }
        TrainingGroupDTO result = trainingGroupService.save(trainingGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trainingGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /training-groups : get all the trainingGroups.
     *
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of trainingGroups in body
     */
    @GetMapping("/training-groups")
    @Timed
    public ResponseEntity<List<TrainingGroupDTO>> getAllTrainingGroups(TrainingGroupCriteria criteria) {
        log.debug("REST request to get TrainingGroups by criteria: {}", criteria);
        List<TrainingGroupDTO> entityList = trainingGroupQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * GET  /training-groups/:id : get the "id" trainingGroup.
     *
     * @param id the id of the trainingGroupDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trainingGroupDTO, or with status 404 (Not Found)
     */
    @GetMapping("/training-groups/{id}")
    @Timed
    public ResponseEntity<TrainingGroupDTO> getTrainingGroup(@PathVariable Long id) {
        log.debug("REST request to get TrainingGroup : {}", id);
        TrainingGroupDTO trainingGroupDTO = trainingGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(trainingGroupDTO));
    }

    /**
     * DELETE  /training-groups/:id : delete the "id" trainingGroup.
     *
     * @param id the id of the trainingGroupDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/training-groups/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrainingGroup(@PathVariable Long id) {
        log.debug("REST request to delete TrainingGroup : {}", id);
        trainingGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/training-groups?query=:query : search for the trainingGroup corresponding
     * to the query.
     *
     * @param query the query of the trainingGroup search
     * @return the result of the search
     */
    @GetMapping("/_search/training-groups")
    @Timed
    public List<TrainingGroupDTO> searchTrainingGroups(@RequestParam String query) {
        log.debug("REST request to search TrainingGroups for query {}", query);
        return trainingGroupService.search(query);
    }

}
