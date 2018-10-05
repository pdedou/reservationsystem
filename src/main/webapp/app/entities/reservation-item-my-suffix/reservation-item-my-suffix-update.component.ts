import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';
import { ReservationItemMySuffixService } from './reservation-item-my-suffix.service';

@Component({
    selector: 'jhi-reservation-item-my-suffix-update',
    templateUrl: './reservation-item-my-suffix-update.component.html'
})
export class ReservationItemMySuffixUpdateComponent implements OnInit {
    private _reservationItem: IReservationItemMySuffix;
    isSaving: boolean;

    constructor(private reservationItemService: ReservationItemMySuffixService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reservationItem }) => {
            this.reservationItem = reservationItem;
        });
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
    get reservationItem() {
        return this._reservationItem;
    }

    set reservationItem(reservationItem: IReservationItemMySuffix) {
        this._reservationItem = reservationItem;
    }
}
