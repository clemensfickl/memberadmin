import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TrainingGroupMember } from './training-group-member.model';
import { TrainingGroupMemberService } from './training-group-member.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-training-group-member',
    templateUrl: './training-group-member.component.html'
})
export class TrainingGroupMemberComponent implements OnInit, OnDestroy {
trainingGroupMembers: TrainingGroupMember[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private trainingGroupMemberService: TrainingGroupMemberService,
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
            this.trainingGroupMemberService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<TrainingGroupMember[]>) => this.trainingGroupMembers = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.trainingGroupMemberService.query().subscribe(
            (res: HttpResponse<TrainingGroupMember[]>) => {
                this.trainingGroupMembers = res.body;
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
        this.registerChangeInTrainingGroupMembers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TrainingGroupMember) {
        return item.id;
    }
    registerChangeInTrainingGroupMembers() {
        this.eventSubscriber = this.eventManager.subscribe('trainingGroupMemberListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
