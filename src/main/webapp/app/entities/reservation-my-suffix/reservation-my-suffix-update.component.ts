import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';
import { ReservationMySuffixService } from './reservation-my-suffix.service';
import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';
import { ReservationItemMySuffixService } from 'app/entities/reservation-item-my-suffix';

@Component({
    selector: 'jhi-reservation-my-suffix-update',
    templateUrl: './reservation-my-suffix-update.component.html'
})
export class ReservationMySuffixUpdateComponent implements OnInit {
    private _reservation: IReservationMySuffix;
    isSaving: boolean;

    reservationitems: IReservationItemMySuffix[];
    reservationEntryTimestamp: string;
    reservationStartTimestamp: string;
    reservationEndTimestamp: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private reservationService: ReservationMySuffixService,
        private reservationItemService: ReservationItemMySuffixService,
        private activatedRoute: ActivatedRoute
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
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.reservation.reservationEntryTimestamp = moment(this.reservationEntryTimestamp, DATE_TIME_FORMAT);
        this.reservation.reservationStartTimestamp = moment(this.reservationStartTimestamp, DATE_TIME_FORMAT);
        this.reservation.reservationEndTimestamp = moment(this.reservationEndTimestamp, DATE_TIME_FORMAT);
        if (this.reservation.id !== undefined) {
            this.subscribeToSaveResponse(this.reservationService.update(this.reservation));
        } else {
            this.subscribeToSaveResponse(this.reservationService.create(this.reservation));
        }
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get reservation() {
        return this._reservation;
    }

    set reservation(reservation: IReservationMySuffix) {
        this._reservation = reservation;
        this.reservationEntryTimestamp = moment(reservation.reservationEntryTimestamp).format(DATE_TIME_FORMAT);
        this.reservationStartTimestamp = moment(reservation.reservationStartTimestamp).format(DATE_TIME_FORMAT);
        this.reservationEndTimestamp = moment(reservation.reservationEndTimestamp).format(DATE_TIME_FORMAT);
    }
}
