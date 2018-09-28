package org.hcmr.reservationsystem.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ReservationSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ReservationSearchRepositoryMockConfiguration {

    @MockBean
    private ReservationSearchRepository mockReservationSearchRepository;

}
