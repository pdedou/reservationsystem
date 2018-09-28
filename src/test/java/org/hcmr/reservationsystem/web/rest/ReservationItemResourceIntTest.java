package org.hcmr.reservationsystem.web.rest;

import org.hcmr.reservationsystem.ReservationsystemApp;

import org.hcmr.reservationsystem.domain.ReservationItem;
import org.hcmr.reservationsystem.domain.Reservation;
import org.hcmr.reservationsystem.repository.ReservationItemRepository;
import org.hcmr.reservationsystem.repository.search.ReservationItemSearchRepository;
import org.hcmr.reservationsystem.service.ReservationItemService;
import org.hcmr.reservationsystem.web.rest.errors.ExceptionTranslator;
import org.hcmr.reservationsystem.service.dto.ReservationItemCriteria;
import org.hcmr.reservationsystem.service.ReservationItemQueryService;

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
 * Test class for the ReservationItemResource REST controller.
 *
 * @see ReservationItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ReservationsystemApp.class)
public class ReservationItemResourceIntTest {

    private static final String DEFAULT_RESERVATION_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RESERVATION_ITEM_NAME = "BBBBBBBBBB";

    @Autowired
    private ReservationItemRepository reservationItemRepository;
    
    @Autowired
    private ReservationItemService reservationItemService;

