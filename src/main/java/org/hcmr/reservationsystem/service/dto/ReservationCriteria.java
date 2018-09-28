package org.hcmr.reservationsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Reservation entity. This class is used in ReservationResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reservations?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReservationCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reservationEntryUser;

    private InstantFilter reservationEntryTimestamp;

    private StringFilter reservationUser;

    private InstantFilter reservationStartTimestamp;

    private InstantFilter reservationEndTimestamp;

    private LongFilter reservationItemId;

    public ReservationCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReservationEntryUser() {
        return reservationEntryUser;
    }

    public void setReservationEntryUser(StringFilter reservationEntryUser) {
        this.reservationEntryUser = reservationEntryUser;
    }

    public InstantFilter getReservationEntryTimestamp() {
        return reservationEntryTimestamp;
    }

    public void setReservationEntryTimestamp(InstantFilter reservationEntryTimestamp) {
        this.reservationEntryTimestamp = reservationEntryTimestamp;
    }

    public StringFilter getReservationUser() {
        return reservationUser;
    }

    public void setReservationUser(StringFilter reservationUser) {
        this.reservationUser = reservationUser;
    }

    public InstantFilter getReservationStartTimestamp() {
        return reservationStartTimestamp;
    }

    public void setReservationStartTimestamp(InstantFilter reservationStartTimestamp) {
        this.reservationStartTimestamp = reservationStartTimestamp;
    }

    public InstantFilter getReservationEndTimestamp() {
        return reservationEndTimestamp;
    }

    public void setReservationEndTimestamp(InstantFilter reservationEndTimestamp) {
        this.reservationEndTimestamp = reservationEndTimestamp;
    }

    public LongFilter getReservationItemId() {
        return reservationItemId;
    }

    public void setReservationItemId(LongFilter reservationItemId) {
        this.reservationItemId = reservationItemId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationCriteria that = (ReservationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reservationEntryUser, that.reservationEntryUser) &&
            Objects.equals(reservationEntryTimestamp, that.reservationEntryTimestamp) &&
            Objects.equals(reservationUser, that.reservationUser) &&
            Objects.equals(reservationStartTimestamp, that.reservationStartTimestamp) &&
            Objects.equals(reservationEndTimestamp, that.reservationEndTimestamp) &&
            Objects.equals(reservationItemId, that.reservationItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reservationEntryUser,
        reservationEntryTimestamp,
        reservationUser,
        reservationStartTimestamp,
        reservationEndTimestamp,
        reservationItemId
        );
    }

    @Override
    public String toString() {
        return "ReservationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reservationEntryUser != null ? "reservationEntryUser=" + reservationEntryUser + ", " : "") +
                (reservationEntryTimestamp != null ? "reservationEntryTimestamp=" + reservationEntryTimestamp + ", " : "") +
                (reservationUser != null ? "reservationUser=" + reservationUser + ", " : "") +
                (reservationStartTimestamp != null ? "reservationStartTimestamp=" + reservationStartTimestamp + ", " : "") +
                (reservationEndTimestamp != null ? "reservationEndTimestamp=" + reservationEndTimestamp + ", " : "") +
                (reservationItemId != null ? "reservationItemId=" + reservationItemId + ", " : "") +
            "}";
    }

}
