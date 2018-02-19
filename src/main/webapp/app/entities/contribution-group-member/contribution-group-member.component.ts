import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ContributionGroupMember } from './contribution-group-member.model';
import { ContributionGroupMemberService } from './contribution-group-member.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-contribution-group-member',
    templateUrl: './contribution-group-member.component.html'
})
export class ContributionGroupMemberComponent implements OnInit, OnDestroy {
contributionGroupMembers: ContributionGroupMember[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private contributionGroupMemberService: ContributionGroupMemberService,
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
            this.contributionGroupMemberService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<ContributionGroupMember[]>) => this.contributionGroupMembers = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.contributionGroupMemberService.query().subscribe(
            (res: HttpResponse<ContributionGroupMember[]>) => {
                this.contributionGroupMembers = res.body;
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
        this.registerChangeInContributionGroupMembers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ContributionGroupMember) {
        return item.id;
    }
    registerChangeInContributionGroupMembers() {
        this.eventSubscriber = this.eventManager.subscribe('contributionGroupMemberListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
