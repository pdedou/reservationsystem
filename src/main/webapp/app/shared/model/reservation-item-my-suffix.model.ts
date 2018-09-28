import { IReservationMySuffix } from 'app/shared/model//reservation-my-suffix.model';

export interface IReservationItemMySuffix {
    id?: number;
    reservationItemName?: string;
    reservations?: IReservationMySuffix[];
}

export class ReservationItemMySuffix implements IReservationItemMySuffix {
    constructor(public id?: number, public reservationItemName?: string, public reservations?: IReservationMySuffix[]) {}
}
