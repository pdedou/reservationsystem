import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';

import { FlatpickrModule } from 'angularx-flatpickr';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NgbModalModule } from '@ng-bootstrap/ng-bootstrap';

import { ReservationsystemSharedModule } from 'app/shared';
import {
    ReservationMySuffixComponent,
    ReservationMySuffixDetailComponent,
    ReservationMySuffixUpdateComponent,
    ReservationMySuffixDeletePopupComponent,
    ReservationMySuffixDeleteDialogComponent,
    reservationRoute,
    reservationPopupRoute
} from './';

const ENTITY_STATES = [...reservationRoute, ...reservationPopupRoute];

@NgModule({
    imports: [ReservationsystemSharedModule,
      CommonModule,
        FormsModule,
        NgbModalModule,
        FlatpickrModule.forRoot(),
        CalendarModule.forRoot({
          provide: DateAdapter,
          useFactory: adapterFactory
        }),
    RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReservationMySuffixComponent,
        ReservationMySuffixDetailComponent,
        ReservationMySuffixUpdateComponent,
        ReservationMySuffixDeleteDialogComponent,
        ReservationMySuffixDeletePopupComponent
    ],
    entryComponents: [
        ReservationMySuffixComponent,
        ReservationMySuffixUpdateComponent,
        ReservationMySuffixDeleteDialogComponent,
        ReservationMySuffixDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservationsystemReservationMySuffixModule {}
