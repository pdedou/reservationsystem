package org.hcmr.reservationsystem.web.rest;

import org.hcmr.reservationsystem.ReservationsystemApp;

import org.hcmr.reservationsystem.domain.Reservation;
import org.hcmr.reservationsystem.domain.ReservationItem;
import org.hcmr.reservationsystem.repository.ReservationRepository;
import org.hcmr.reservationsystem.repository.search.ReservationSearchRepository;
import org.hcmr.reservationsystem.service.ReservationService;
import org.hcmr.reservationsystem.web.rest.errors.ExceptionTranslator;
import org.hcmr.reservationsystem.service.dto.ReservationCriteria;
import org.hcmr.reservationsystem.service.ReservationQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static org.hcmr.reservationsystem.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReservationResource REST controller.
 *
 * @see ReservationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservationsystemApp.class)
public class ReservationResourceIntTest {

    private static final String DEFAULT_RESERVATION_USER = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_USER = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESERVATION_START_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESERVATION_START_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RESERVATION_END_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESERVATION_END_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESERVATION_ENTRY_USER = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_ENTRY_USER = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESERVATION_ENTRY_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESERVATION_ENTRY_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ReservationService reservationService;

    /**
     * This repository is mocked in the org.hcmr.reservationsystem.repository.search test package.
     *
     * @see org.hcmr.reservationsystem.repository.search.ReservationSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReservationSearchRepository mockReservationSearchRepository;

    @Autowired
    private ReservationQueryService reservationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReservationMockMvc;

    private Reservation reservation;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservationResource reservationResource = new ReservationResource(reservationService, reservationQueryService);
        this.restReservationMockMvc = MockMvcBuilders.standaloneSetup(reservationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reservation createEntity(EntityManager em) {
        Reservation reservation = new Reservation()
            .reservationUser(DEFAULT_RESERVATION_USER)
            .reservationStartTimestamp(DEFAULT_RESERVATION_START_TIMESTAMP)
            .reservationEndTimestamp(DEFAULT_RESERVATION_END_TIMESTAMP)
            .reservationEntryUser(DEFAULT_RESERVATION_ENTRY_USER)
            .reservationEntryTimestamp(DEFAULT_RESERVATION_ENTRY_TIMESTAMP);
        return reservation;
    }

    @Before
    public void initTest() {
        reservation = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservation() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation
        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isCreated());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate + 1);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getReservationUser()).isEqualTo(DEFAULT_RESERVATION_USER);
        assertThat(testReservation.getReservationStartTimestamp()).isEqualTo(DEFAULT_RESERVATION_START_TIMESTAMP);
        assertThat(testReservation.getReservationEndTimestamp()).isEqualTo(DEFAULT_RESERVATION_END_TIMESTAMP);
        assertThat(testReservation.getReservationEntryUser()).isEqualTo(DEFAULT_RESERVATION_ENTRY_USER);
        assertThat(testReservation.getReservationEntryTimestamp()).isEqualTo(DEFAULT_RESERVATION_ENTRY_TIMESTAMP);

        // Validate the Reservation in Elasticsearch
        verify(mockReservationSearchRepository, times(1)).save(testReservation);
    }

    @Test
    @Transactional
    public void createReservationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservationRepository.findAll().size();

        // Create the Reservation with an existing ID
        reservation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationMockMvc.perform(post("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeCreate);

        // Validate the Reservation in Elasticsearch
        verify(mockReservationSearchRepository, times(0)).save(reservation);
    }

    @Test
    @Transactional
    public void getAllReservations() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reservationUser").value(hasItem(DEFAULT_RESERVATION_USER.toString())))
            .andExpect(jsonPath("$.[*].reservationStartTimestamp").value(hasItem(DEFAULT_RESERVATION_START_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].reservationEndTimestamp").value(hasItem(DEFAULT_RESERVATION_END_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].reservationEntryUser").value(hasItem(DEFAULT_RESERVATION_ENTRY_USER.toString())))
            .andExpect(jsonPath("$.[*].reservationEntryTimestamp").value(hasItem(DEFAULT_RESERVATION_ENTRY_TIMESTAMP.toString())));
    }
    
    @Test
    @Transactional
    public void getReservation() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservation.getId().intValue()))
            .andExpect(jsonPath("$.reservationUser").value(DEFAULT_RESERVATION_USER.toString()))
            .andExpect(jsonPath("$.reservationStartTimestamp").value(DEFAULT_RESERVATION_START_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.reservationEndTimestamp").value(DEFAULT_RESERVATION_END_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.reservationEntryUser").value(DEFAULT_RESERVATION_ENTRY_USER.toString()))
            .andExpect(jsonPath("$.reservationEntryTimestamp").value(DEFAULT_RESERVATION_ENTRY_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationUserIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationUser equals to DEFAULT_RESERVATION_USER
        defaultReservationShouldBeFound("reservationUser.equals=" + DEFAULT_RESERVATION_USER);

        // Get all the reservationList where reservationUser equals to UPDATED_RESERVATION_USER
        defaultReservationShouldNotBeFound("reservationUser.equals=" + UPDATED_RESERVATION_USER);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationUserIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationUser in DEFAULT_RESERVATION_USER or UPDATED_RESERVATION_USER
        defaultReservationShouldBeFound("reservationUser.in=" + DEFAULT_RESERVATION_USER + "," + UPDATED_RESERVATION_USER);

        // Get all the reservationList where reservationUser equals to UPDATED_RESERVATION_USER
        defaultReservationShouldNotBeFound("reservationUser.in=" + UPDATED_RESERVATION_USER);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationUser is not null
        defaultReservationShouldBeFound("reservationUser.specified=true");

        // Get all the reservationList where reservationUser is null
        defaultReservationShouldNotBeFound("reservationUser.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationStartTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationStartTimestamp equals to DEFAULT_RESERVATION_START_TIMESTAMP
        defaultReservationShouldBeFound("reservationStartTimestamp.equals=" + DEFAULT_RESERVATION_START_TIMESTAMP);

        // Get all the reservationList where reservationStartTimestamp equals to UPDATED_RESERVATION_START_TIMESTAMP
        defaultReservationShouldNotBeFound("reservationStartTimestamp.equals=" + UPDATED_RESERVATION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationStartTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationStartTimestamp in DEFAULT_RESERVATION_START_TIMESTAMP or UPDATED_RESERVATION_START_TIMESTAMP
        defaultReservationShouldBeFound("reservationStartTimestamp.in=" + DEFAULT_RESERVATION_START_TIMESTAMP + "," + UPDATED_RESERVATION_START_TIMESTAMP);

        // Get all the reservationList where reservationStartTimestamp equals to UPDATED_RESERVATION_START_TIMESTAMP
        defaultReservationShouldNotBeFound("reservationStartTimestamp.in=" + UPDATED_RESERVATION_START_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationStartTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationStartTimestamp is not null
        defaultReservationShouldBeFound("reservationStartTimestamp.specified=true");

        // Get all the reservationList where reservationStartTimestamp is null
        defaultReservationShouldNotBeFound("reservationStartTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEndTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEndTimestamp equals to DEFAULT_RESERVATION_END_TIMESTAMP
        defaultReservationShouldBeFound("reservationEndTimestamp.equals=" + DEFAULT_RESERVATION_END_TIMESTAMP);

        // Get all the reservationList where reservationEndTimestamp equals to UPDATED_RESERVATION_END_TIMESTAMP
        defaultReservationShouldNotBeFound("reservationEndTimestamp.equals=" + UPDATED_RESERVATION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEndTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEndTimestamp in DEFAULT_RESERVATION_END_TIMESTAMP or UPDATED_RESERVATION_END_TIMESTAMP
        defaultReservationShouldBeFound("reservationEndTimestamp.in=" + DEFAULT_RESERVATION_END_TIMESTAMP + "," + UPDATED_RESERVATION_END_TIMESTAMP);

        // Get all the reservationList where reservationEndTimestamp equals to UPDATED_RESERVATION_END_TIMESTAMP
        defaultReservationShouldNotBeFound("reservationEndTimestamp.in=" + UPDATED_RESERVATION_END_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEndTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEndTimestamp is not null
        defaultReservationShouldBeFound("reservationEndTimestamp.specified=true");

        // Get all the reservationList where reservationEndTimestamp is null
        defaultReservationShouldNotBeFound("reservationEndTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEntryUserIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEntryUser equals to DEFAULT_RESERVATION_ENTRY_USER
        defaultReservationShouldBeFound("reservationEntryUser.equals=" + DEFAULT_RESERVATION_ENTRY_USER);

        // Get all the reservationList where reservationEntryUser equals to UPDATED_RESERVATION_ENTRY_USER
        defaultReservationShouldNotBeFound("reservationEntryUser.equals=" + UPDATED_RESERVATION_ENTRY_USER);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEntryUserIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEntryUser in DEFAULT_RESERVATION_ENTRY_USER or UPDATED_RESERVATION_ENTRY_USER
        defaultReservationShouldBeFound("reservationEntryUser.in=" + DEFAULT_RESERVATION_ENTRY_USER + "," + UPDATED_RESERVATION_ENTRY_USER);

        // Get all the reservationList where reservationEntryUser equals to UPDATED_RESERVATION_ENTRY_USER
        defaultReservationShouldNotBeFound("reservationEntryUser.in=" + UPDATED_RESERVATION_ENTRY_USER);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEntryUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEntryUser is not null
        defaultReservationShouldBeFound("reservationEntryUser.specified=true");

        // Get all the reservationList where reservationEntryUser is null
        defaultReservationShouldNotBeFound("reservationEntryUser.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEntryTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEntryTimestamp equals to DEFAULT_RESERVATION_ENTRY_TIMESTAMP
        defaultReservationShouldBeFound("reservationEntryTimestamp.equals=" + DEFAULT_RESERVATION_ENTRY_TIMESTAMP);

        // Get all the reservationList where reservationEntryTimestamp equals to UPDATED_RESERVATION_ENTRY_TIMESTAMP
        defaultReservationShouldNotBeFound("reservationEntryTimestamp.equals=" + UPDATED_RESERVATION_ENTRY_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEntryTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEntryTimestamp in DEFAULT_RESERVATION_ENTRY_TIMESTAMP or UPDATED_RESERVATION_ENTRY_TIMESTAMP
        defaultReservationShouldBeFound("reservationEntryTimestamp.in=" + DEFAULT_RESERVATION_ENTRY_TIMESTAMP + "," + UPDATED_RESERVATION_ENTRY_TIMESTAMP);

        // Get all the reservationList where reservationEntryTimestamp equals to UPDATED_RESERVATION_ENTRY_TIMESTAMP
        defaultReservationShouldNotBeFound("reservationEntryTimestamp.in=" + UPDATED_RESERVATION_ENTRY_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationEntryTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationRepository.saveAndFlush(reservation);

        // Get all the reservationList where reservationEntryTimestamp is not null
        defaultReservationShouldBeFound("reservationEntryTimestamp.specified=true");

        // Get all the reservationList where reservationEntryTimestamp is null
        defaultReservationShouldNotBeFound("reservationEntryTimestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationsByReservationItemIsEqualToSomething() throws Exception {
        // Initialize the database
        ReservationItem reservationItem = ReservationItemResourceIntTest.createEntity(em);
        em.persist(reservationItem);
        em.flush();
        reservation.setReservationItem(reservationItem);
        reservationRepository.saveAndFlush(reservation);
        Long reservationItemId = reservationItem.getId();

        // Get all the reservationList where reservationItem equals to reservationItemId
        defaultReservationShouldBeFound("reservationItemId.equals=" + reservationItemId);

        // Get all the reservationList where reservationItem equals to reservationItemId + 1
        defaultReservationShouldNotBeFound("reservationItemId.equals=" + (reservationItemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReservationShouldBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reservationUser").value(hasItem(DEFAULT_RESERVATION_USER.toString())))
            .andExpect(jsonPath("$.[*].reservationStartTimestamp").value(hasItem(DEFAULT_RESERVATION_START_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].reservationEndTimestamp").value(hasItem(DEFAULT_RESERVATION_END_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].reservationEntryUser").value(hasItem(DEFAULT_RESERVATION_ENTRY_USER.toString())))
            .andExpect(jsonPath("$.[*].reservationEntryTimestamp").value(hasItem(DEFAULT_RESERVATION_ENTRY_TIMESTAMP.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReservationShouldNotBeFound(String filter) throws Exception {
        restReservationMockMvc.perform(get("/api/reservations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingReservation() throws Exception {
        // Get the reservation
        restReservationMockMvc.perform(get("/api/reservations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservation() throws Exception {
        // Initialize the database
        reservationService.save(reservation);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockReservationSearchRepository);

        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Update the reservation
        Reservation updatedReservation = reservationRepository.findById(reservation.getId()).get();
        // Disconnect from session so that the updates on updatedReservation are not directly saved in db
        em.detach(updatedReservation);
        updatedReservation
            .reservationUser(UPDATED_RESERVATION_USER)
            .reservationStartTimestamp(UPDATED_RESERVATION_START_TIMESTAMP)
            .reservationEndTimestamp(UPDATED_RESERVATION_END_TIMESTAMP)
            .reservationEntryUser(UPDATED_RESERVATION_ENTRY_USER)
            .reservationEntryTimestamp(UPDATED_RESERVATION_ENTRY_TIMESTAMP);

        restReservationMockMvc.perform(put("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReservation)))
            .andExpect(status().isOk());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);
        Reservation testReservation = reservationList.get(reservationList.size() - 1);
        assertThat(testReservation.getReservationUser()).isEqualTo(UPDATED_RESERVATION_USER);
        assertThat(testReservation.getReservationStartTimestamp()).isEqualTo(UPDATED_RESERVATION_START_TIMESTAMP);
        assertThat(testReservation.getReservationEndTimestamp()).isEqualTo(UPDATED_RESERVATION_END_TIMESTAMP);
        assertThat(testReservation.getReservationEntryUser()).isEqualTo(UPDATED_RESERVATION_ENTRY_USER);
        assertThat(testReservation.getReservationEntryTimestamp()).isEqualTo(UPDATED_RESERVATION_ENTRY_TIMESTAMP);

        // Validate the Reservation in Elasticsearch
        verify(mockReservationSearchRepository, times(1)).save(testReservation);
    }

    @Test
    @Transactional
    public void updateNonExistingReservation() throws Exception {
        int databaseSizeBeforeUpdate = reservationRepository.findAll().size();

        // Create the Reservation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationMockMvc.perform(put("/api/reservations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservation)))
            .andExpect(status().isBadRequest());

        // Validate the Reservation in the database
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Reservation in Elasticsearch
        verify(mockReservationSearchRepository, times(0)).save(reservation);
    }

    @Test
    @Transactional
    public void deleteReservation() throws Exception {
        // Initialize the database
        reservationService.save(reservation);

        int databaseSizeBeforeDelete = reservationRepository.findAll().size();

        // Get the reservation
        restReservationMockMvc.perform(delete("/api/reservations/{id}", reservation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Reservation> reservationList = reservationRepository.findAll();
        assertThat(reservationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Reservation in Elasticsearch
        verify(mockReservationSearchRepository, times(1)).deleteById(reservation.getId());
    }

    @Test
    @Transactional
    public void searchReservation() throws Exception {
        // Initialize the database
        reservationService.save(reservation);
        when(mockReservationSearchRepository.search(queryStringQuery("id:" + reservation.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reservation), PageRequest.of(0, 1), 1));
        // Search the reservation
        restReservationMockMvc.perform(get("/api/_search/reservations?query=id:" + reservation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservation.getId().intValue())))
            .andExpect(jsonPath("$.[*].reservationUser").value(hasItem(DEFAULT_RESERVATION_USER.toString())))
            .andExpect(jsonPath("$.[*].reservationStartTimestamp").value(hasItem(DEFAULT_RESERVATION_START_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].reservationEndTimestamp").value(hasItem(DEFAULT_RESERVATION_END_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].reservationEntryUser").value(hasItem(DEFAULT_RESERVATION_ENTRY_USER.toString())))
            .andExpect(jsonPath("$.[*].reservationEntryTimestamp").value(hasItem(DEFAULT_RESERVATION_ENTRY_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reservation.class);
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(reservation1.getId());
        assertThat(reservation1).isEqualTo(reservation2);
        reservation2.setId(2L);
        assertThat(reservation1).isNotEqualTo(reservation2);
        reservation1.setId(null);
        assertThat(reservation1).isNotEqualTo(reservation2);
    }
}
