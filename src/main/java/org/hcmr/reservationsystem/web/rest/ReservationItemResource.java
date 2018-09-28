package org.hcmr.reservationsystem.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.hcmr.reservationsystem.domain.ReservationItem;
import org.hcmr.reservationsystem.service.ReservationItemService;
import org.hcmr.reservationsystem.web.rest.errors.BadRequestAlertException;
import org.hcmr.reservationsystem.web.rest.util.HeaderUtil;
import org.hcmr.reservationsystem.web.rest.util.PaginationUtil;
import org.hcmr.reservationsystem.service.dto.ReservationItemCriteria;
import org.hcmr.reservationsystem.service.ReservationItemQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ReservationItem.
 */
@RestController
@RequestMapping("/api")
public class ReservationItemResource {

    private final Logger log = LoggerFactory.getLogger(ReservationItemResource.class);

    private static final String ENTITY_NAME = "reservationItem";

    private final ReservationItemService reservationItemService;

    private final ReservationItemQueryService reservationItemQueryService;

    public ReservationItemResource(ReservationItemService reservationItemService, ReservationItemQueryService reservationItemQueryService) {
        this.reservationItemService = reservationItemService;
        this.reservationItemQueryService = reservationItemQueryService;
    }

    /**
     * POST  /reservation-items : Create a new reservationItem.
     *
     * @param reservationItem the reservationItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new reservationItem, or with status 400 (Bad Request) if the reservationItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/reservation-items")
    @Timed
    public ResponseEntity<ReservationItem> createReservationItem(@Valid @RequestBody ReservationItem reservationItem) throws URISyntaxException {
        log.debug("REST request to save ReservationItem : {}", reservationItem);
        if (reservationItem.getId() != null) {
            throw new BadRequestAlertException("A new reservationItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReservationItem result = reservationItemService.save(reservationItem);
        return ResponseEntity.created(new URI("/api/reservation-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /reservation-items : Updates an existing reservationItem.
     *
     * @param reservationItem the reservationItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated reservationItem,
     * or with status 400 (Bad Request) if the reservationItem is not valid,
     * or with status 500 (Internal Server Error) if the reservationItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/reservation-items")
    @Timed
    public ResponseEntity<ReservationItem> updateReservationItem(@Valid @RequestBody ReservationItem reservationItem) throws URISyntaxException {
        log.debug("REST request to update ReservationItem : {}", reservationItem);
        if (reservationItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReservationItem result = reservationItemService.save(reservationItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, reservationItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /reservation-items : get all the reservationItems.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of reservationItems in body
     */
    @GetMapping("/reservation-items")
    @Timed
    public ResponseEntity<List<ReservationItem>> getAllReservationItems(ReservationItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReservationItems by criteria: {}", criteria);
        Page<ReservationItem> page = reservationItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/reservation-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /reservation-items/:id : get the "id" reservationItem.
     *
     * @param id the id of the reservationItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the reservationItem, or with status 404 (Not Found)
     */
    @GetMapping("/reservation-items/{id}")
    @Timed
    public ResponseEntity<ReservationItem> getReservationItem(@PathVariable Long id) {
        log.debug("REST request to get ReservationItem : {}", id);
        Optional<ReservationItem> reservationItem = reservationItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reservationItem);
    }

    /**
     * DELETE  /reservation-items/:id : delete the "id" reservationItem.
     *
     * @param id the id of the reservationItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/reservation-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteReservationItem(@PathVariable Long id) {
        log.debug("REST request to delete ReservationItem : {}", id);
        reservationItemService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/reservation-items?query=:query : search for the reservationItem corresponding
     * to the query.
     *
     * @param query the query of the reservationItem search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/reservation-items")
    @Timed
    public ResponseEntity<List<ReservationItem>> searchReservationItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReservationItems for query {}", query);
        Page<ReservationItem> page = reservationItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/reservation-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
