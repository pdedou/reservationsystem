/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReservationsystemTestModule } from '../../../test.module';
import { ReservationItemMySuffixUpdateComponent } from 'app/entities/reservation-item-my-suffix/reservation-item-my-suffix-update.component';
import { ReservationItemMySuffixService } from 'app/entities/reservation-item-my-suffix/reservation-item-my-suffix.service';
import { ReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';

describe('Component Tests', () => {
    describe('ReservationItemMySuffix Management Update Component', () => {
        let comp: ReservationItemMySuffixUpdateComponent;
        let fixture: ComponentFixture<ReservationItemMySuffixUpdateComponent>;
        let service: ReservationItemMySuffixService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservationsystemTestModule],
                declarations: [ReservationItemMySuffixUpdateComponent]
            })
                .overrideTemplate(ReservationItemMySuffixUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReservationItemMySuffixUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservationItemMySuffixService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReservationItemMySuffix(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reservationItem = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReservationItemMySuffix();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reservationItem = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
