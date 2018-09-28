/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReservationsystemTestModule } from '../../../test.module';
import { ReservationItemMySuffixDetailComponent } from 'app/entities/reservation-item-my-suffix/reservation-item-my-suffix-detail.component';
import { ReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';

describe('Component Tests', () => {
    describe('ReservationItemMySuffix Management Detail Component', () => {
        let comp: ReservationItemMySuffixDetailComponent;
        let fixture: ComponentFixture<ReservationItemMySuffixDetailComponent>;
        const route = ({ data: of({ reservationItem: new ReservationItemMySuffix(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReservationsystemTestModule],
                declarations: [ReservationItemMySuffixDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ReservationItemMySuffixDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ReservationItemMySuffixDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.reservationItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
