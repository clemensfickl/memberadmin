import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ContributionGroup } from './contribution-group.model';
import { ContributionGroupService } from './contribution-group.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-contribution-group',
    templateUrl: './contribution-group.component.html'
})
export class ContributionGroupComponent implements OnInit, OnDestroy {
contributionGroups: ContributionGroup[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private contributionGroupService: ContributionGroupService,
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
            this.contributionGroupService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<ContributionGroup[]>) => this.contributionGroups = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.contributionGroupService.query().subscribe(
            (res: HttpResponse<ContributionGroup[]>) => {
                this.contributionGroups = res.body;
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
        this.registerChangeInContributionGroups();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ContributionGroup) {
        return item.id;
    }
    registerChangeInContributionGroups() {
        this.eventSubscriber = this.eventManager.subscribe('contributionGroupListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
