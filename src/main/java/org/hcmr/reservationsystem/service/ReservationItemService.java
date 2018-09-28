package org.hcmr.reservationsystem.service;

import org.hcmr.reservationsystem.domain.ReservationItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ReservationItem.
 */
public interface ReservationItemService {

    /**
     * Save a reservationItem.
     *
     * @param reservationItem the entity to save
     * @return the persisted entity
     */
    ReservationItem save(ReservationItem reservationItem);

    /**
     * Get all the reservationItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReservationItem> findAll(Pageable pageable);


    /**
     * Get the "id" reservationItem.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ReservationItem> findOne(Long id);

    /**
     * Delete the "id" reservationItem.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the reservationItem corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ReservationItem> search(String query, Pageable pageable);
}
