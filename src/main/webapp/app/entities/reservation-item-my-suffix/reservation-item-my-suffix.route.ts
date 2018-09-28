import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { ReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';
import { ReservationItemMySuffixService } from './reservation-item-my-suffix.service';
import { ReservationItemMySuffixComponent } from './reservation-item-my-suffix.component';
import { ReservationItemMySuffixDetailComponent } from './reservation-item-my-suffix-detail.component';
import { ReservationItemMySuffixUpdateComponent } from './reservation-item-my-suffix-update.component';
import { ReservationItemMySuffixDeletePopupComponent } from './reservation-item-my-suffix-delete-dialog.component';
import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';

@Injectable({ providedIn: 'root' })
export class ReservationItemMySuffixResolve implements Resolve<IReservationItemMySuffix> {
    constructor(private service: ReservationItemMySuffixService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((reservationItem: HttpResponse<ReservationItemMySuffix>) => reservationItem.body));
        }
        return of(new ReservationItemMySuffix());
    }
}

export const reservationItemRoute: Routes = [
    {
        path: 'reservation-item-my-suffix',
        component: ReservationItemMySuffixComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservationItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservation-item-my-suffix/:id/view',
        component: ReservationItemMySuffixDetailComponent,
        resolve: {
            reservationItem: ReservationItemMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservationItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservation-item-my-suffix/new',
        component: ReservationItemMySuffixUpdateComponent,
        resolve: {
            reservationItem: ReservationItemMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservationItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'reservation-item-my-suffix/:id/edit',
        component: ReservationItemMySuffixUpdateComponent,
        resolve: {
            reservationItem: ReservationItemMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservationItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const reservationItemPopupRoute: Routes = [
    {
        path: 'reservation-item-my-suffix/:id/delete',
        component: ReservationItemMySuffixDeletePopupComponent,
        resolve: {
            reservationItem: ReservationItemMySuffixResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reservationsystemApp.reservationItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
