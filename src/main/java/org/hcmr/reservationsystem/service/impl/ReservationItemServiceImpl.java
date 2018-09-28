package org.hcmr.reservationsystem.service.impl;

import org.hcmr.reservationsystem.service.ReservationItemService;
import org.hcmr.reservationsystem.domain.ReservationItem;
import org.hcmr.reservationsystem.repository.ReservationItemRepository;
import org.hcmr.reservationsystem.repository.search.ReservationItemSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing ReservationItem.
 */
@Service
@Transactional
public class ReservationItemServiceImpl implements ReservationItemService {

    private final Logger log = LoggerFactory.getLogger(ReservationItemServiceImpl.class);

    private final ReservationItemRepository reservationItemRepository;

    private final ReservationItemSearchRepository reservationItemSearchRepository;

    public ReservationItemServiceImpl(ReservationItemRepository reservationItemRepository, ReservationItemSearchRepository reservationItemSearchRepository) {
        this.reservationItemRepository = reservationItemRepository;
        this.reservationItemSearchRepository = reservationItemSearchRepository;
    }

    /**
     * Save a reservationItem.
     *
     * @param reservationItem the entity to save
     * @return the persisted entity
     */
    @Override
    public ReservationItem save(ReservationItem reservationItem) {
        log.debug("Request to save ReservationItem : {}", reservationItem);        ReservationItem result = reservationItemRepository.save(reservationItem);
        reservationItemSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the reservationItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservationItem> findAll(Pageable pageable) {
        log.debug("Request to get all ReservationItems");
        return reservationItemRepository.findAll(pageable);
    }


    /**
     * Get one reservationItem by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReservationItem> findOne(Long id) {
        log.debug("Request to get ReservationItem : {}", id);
        return reservationItemRepository.findById(id);
    }

    /**
     * Delete the reservationItem by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReservationItem : {}", id);
        reservationItemRepository.deleteById(id);
        reservationItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the reservationItem corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReservationItem> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReservationItems for query {}", query);
        return reservationItemSearchRepository.search(queryStringQuery(query), pageable);    }
}
