/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReservationsystemTestModule } from '../../../test.module';
import { ReservationMySuffixUpdateComponent } from 'app/entities/reservation-my-suffix/reservation-my-suffix-update.component';
import { ReservationMySuffixService } from 'app/entities/reservation-my-suffix/reservation-my-suffix.service';
import { ReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';

describe('Component Tests', () => {
    describe('ReservationMySuffix Management Update Component', () => {
        let comp: ReservationMySuffixUpdateComponent;
        let fixture: ComponentFixture<ReservationMySuffixUpdateComponent>;
        let service: ReservationMySuffixService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservationsystemTestModule],
                declarations: [ReservationMySuffixUpdateComponent]
            })
                .overrideTemplate(ReservationMySuffixUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ReservationMySuffixUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReservationMySuffixService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new ReservationMySuffix(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reservation = entity;
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
                    const entity = new ReservationMySuffix();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.reservation = entity;
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
