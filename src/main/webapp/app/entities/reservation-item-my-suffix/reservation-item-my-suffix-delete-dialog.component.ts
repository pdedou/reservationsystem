import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';
import { ReservationItemMySuffixService } from './reservation-item-my-suffix.service';

@Component({
    selector: 'jhi-reservation-item-my-suffix-delete-dialog',
    templateUrl: './reservation-item-my-suffix-delete-dialog.component.html'
})
export class ReservationItemMySuffixDeleteDialogComponent {
    reservationItem: IReservationItemMySuffix;

    constructor(
        private reservationItemService: ReservationItemMySuffixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.reservationItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'reservationItemListModification',
                content: 'Deleted an reservationItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reservation-item-my-suffix-delete-popup',
    template: ''
})
export class ReservationItemMySuffixDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ reservationItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ReservationItemMySuffixDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.reservationItem = reservationItem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
