import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';
import { ReservationMySuffixService } from './reservation-my-suffix.service';
import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';
import { ReservationItemMySuffixService } from 'app/entities/reservation-item-my-suffix';
import { Principal, User, UserService } from '../../core';
import { ITEMS_PER_PAGE } from '../../shared';

@Component({
    selector: 'jhi-reservation-my-suffix-update',
    templateUrl: './reservation-my-suffix-update.component.html'
})
export class ReservationMySuffixUpdateComponent implements OnInit {
    private _reservation: IReservationMySuffix;
    isSaving: boolean;

    currentAccount: any;
    users: User[];
    links: any;
    totalItems: any;
    queryCount: any;
    predicate: any;
    reverse: any;

    reservationitems: IReservationItemMySuffix[];
    reservationStartTimestamp: string;
    reservationEndTimestamp: string;
    reservationEntryTimestamp: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private reservationService: ReservationMySuffixService,
        private reservationItemService: ReservationItemMySuffixService,
        private activatedRoute: ActivatedRoute,
        private userService: UserService,
        private principal: Principal,
        private parseLinks: JhiParseLinks
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reservation }) => {
            this.reservation = reservation;
        });
        this.reservationItemService.query().subscribe(
            (res: HttpResponse<IReservationItemMySuffix[]>) => {
                this.reservationitems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
      this.loadAll();
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.reservation.reservationStartTimestamp = moment(this.reservationStartTimestamp, DATE_TIME_FORMAT);
        this.reservation.reservationEndTimestamp = moment(this.reservationEndTimestamp, DATE_TIME_FORMAT);
        this.reservation.reservationEntryTimestamp = moment(this.reservationEntryTimestamp, DATE_TIME_FORMAT);
        if (this.reservation.id !== undefined) {
            this.subscribeToSaveResponse(this.reservationService.update(this.reservation));
        } else {
            this.subscribeToSaveResponse(this.reservationService.create(this.reservation));
        }
    }

    loadAll() {
        this.userService.query({
            page: 0,
            size: 1000,
            sort: ['id,asc']}).subscribe(
                (res: HttpResponse<User[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpResponse<any>) => this.onError(res.body)
        );
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    trackIdentity(index, item: User) {
        return item.id;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        this.users = data;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReservationMySuffix>>) {
        result.subscribe((res: HttpResponse<IReservationMySuffix>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackReservationItemById(index: number, item: IReservationItemMySuffix) {
        return item.id;
    }
    get reservation() {
        return this._reservation;
    }

    set reservation(reservation: IReservationMySuffix) {
        this._reservation = reservation;
        this.reservationStartTimestamp = moment(reservation.reservationStartTimestamp).format(DATE_TIME_FORMAT);
        this.reservationEndTimestamp = moment(reservation.reservationEndTimestamp).format(DATE_TIME_FORMAT);
        this.reservationEntryTimestamp = moment(reservation.reservationEntryTimestamp).format(DATE_TIME_FORMAT);
    }
}
