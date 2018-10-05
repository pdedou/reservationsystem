package org.hcmr.reservationsystem.service;

import org.hcmr.reservationsystem.domain.Reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Reservation.
 */
public interface ReservationService {

    /**
     * Save a reservation.
     *
     * @param reservation the entity to save
     * @return the persisted entity
     */
    Reservation save(Reservation reservation);

    /**
     * Get all the reservations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Reservation> findAll(Pageable pageable);


    /**
     * Get the "id" reservation.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Reservation> findOne(Long id);

    /**
     * Delete the "id" reservation.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the reservation corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Reservation> search(String query, Pageable pageable);
}
