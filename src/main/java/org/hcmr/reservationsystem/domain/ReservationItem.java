package org.hcmr.reservationsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ReservationItem.
 */
@Entity
@Table(name = "reservation_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "reservationitem")
public class ReservationItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reservation_item_name", nullable = false)
    private String reservationItemName;

    @OneToMany(mappedBy = "reservationItem")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationItemName() {
        return reservationItemName;
    }

    public ReservationItem reservationItemName(String reservationItemName) {
        this.reservationItemName = reservationItemName;
        return this;
    }

    public void setReservationItemName(String reservationItemName) {
        this.reservationItemName = reservationItemName;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public ReservationItem reservations(Set<Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    public ReservationItem addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setReservationItem(this);
        return this;
    }

    public ReservationItem removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setReservationItem(null);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
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
        ReservationItem reservationItem = (ReservationItem) o;
        if (reservationItem.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reservationItem.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReservationItem{" +
            "id=" + getId() +
            ", reservationItemName='" + getReservationItemName() + "'" +
            "}";
    }
}
