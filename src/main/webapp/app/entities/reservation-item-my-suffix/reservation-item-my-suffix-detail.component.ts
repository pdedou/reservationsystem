import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';

@Component({
    selector: 'jhi-reservation-item-my-suffix-detail',
    templateUrl: './reservation-item-my-suffix-detail.component.html'
})
export class ReservationItemMySuffixDetailComponent implements OnInit {
    reservationItem: IReservationItemMySuffix;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reservationItem }) => {
            this.reservationItem = reservationItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
