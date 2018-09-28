package org.hcmr.reservationsystem.repository;

import org.hcmr.reservationsystem.domain.ReservationItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReservationItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationItemRepository extends JpaRepository<ReservationItem, Long>, JpaSpecificationExecutor<ReservationItem> {

}
