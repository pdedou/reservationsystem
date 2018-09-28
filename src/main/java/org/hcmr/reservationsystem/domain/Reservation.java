package org.hcmr.reservationsystem.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "reservation_entry_user")
    private String reservationEntryUser;

    @Column(name = "reservation_entry_timestamp")
    private Instant reservationEntryTimestamp;

    @Column(name = "reservation_user")
    private String reservationUser;

    @Column(name = "reservation_start_timestamp")
    private Instant reservationStartTimestamp;

    @Column(name = "reservation_end_timestamp")
    private Instant reservationEndTimestamp;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "reservation_reservation_item",
               joinColumns = @JoinColumn(name = "reservations_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "reservation_items_id", referencedColumnName = "id"))
    private Set<ReservationItem> reservationItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<ReservationItem> getReservationItems() {
        return reservationItems;
    }

    public Reservation reservationItems(Set<ReservationItem> reservationItems) {
        this.reservationItems = reservationItems;
        return this;
    }

    public Reservation addReservationItem(ReservationItem reservationItem) {
        this.reservationItems.add(reservationItem);
        reservationItem.getReservations().add(this);
        return this;
    }

    public Reservation removeReservationItem(ReservationItem reservationItem) {
        this.reservationItems.remove(reservationItem);
        reservationItem.getReservations().remove(this);
        return this;
    }

    public void setReservationItems(Set<ReservationItem> reservationItems) {
        this.reservationItems = reservationItems;
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
            ", reservationEntryUser='" + getReservationEntryUser() + "'" +
            ", reservationEntryTimestamp='" + getReservationEntryTimestamp() + "'" +
            ", reservationUser='" + getReservationUser() + "'" +
            ", reservationStartTimestamp='" + getReservationStartTimestamp() + "'" +
            ", reservationEndTimestamp='" + getReservationEndTimestamp() + "'" +
            "}";
    }
}
