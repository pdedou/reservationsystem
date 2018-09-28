import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';

@Component({
    selector: 'jhi-reservation-my-suffix-detail',
    templateUrl: './reservation-my-suffix-detail.component.html'
})
export class ReservationMySuffixDetailComponent implements OnInit {
    reservation: IReservationMySuffix;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reservation }) => {
            this.reservation = reservation;
        });
    }

    previousState() {
        window.history.back();
    }
}
