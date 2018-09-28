import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';
import { ReservationItemMySuffixService } from './reservation-item-my-suffix.service';
import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';
import { ReservationMySuffixService } from 'app/entities/reservation-my-suffix';

@Component({
    selector: 'jhi-reservation-item-my-suffix-update',
    templateUrl: './reservation-item-my-suffix-update.component.html'
})
export class ReservationItemMySuffixUpdateComponent implements OnInit {
    private _reservationItem: IReservationItemMySuffix;
    isSaving: boolean;

    reservations: IReservationMySuffix[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private reservationItemService: ReservationItemMySuffixService,
        private reservationService: ReservationMySuffixService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reservationItem }) => {
            this.reservationItem = reservationItem;
        });
        this.reservationService.query().subscribe(
            (res: HttpResponse<IReservationMySuffix[]>) => {
                this.reservations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.reservationItem.id !== undefined) {
            this.subscribeToSaveResponse(this.reservationItemService.update(this.reservationItem));
        } else {
            this.subscribeToSaveResponse(this.reservationItemService.create(this.reservationItem));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReservationItemMySuffix>>) {
        result.subscribe(
            (res: HttpResponse<IReservationItemMySuffix>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
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

    trackReservationById(index: number, item: IReservationMySuffix) {
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
    get reservationItem() {
        return this._reservationItem;
    }

    set reservationItem(reservationItem: IReservationItemMySuffix) {
        this._reservationItem = reservationItem;
    }
}
