import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReservationsystemSharedModule } from 'app/shared';
import {
    ReservationItemMySuffixComponent,
    ReservationItemMySuffixDetailComponent,
    ReservationItemMySuffixUpdateComponent,
    ReservationItemMySuffixDeletePopupComponent,
    ReservationItemMySuffixDeleteDialogComponent,
    reservationItemRoute,
    reservationItemPopupRoute
} from './';

const ENTITY_STATES = [...reservationItemRoute, ...reservationItemPopupRoute];

@NgModule({
    imports: [ReservationsystemSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        ReservationItemMySuffixComponent,
        ReservationItemMySuffixDetailComponent,
        ReservationItemMySuffixUpdateComponent,
        ReservationItemMySuffixDeleteDialogComponent,
        ReservationItemMySuffixDeletePopupComponent
    ],
    entryComponents: [
        ReservationItemMySuffixComponent,
        ReservationItemMySuffixUpdateComponent,
        ReservationItemMySuffixDeleteDialogComponent,
        ReservationItemMySuffixDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservationsystemReservationItemMySuffixModule {}
