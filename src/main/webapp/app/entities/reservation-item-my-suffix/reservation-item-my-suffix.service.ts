import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';

type EntityResponseType = HttpResponse<IReservationItemMySuffix>;
type EntityArrayResponseType = HttpResponse<IReservationItemMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class ReservationItemMySuffixService {
    private resourceUrl = SERVER_API_URL + 'api/reservation-items';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/reservation-items';

    constructor(private http: HttpClient) {}

    create(reservationItem: IReservationItemMySuffix): Observable<EntityResponseType> {
        return this.http.post<IReservationItemMySuffix>(this.resourceUrl, reservationItem, { observe: 'response' });
    }

    update(reservationItem: IReservationItemMySuffix): Observable<EntityResponseType> {
        return this.http.put<IReservationItemMySuffix>(this.resourceUrl, reservationItem, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IReservationItemMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReservationItemMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReservationItemMySuffix[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
