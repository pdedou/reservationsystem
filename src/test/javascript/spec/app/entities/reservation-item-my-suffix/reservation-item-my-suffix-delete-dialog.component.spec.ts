/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReservationsystemTestModule } from '../../../test.module';
import { ReservationItemMySuffixDeleteDialogComponent } from 'app/entities/reservation-item-my-suffix/reservation-item-my-suffix-delete-dialog.component';
import { ReservationItemMySuffixService } from 'app/entities/reservation-item-my-suffix/reservation-item-my-suffix.service';

describe('Component Tests', () => {
    describe('ReservationItemMySuffix Management Delete Component', () => {
        let comp: ReservationItemMySuffixDeleteDialogComponent;
        let fixture: ComponentFixture<ReservationItemMySuffixDeleteDialogComponent>;
        let service: ReservationItemMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservationsystemTestModule],
                declarations: [ReservationItemMySuffixDeleteDialogComponent]
            })
                .overrideTemplate(ReservationItemMySuffixDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReservationItemMySuffixDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservationItemMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
