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

/**
 * Criteria class for the ReservationItem entity. This class is used in ReservationItemResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /reservation-items?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReservationItemCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reservationItemName;

    private LongFilter reservationId;

    public ReservationItemCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReservationItemName() {
        return reservationItemName;
    }

    public void setReservationItemName(StringFilter reservationItemName) {
        this.reservationItemName = reservationItemName;
    }

    public LongFilter getReservationId() {
        return reservationId;
    }

    public void setReservationId(LongFilter reservationId) {
        this.reservationId = reservationId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ReservationItemCriteria that = (ReservationItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reservationItemName, that.reservationItemName) &&
            Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reservationItemName,
        reservationId
        );
    }

    @Override
    public String toString() {
        return "ReservationItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reservationItemName != null ? "reservationItemName=" + reservationItemName + ", " : "") +
                (reservationId != null ? "reservationId=" + reservationId + ", " : "") +
            "}";
    }

}
