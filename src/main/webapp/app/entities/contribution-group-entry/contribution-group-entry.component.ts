import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ContributionGroupEntry } from './contribution-group-entry.model';
import { ContributionGroupEntryService } from './contribution-group-entry.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-contribution-group-entry',
    templateUrl: './contribution-group-entry.component.html'
})
export class ContributionGroupEntryComponent implements OnInit, OnDestroy {
contributionGroupEntries: ContributionGroupEntry[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private contributionGroupEntryService: ContributionGroupEntryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.contributionGroupEntryService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<ContributionGroupEntry[]>) => this.contributionGroupEntries = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.contributionGroupEntryService.query().subscribe(
            (res: HttpResponse<ContributionGroupEntry[]>) => {
                this.contributionGroupEntries = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInContributionGroupEntries();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ContributionGroupEntry) {
        return item.id;
    }
    registerChangeInContributionGroupEntries() {
        this.eventSubscriber = this.eventManager.subscribe('contributionGroupEntryListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
