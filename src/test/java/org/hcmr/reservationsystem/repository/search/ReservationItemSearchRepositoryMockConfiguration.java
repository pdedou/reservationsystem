package org.hcmr.reservationsystem.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of ReservationItemSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ReservationItemSearchRepositoryMockConfiguration {

    @MockBean
    private ReservationItemSearchRepository mockReservationItemSearchRepository;

}
