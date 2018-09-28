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

import org.hcmr.reservationsystem.domain.Reservation;
import org.hcmr.reservationsystem.domain.*; // for static metamodels
import org.hcmr.reservationsystem.repository.ReservationRepository;
import org.hcmr.reservationsystem.repository.search.ReservationSearchRepository;
import org.hcmr.reservationsystem.service.dto.ReservationCriteria;

/**
 * Service for executing complex queries for Reservation entities in the database.
 * The main input is a {@link ReservationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Reservation} or a {@link Page} of {@link Reservation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReservationQueryService extends QueryService<Reservation> {

    private final Logger log = LoggerFactory.getLogger(ReservationQueryService.class);

    private final ReservationRepository reservationRepository;

    private final ReservationSearchRepository reservationSearchRepository;

    public ReservationQueryService(ReservationRepository reservationRepository, ReservationSearchRepository reservationSearchRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationSearchRepository = reservationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link Reservation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByCriteria(ReservationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Reservation} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reservation> specification = createSpecification(criteria);
        return reservationRepository.findAll(specification, page);
    }

    /**
     * Function to convert ReservationCriteria to a {@link Specification}
     */
    private Specification<Reservation> createSpecification(ReservationCriteria criteria) {
        Specification<Reservation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Reservation_.id));
            }
            if (criteria.getReservationEntryUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReservationEntryUser(), Reservation_.reservationEntryUser));
            }
            if (criteria.getReservationEntryTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReservationEntryTimestamp(), Reservation_.reservationEntryTimestamp));
            }
            if (criteria.getReservationUser() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReservationUser(), Reservation_.reservationUser));
            }
            if (criteria.getReservationStartTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReservationStartTimestamp(), Reservation_.reservationStartTimestamp));
            }
            if (criteria.getReservationEndTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReservationEndTimestamp(), Reservation_.reservationEndTimestamp));
            }
            if (criteria.getReservationItemId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getReservationItemId(), Reservation_.reservationItems, ReservationItem_.id));
            }
        }
        return specification;
    }
}
