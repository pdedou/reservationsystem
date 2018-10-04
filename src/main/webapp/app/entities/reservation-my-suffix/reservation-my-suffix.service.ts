import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';

type EntityResponseType = HttpResponse<IReservationMySuffix>;
type EntityArrayResponseType = HttpResponse<IReservationMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ReservationMySuffixService {
    private resourceUrl = SERVER_API_URL + 'api/reservations';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/reservations';

    constructor(private http: HttpClient) {}

    create(reservation: IReservationMySuffix): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reservation);
        return this.http
            .post<IReservationMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(reservation: IReservationMySuffix): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(reservation);
        return this.http
            .put<IReservationMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IReservationMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReservationMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IReservationMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    private convertDateFromClient(reservation: IReservationMySuffix): IReservationMySuffix {
        const copy: IReservationMySuffix = Object.assign({}, reservation, {
            reservationEntryTimestamp:
                reservation.reservationEntryTimestamp != null && reservation.reservationEntryTimestamp.isValid()
                    ? reservation.reservationEntryTimestamp.toJSON()
                    : null,
            reservationStartTimestamp:
                reservation.reservationStartTimestamp != null && reservation.reservationStartTimestamp.isValid()
                    ? reservation.reservationStartTimestamp.toJSON()
                    : null,
            reservationEndTimestamp:
                reservation.reservationEndTimestamp != null && reservation.reservationEndTimestamp.isValid()
                    ? reservation.reservationEndTimestamp.toJSON()
                    : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.reservationEntryTimestamp = res.body.reservationEntryTimestamp != null ? moment(res.body.reservationEntryTimestamp) : null;
        res.body.reservationStartTimestamp = res.body.reservationStartTimestamp != null ? moment(res.body.reservationStartTimestamp) : null;
        res.body.reservationEndTimestamp = res.body.reservationEndTimestamp != null ? moment(res.body.reservationEndTimestamp) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((reservation: IReservationMySuffix) => {
            reservation.reservationEntryTimestamp =
                reservation.reservationEntryTimestamp != null ? moment(reservation.reservationEntryTimestamp) : null;
            reservation.reservationStartTimestamp =
                reservation.reservationStartTimestamp != null ? moment(reservation.reservationStartTimestamp) : null;
            reservation.reservationEndTimestamp =
                reservation.reservationEndTimestamp != null ? moment(reservation.reservationEndTimestamp) : null;
        });
        return res;
    }

}
