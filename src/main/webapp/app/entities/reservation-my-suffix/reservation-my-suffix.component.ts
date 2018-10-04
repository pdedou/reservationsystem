import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { IReservationMySuffix } from 'app/shared/model/reservation-my-suffix.model';
import { Principal } from 'app/core';

import { ITEMS_PER_PAGE } from 'app/shared';
import { ReservationMySuffixService } from './reservation-my-suffix.service';

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
    selector: 'jhi-reservation-my-suffix',
    templateUrl: './reservation-my-suffix.component.html'
})
export class ReservationMySuffixComponent implements OnInit, OnDestroy {
    @ViewChild('modalContent')
    modalContent: TemplateRef<any>;
    view: CalendarView = CalendarView.Month;
    CalendarView = CalendarView;
    viewDate: Date = new Date();

    reservations: IReservationMySuffix[];
    currentAccount: any;
    eventSubscriber: Subscription;
    itemsPerPage: number;
    links: any;
    page: any;
    predicate: any;
    queryCount: any;
    reverse: any;
    totalItems: number;
    currentSearch: string;

    modalData: {
        action: string;
        event: CalendarEvent;
    };

    actions: CalendarEventAction[] = [
    {
        label: '<i class="fa fa-fw fa-pencil"></i>',
        onClick: ({ event }: { event: CalendarEvent }): void => {
        this.handleEvent('Edited', event);
        }
    },
    {
    label: '<i class="fa fa-fw fa-times"></i>',
    onClick: ({ event }: { event: CalendarEvent }): void => {
        this.events = this.events.filter(iEvent => iEvent !== event);
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

    constructor(
        private reservationService: ReservationMySuffixService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private parseLinks: JhiParseLinks,
        private activatedRoute: ActivatedRoute,
        private principal: Principal,
        private modal: NgbModal
    ) {
        this.reservations = [];
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 0;
        this.links = {
            last: 0
        };
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

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
    }

    handleEvent(action: string, event: CalendarEvent): void {
    this.modalData = { event, action };
    this.modal.open(this.modalContent, { size: 'lg' });
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
    }

    populateCalendar() {
      const monthEnd = endOfMonth(this.viewDate);
//      const month = format(monthEnd, 'YYYY-MM');

        this.reservations.forEach((item) => {
          const value = item.reservationUser + item.reservationStartTimestamp + item.reservationEndTimestamp;
          this.events.push({
            start: item.reservationStartTimestamp.toDate(),
            end: item.reservationEndTimestamp.toDate(),
            title: 'An event with no end date',
            color: colors.yellow,
            actions: this.actions
          });

        });
        this.refresh.next();
    }

    loadAll() {
      if (this.currentSearch) {
        this.reservationService
          .search({
            query: this.currentSearch,
            page: this.page,
            size: this.itemsPerPage
          })
          .subscribe(
          (res: HttpResponse<IReservationMySuffix[]>) => {
            this.paginateReservations(res.body, res.headers);
            this.populateCalendar();
          },
          (res: HttpErrorResponse) => this.onError(res.message)
          );
        return;
      }
      this.reservationService
        .query({
          page: this.page,
          size: this.itemsPerPage
        })
        .subscribe(
        (res: HttpResponse<IReservationMySuffix[]>) => {
            this.paginateReservations(res.body, res.headers);
            this.populateCalendar();
          },
        (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    reset() {
        this.page = 0;
        this.reservations = [];
        this.loadAll();
    }

    loadPage(page) {
        this.page = page;
        this.loadAll();
    }

    clear() {
        this.reservations = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = 'id';
        this.reverse = true;
        this.currentSearch = '';
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.reservations = [];
        this.links = {
            last: 0
        };
        this.page = 0;
        this.predicate = '_score';
        this.reverse = false;
        this.currentSearch = query;
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInReservations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IReservationMySuffix) {
        return item.id;
    }

    registerChangeInReservations() {
        this.eventSubscriber = this.eventManager.subscribe('reservationListModification', response => this.reset());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private paginateReservations(data: IReservationMySuffix[], headers: HttpHeaders) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = parseInt(headers.get('X-Total-Count'), 10);
        for (let i = 0; i < data.length; i++) {
            this.reservations.push(data[i]);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
