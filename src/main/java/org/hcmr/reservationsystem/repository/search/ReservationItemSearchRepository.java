package org.hcmr.reservationsystem.repository.search;

import org.hcmr.reservationsystem.domain.ReservationItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReservationItem entity.
 */
public interface ReservationItemSearchRepository extends ElasticsearchRepository<ReservationItem, Long> {
}
