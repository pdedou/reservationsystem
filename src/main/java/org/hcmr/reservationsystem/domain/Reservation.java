package org.hcmr.reservationsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reservation")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "reservation_user")
    private String reservationUser;

    @Column(name = "reservation_start_timestamp")
    private Instant reservationStartTimestamp;

    @Column(name = "reservation_end_timestamp")
    private Instant reservationEndTimestamp;

    @Column(name = "reservation_entry_user")
    private String reservationEntryUser;

    @Column(name = "reservation_entry_timestamp")
    private Instant reservationEntryTimestamp;

    @ManyToOne
    @JsonIgnoreProperties("reservations")
    private ReservationItem reservationItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationUser() {
        return reservationUser;
    }

    public Reservation reservationUser(String reservationUser) {
        this.reservationUser = reservationUser;
        return this;
    }

    public void setReservationUser(String reservationUser) {
        this.reservationUser = reservationUser;
    }

    public Instant getReservationStartTimestamp() {
        return reservationStartTimestamp;
    }

    public Reservation reservationStartTimestamp(Instant reservationStartTimestamp) {
        this.reservationStartTimestamp = reservationStartTimestamp;
        return this;
    }

    public void setReservationStartTimestamp(Instant reservationStartTimestamp) {
        this.reservationStartTimestamp = reservationStartTimestamp;
    }

    public Instant getReservationEndTimestamp() {
        return reservationEndTimestamp;
    }

    public Reservation reservationEndTimestamp(Instant reservationEndTimestamp) {
        this.reservationEndTimestamp = reservationEndTimestamp;
        return this;
    }

    public void setReservationEndTimestamp(Instant reservationEndTimestamp) {
        this.reservationEndTimestamp = reservationEndTimestamp;
    }

    public String getReservationEntryUser() {
        return reservationEntryUser;
    }

    public Reservation reservationEntryUser(String reservationEntryUser) {
        this.reservationEntryUser = reservationEntryUser;
        return this;
    }

    public void setReservationEntryUser(String reservationEntryUser) {
        this.reservationEntryUser = reservationEntryUser;
    }

    public Instant getReservationEntryTimestamp() {
        return reservationEntryTimestamp;
    }

    public Reservation reservationEntryTimestamp(Instant reservationEntryTimestamp) {
        this.reservationEntryTimestamp = reservationEntryTimestamp;
        return this;
    }

    public void setReservationEntryTimestamp(Instant reservationEntryTimestamp) {
        this.reservationEntryTimestamp = reservationEntryTimestamp;
    }

    public ReservationItem getReservationItem() {
        return reservationItem;
    }

    public Reservation reservationItem(ReservationItem reservationItem) {
        this.reservationItem = reservationItem;
        return this;
    }

    public void setReservationItem(ReservationItem reservationItem) {
        this.reservationItem = reservationItem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation reservation = (Reservation) o;
        if (reservation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", reservationUser='" + getReservationUser() + "'" +
            ", reservationStartTimestamp='" + getReservationStartTimestamp() + "'" +
            ", reservationEndTimestamp='" + getReservationEndTimestamp() + "'" +
            ", reservationEntryUser='" + getReservationEntryUser() + "'" +
            ", reservationEntryTimestamp='" + getReservationEntryTimestamp() + "'" +
            "}";
    }
}
