package org.hcmr.reservationsystem.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.hcmr.reservationsystem.domain.ReservationItem;
import org.hcmr.reservationsystem.domain.*; // for static metamodels
import org.hcmr.reservationsystem.repository.ReservationItemRepository;
import org.hcmr.reservationsystem.repository.search.ReservationItemSearchRepository;
import org.hcmr.reservationsystem.service.dto.ReservationItemCriteria;

/**
 * Service for executing complex queries for ReservationItem entities in the database.
 * The main input is a {@link ReservationItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReservationItem} or a {@link Page} of {@link ReservationItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReservationItemQueryService extends QueryService<ReservationItem> {

    private final Logger log = LoggerFactory.getLogger(ReservationItemQueryService.class);

    private final ReservationItemRepository reservationItemRepository;

    private final ReservationItemSearchRepository reservationItemSearchRepository;

    public ReservationItemQueryService(ReservationItemRepository reservationItemRepository, ReservationItemSearchRepository reservationItemSearchRepository) {
        this.reservationItemRepository = reservationItemRepository;
        this.reservationItemSearchRepository = reservationItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReservationItem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReservationItem> findByCriteria(ReservationItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReservationItem> specification = createSpecification(criteria);
        return reservationItemRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ReservationItem} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReservationItem> findByCriteria(ReservationItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReservationItem> specification = createSpecification(criteria);
        return reservationItemRepository.findAll(specification, page);
    }

    /**
     * Function to convert ReservationItemCriteria to a {@link Specification}
     */
    private Specification<ReservationItem> createSpecification(ReservationItemCriteria criteria) {
        Specification<ReservationItem> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ReservationItem_.id));
            }
            if (criteria.getReservationItemName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReservationItemName(), ReservationItem_.reservationItemName));
            }
            if (criteria.getReservationId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReservationId(), ReservationItem_.reservations, Reservation_.id));
            }
        }
        return specification;
    }
}
