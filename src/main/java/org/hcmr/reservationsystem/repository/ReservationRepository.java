package org.hcmr.reservationsystem.repository;

import org.hcmr.reservationsystem.domain.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Reservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    @Query(value = "select distinct reservation from Reservation reservation left join fetch reservation.reservationItems",
        countQuery = "select count(distinct reservation) from Reservation reservation")
    Page<Reservation> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct reservation from Reservation reservation left join fetch reservation.reservationItems")
    List<Reservation> findAllWithEagerRelationships();

    @Query("select reservation from Reservation reservation left join fetch reservation.reservationItems where reservation.id =:id")
    Optional<Reservation> findOneWithEagerRelationships(@Param("id") Long id);

}