    /**
     * This repository is mocked in the org.hcmr.reservationsystem.repository.search test package.
     *
     * @see org.hcmr.reservationsystem.repository.search.ReservationItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private ReservationItemSearchRepository mockReservationItemSearchRepository;

    @Autowired
    private ReservationItemQueryService reservationItemQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restReservationItemMockMvc;

    private ReservationItem reservationItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ReservationItemResource reservationItemResource = new ReservationItemResource(reservationItemService, reservationItemQueryService);
        this.restReservationItemMockMvc = MockMvcBuilders.standaloneSetup(reservationItemResource)
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
    public static ReservationItem createEntity(EntityManager em) {
        ReservationItem reservationItem = new ReservationItem()
            .reservationItemName(DEFAULT_RESERVATION_ITEM_NAME);
        return reservationItem;
    }

    @Before
    public void initTest() {
        reservationItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createReservationItem() throws Exception {
        int databaseSizeBeforeCreate = reservationItemRepository.findAll().size();

        // Create the ReservationItem
        restReservationItemMockMvc.perform(post("/api/reservation-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationItem)))
            .andExpect(status().isCreated());

        // Validate the ReservationItem in the database
        List<ReservationItem> reservationItemList = reservationItemRepository.findAll();
        assertThat(reservationItemList).hasSize(databaseSizeBeforeCreate + 1);
        ReservationItem testReservationItem = reservationItemList.get(reservationItemList.size() - 1);
        assertThat(testReservationItem.getReservationItemName()).isEqualTo(DEFAULT_RESERVATION_ITEM_NAME);

        // Validate the ReservationItem in Elasticsearch
        verify(mockReservationItemSearchRepository, times(1)).save(testReservationItem);
    }

    @Test
    @Transactional
    public void createReservationItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reservationItemRepository.findAll().size();

        // Create the ReservationItem with an existing ID
        reservationItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReservationItemMockMvc.perform(post("/api/reservation-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationItem)))
            .andExpect(status().isBadRequest());

        // Validate the ReservationItem in the database
        List<ReservationItem> reservationItemList = reservationItemRepository.findAll();
        assertThat(reservationItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the ReservationItem in Elasticsearch
        verify(mockReservationItemSearchRepository, times(0)).save(reservationItem);
    }

    @Test
    @Transactional
    public void checkReservationItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = reservationItemRepository.findAll().size();
        // set the field null
        reservationItem.setReservationItemName(null);

        // Create the ReservationItem, which fails.

        restReservationItemMockMvc.perform(post("/api/reservation-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationItem)))
            .andExpect(status().isBadRequest());

        List<ReservationItem> reservationItemList = reservationItemRepository.findAll();
        assertThat(reservationItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReservationItems() throws Exception {
        // Initialize the database
        reservationItemRepository.saveAndFlush(reservationItem);

        // Get all the reservationItemList
        restReservationItemMockMvc.perform(get("/api/reservation-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservationItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].reservationItemName").value(hasItem(DEFAULT_RESERVATION_ITEM_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getReservationItem() throws Exception {
        // Initialize the database
        reservationItemRepository.saveAndFlush(reservationItem);

        // Get the reservationItem
        restReservationItemMockMvc.perform(get("/api/reservation-items/{id}", reservationItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reservationItem.getId().intValue()))
            .andExpect(jsonPath("$.reservationItemName").value(DEFAULT_RESERVATION_ITEM_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllReservationItemsByReservationItemNameIsEqualToSomething() throws Exception {
        // Initialize the database
        reservationItemRepository.saveAndFlush(reservationItem);

        // Get all the reservationItemList where reservationItemName equals to DEFAULT_RESERVATION_ITEM_NAME
        defaultReservationItemShouldBeFound("reservationItemName.equals=" + DEFAULT_RESERVATION_ITEM_NAME);

        // Get all the reservationItemList where reservationItemName equals to UPDATED_RESERVATION_ITEM_NAME
        defaultReservationItemShouldNotBeFound("reservationItemName.equals=" + UPDATED_RESERVATION_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllReservationItemsByReservationItemNameIsInShouldWork() throws Exception {
        // Initialize the database
        reservationItemRepository.saveAndFlush(reservationItem);

        // Get all the reservationItemList where reservationItemName in DEFAULT_RESERVATION_ITEM_NAME or UPDATED_RESERVATION_ITEM_NAME
        defaultReservationItemShouldBeFound("reservationItemName.in=" + DEFAULT_RESERVATION_ITEM_NAME + "," + UPDATED_RESERVATION_ITEM_NAME);

        // Get all the reservationItemList where reservationItemName equals to UPDATED_RESERVATION_ITEM_NAME
        defaultReservationItemShouldNotBeFound("reservationItemName.in=" + UPDATED_RESERVATION_ITEM_NAME);
    }

    @Test
    @Transactional
    public void getAllReservationItemsByReservationItemNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        reservationItemRepository.saveAndFlush(reservationItem);

        // Get all the reservationItemList where reservationItemName is not null
        defaultReservationItemShouldBeFound("reservationItemName.specified=true");

        // Get all the reservationItemList where reservationItemName is null
        defaultReservationItemShouldNotBeFound("reservationItemName.specified=false");
    }

    @Test
    @Transactional
    public void getAllReservationItemsByReservationIsEqualToSomething() throws Exception {
        // Initialize the database
        Reservation reservation = ReservationResourceIntTest.createEntity(em);
        em.persist(reservation);
        em.flush();
        reservationItem.addReservation(reservation);
        reservationItemRepository.saveAndFlush(reservationItem);
        Long reservationId = reservation.getId();

        // Get all the reservationItemList where reservation equals to reservationId
        defaultReservationItemShouldBeFound("reservationId.equals=" + reservationId);

        // Get all the reservationItemList where reservation equals to reservationId + 1
        defaultReservationItemShouldNotBeFound("reservationId.equals=" + (reservationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultReservationItemShouldBeFound(String filter) throws Exception {
        restReservationItemMockMvc.perform(get("/api/reservation-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservationItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].reservationItemName").value(hasItem(DEFAULT_RESERVATION_ITEM_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultReservationItemShouldNotBeFound(String filter) throws Exception {
        restReservationItemMockMvc.perform(get("/api/reservation-items?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingReservationItem() throws Exception {
        // Get the reservationItem
        restReservationItemMockMvc.perform(get("/api/reservation-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReservationItem() throws Exception {
        // Initialize the database
        reservationItemService.save(reservationItem);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockReservationItemSearchRepository);

        int databaseSizeBeforeUpdate = reservationItemRepository.findAll().size();

        // Update the reservationItem
        ReservationItem updatedReservationItem = reservationItemRepository.findById(reservationItem.getId()).get();
        // Disconnect from session so that the updates on updatedReservationItem are not directly saved in db
        em.detach(updatedReservationItem);
        updatedReservationItem
            .reservationItemName(UPDATED_RESERVATION_ITEM_NAME);

        restReservationItemMockMvc.perform(put("/api/reservation-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedReservationItem)))
            .andExpect(status().isOk());

        // Validate the ReservationItem in the database
        List<ReservationItem> reservationItemList = reservationItemRepository.findAll();
        assertThat(reservationItemList).hasSize(databaseSizeBeforeUpdate);
        ReservationItem testReservationItem = reservationItemList.get(reservationItemList.size() - 1);
        assertThat(testReservationItem.getReservationItemName()).isEqualTo(UPDATED_RESERVATION_ITEM_NAME);

        // Validate the ReservationItem in Elasticsearch
        verify(mockReservationItemSearchRepository, times(1)).save(testReservationItem);
    }

    @Test
    @Transactional
    public void updateNonExistingReservationItem() throws Exception {
        int databaseSizeBeforeUpdate = reservationItemRepository.findAll().size();

        // Create the ReservationItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReservationItemMockMvc.perform(put("/api/reservation-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(reservationItem)))
            .andExpect(status().isBadRequest());

        // Validate the ReservationItem in the database
        List<ReservationItem> reservationItemList = reservationItemRepository.findAll();
        assertThat(reservationItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ReservationItem in Elasticsearch
        verify(mockReservationItemSearchRepository, times(0)).save(reservationItem);
    }

    @Test
    @Transactional
    public void deleteReservationItem() throws Exception {
        // Initialize the database
        reservationItemService.save(reservationItem);

        int databaseSizeBeforeDelete = reservationItemRepository.findAll().size();

        // Get the reservationItem
        restReservationItemMockMvc.perform(delete("/api/reservation-items/{id}", reservationItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ReservationItem> reservationItemList = reservationItemRepository.findAll();
        assertThat(reservationItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ReservationItem in Elasticsearch
        verify(mockReservationItemSearchRepository, times(1)).deleteById(reservationItem.getId());
    }

    @Test
    @Transactional
    public void searchReservationItem() throws Exception {
        // Initialize the database
        reservationItemService.save(reservationItem);
        when(mockReservationItemSearchRepository.search(queryStringQuery("id:" + reservationItem.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(reservationItem), PageRequest.of(0, 1), 1));
        // Search the reservationItem
        restReservationItemMockMvc.perform(get("/api/_search/reservation-items?query=id:" + reservationItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reservationItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].reservationItemName").value(hasItem(DEFAULT_RESERVATION_ITEM_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReservationItem.class);
        ReservationItem reservationItem1 = new ReservationItem();
        reservationItem1.setId(1L);
        ReservationItem reservationItem2 = new ReservationItem();
        reservationItem2.setId(reservationItem1.getId());
        assertThat(reservationItem1).isEqualTo(reservationItem2);
        reservationItem2.setId(2L);
        assertThat(reservationItem1).isNotEqualTo(reservationItem2);
        reservationItem1.setId(null);
        assertThat(reservationItem1).isNotEqualTo(reservationItem2);
    }
}
