import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';
import { ReservationMySuffixService } from './reservation-my-suffix.service';
import { IReservationItemMySuffix } from 'app/shared/model/reservation-item-my-suffix.model';
import { ReservationItemMySuffixService } from 'app/entities/reservation-item-my-suffix';

import {
    ChangeDetectionStrategy,
    ViewChild,
    TemplateRef
} from '@angular/core';
import {
    startOfDay,
    endOfDay,
    subDays,
    addDays,
    endOfMonth,
    isSameDay,
    isSameMonth,
    addHours
} from 'date-fns';
import { Subject } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {
    CalendarEvent,
    CalendarEventAction,
    CalendarEventTimesChangedEvent,
    CalendarView
} from 'angular-calendar';

const colors: any = {
    red: {
        primary: '#ad2121',
        secondary: '#FAE3E3'
  },
    blue: {
        primary: '#1e90ff',
        secondary: '#D1E8FF'
  },
    yellow: {
        primary: '#e3bc08',
        secondary: '#FDF1BA'
  }
};

@Component({
    selector: 'jhi-reservation-my-suffix-update',
    templateUrl: './reservation-my-suffix-update.component.html'
})
export class ReservationMySuffixUpdateComponent implements OnInit {
    @ViewChild('modalContent')
    modalContent: TemplateRef<any>;
    view: CalendarView = CalendarView.Month;
    CalendarView = CalendarView;
    viewDate: Date = new Date();

    private _reservation: IReservationMySuffix;
    isSaving: boolean;

    reservationitems: IReservationItemMySuffix[];
    reservationEntryTimestamp: string;
    reservationStartTimestamp: string;
    reservationEndTimestamp: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private reservationService: ReservationMySuffixService,
        private reservationItemService: ReservationItemMySuffixService,
        private activatedRoute: ActivatedRoute,
        private modal: NgbModal
    ) {}

    modalData: {
        action: string;
        event: CalendarEvent;
    };

    actions: CalendarEventAction[] = [
    {
        label: '<i class="fa fa-fw fa-pencil"></i>',
        onClick: ({ event }: { event: CalendarEvent }): void => {
        this.reservation.reservationUser = event.title;
        this.save();
        this.handleEvent('Edited', event);
        }
    },
    {
    label: '<i class="fa fa-fw fa-times"></i>',
    onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter(iEvent => iEvent !== event);
        this.reservation.reservationUser = event.title;
        this.save();
        this.handleEvent('Deleted', event);
      }
    }
    ];

    refresh: Subject<any> = new Subject();

    events: CalendarEvent[] = [
    {
        start: subDays(startOfDay(new Date()), 1),
        end: addDays(new Date(), 1),
        title: 'A 3 day event',
        color: colors.red,
        actions: this.actions,
        allDay: true,
        resizable: {
        beforeStart: true,
        afterEnd: true
    },
    draggable: true
    },
    {
        start: startOfDay(new Date()),
        title: 'An event with no end date',
        color: colors.yellow,
        actions: this.actions
    },
    {
        start: subDays(endOfMonth(new Date()), 3),
        end: addDays(endOfMonth(new Date()), 3),
        title: 'A long event that spans 2 months',
        color: colors.blue,
        allDay: true
    },
    {
        start: addHours(startOfDay(new Date()), 2),
        end: new Date(),
        title: 'A draggable and resizable event',
        color: colors.yellow,
        actions: this.actions,
        resizable: {
        beforeStart: true,
        afterEnd: true
        },
        draggable: true
    }
    ];
    activeDayIsOpen: boolean = true;

    dayClicked({ date, events }: { date: Date; events: CalendarEvent[] }): void {
    if (isSameMonth(date, this.viewDate)) {
        this.viewDate = date;
        if (
        (isSameDay(this.viewDate, date) && this.activeDayIsOpen === true) ||
        events.length === 0
        )   {
        this.activeDayIsOpen = false;
        }   else {
        this.activeDayIsOpen = true;
        }
    }
    }

    eventTimesChanged({
    event,
    newStart,
    newEnd
    }:  CalendarEventTimesChangedEvent): void {
    event.start = newStart;
    event.end = newEnd;
    this.handleEvent('Dropped or resized', event);
    this.refresh.next();
    this.reservation.reservationUser = event.title;
    this.save();
    }

    handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = { event, action };
    this.modal.open(this.modalContent, { size: 'lg' });
    this.reservation.reservationUser = event.title;
    this.save();
    }

    addEvent(): void {
    this.events.push({
        title: 'New event',
        start: startOfDay(new Date()),
        end: endOfDay(new Date()),
        color: colors.red,
        draggable: true,
        resizable: {
        beforeStart: true,
        afterEnd: true
        }
    });
    this.refresh.next();
    this.reservation.reservationUser = 'test';
    this.save();
    }


    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ reservation }) => {
            this.reservation = reservation;
        });
        this.reservationItemService.query().subscribe(
            (res: HttpResponse<IReservationItemMySuffix[]>) => {
                this.reservationitems = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.reservation.reservationEntryTimestamp = moment(this.reservationEntryTimestamp, DATE_TIME_FORMAT);
        this.reservation.reservationStartTimestamp = moment(this.reservationStartTimestamp, DATE_TIME_FORMAT);
        this.reservation.reservationEndTimestamp = moment(this.reservationEndTimestamp, DATE_TIME_FORMAT);
        if (this.reservation.id !== undefined) {
            this.subscribeToSaveResponse(this.reservationService.update(this.reservation));
        } else {
            this.subscribeToSaveResponse(this.reservationService.create(this.reservation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IReservationMySuffix>>) {
        result.subscribe((res: HttpResponse<IReservationMySuffix>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackReservationItemById(index: number, item: IReservationItemMySuffix) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get reservation() {
        return this._reservation;
    }

    set reservation(reservation: IReservationMySuffix) {
        this._reservation = reservation;
        this.reservationEntryTimestamp = moment(reservation.reservationEntryTimestamp).format(DATE_TIME_FORMAT);
        this.reservationStartTimestamp = moment(reservation.reservationStartTimestamp).format(DATE_TIME_FORMAT);
        this.reservationEndTimestamp = moment(reservation.reservationEndTimestamp).format(DATE_TIME_FORMAT);
    }
}
