import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ReservationsystemReservationMySuffixModule } from './reservation-my-suffix/reservation-my-suffix.module';
import { ReservationsystemReservationItemMySuffixModule } from './reservation-item-my-suffix/reservation-item-my-suffix.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ReservationsystemReservationMySuffixModule,
        ReservationsystemReservationItemMySuffixModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReservationsystemEntityModule {}
