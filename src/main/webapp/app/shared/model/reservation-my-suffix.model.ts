import { Moment } from 'moment';
import { IReservationItemMySuffix } from 'app/shared/model//reservation-item-my-suffix.model';

export interface IReservationMySuffix {
    id?: number;
    reservationUser?: string;
    reservationStartTimestamp?: Moment;
    reservationEndTimestamp?: Moment;
    reservationEntryUser?: string;
    reservationEntryTimestamp?: Moment;
    reservationItem?: IReservationItemMySuffix;
}

export class ReservationMySuffix implements IReservationMySuffix {
    constructor(
        public id?: number,
        public reservationUser?: string,
        public reservationStartTimestamp?: Moment,
        public reservationEndTimestamp?: Moment,
        public reservationEntryUser?: string,
        public reservationEntryTimestamp?: Moment,
        public reservationItem?: IReservationItemMySuffix
    ) {}
}
