import { Moment } from 'moment';
import { IReservationItemMySuffix } from 'app/shared/model//reservation-item-my-suffix.model';

export interface IReservationMySuffix {
    id?: number;
    reservationEntryUser?: string;
    reservationEntryTimestamp?: Moment;
    reservationUser?: string;
    reservationStartTimestamp?: Moment;
    reservationEndTimestamp?: Moment;
    reservationItems?: IReservationItemMySuffix[];
}

export class ReservationMySuffix implements IReservationMySuffix {
    constructor(
        public id?: number,
        public reservationEntryUser?: string,
        public reservationEntryTimestamp?: Moment,
        public reservationUser?: string,
        public reservationStartTimestamp?: Moment,
        public reservationEndTimestamp?: Moment,
        public reservationItems?: IReservationItemMySuffix[]
    ) {}
}
