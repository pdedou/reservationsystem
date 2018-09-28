/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReservationsystemTestModule } from '../../../test.module';
import { ReservationMySuffixDetailComponent } from 'app/entities/reservation-my-suffix/reservation-my-suffix-detail.component';
import { ReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';

describe('Component Tests', () => {
    describe('ReservationMySuffix Management Detail Component', () => {
        let comp: ReservationMySuffixDetailComponent;
        let fixture: ComponentFixture<ReservationMySuffixDetailComponent>;
        const route = ({ data: of({ reservation: new ReservationMySuffix(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservationsystemTestModule],
                declarations: [ReservationMySuffixDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReservationMySuffixDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReservationMySuffixDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reservation).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
