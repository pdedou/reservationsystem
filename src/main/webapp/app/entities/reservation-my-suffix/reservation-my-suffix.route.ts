import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';
import { ReservationMySuffixService } from './reservation-my-suffix.service';
import { ReservationMySuffixComponent } from './reservation-my-suffix.component';
import { ReservationMySuffixDetailComponent } from './reservation-my-suffix-detail.component';
import { ReservationMySuffixUpdateComponent } from './reservation-my-suffix-update.component';
import { ReservationMySuffixDeletePopupComponent } from './reservation-my-suffix-delete-dialog.component';
import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';

@Injectable({ providedIn: 'root' })
export class ReservationMySuffixResolve implements Resolve<IReservationMySuffix> {
    constructor(private service: ReservationMySuffixService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((reservation: HttpResponse<ReservationMySuffix>) => reservation.body));
        }
        return of(new ReservationMySuffix());
    }
}

export const reservationRoute: Routes = [
    {
        path: 'reservation-my-suffix',
        component: ReservationMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservation-my-suffix/:id/view',
        component: ReservationMySuffixDetailComponent,
        resolve: {
            reservation: ReservationMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservation-my-suffix/new',
        component: ReservationMySuffixUpdateComponent,
        resolve: {
            reservation: ReservationMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservation-my-suffix/:id/edit',
        component: ReservationMySuffixUpdateComponent,
        resolve: {
            reservation: ReservationMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reservationPopupRoute: Routes = [
    {
        path: 'reservation-my-suffix/:id/delete',
        component: ReservationMySuffixDeletePopupComponent,
        resolve: {
            reservation: ReservationMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
