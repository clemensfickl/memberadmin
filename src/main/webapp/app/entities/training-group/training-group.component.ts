import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TrainingGroup } from './training-group.model';
import { TrainingGroupService } from './training-group.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-training-group',
    templateUrl: './training-group.component.html'
})
export class TrainingGroupComponent implements OnInit, OnDestroy {
trainingGroups: TrainingGroup[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private trainingGroupService: TrainingGroupService,
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
            this.trainingGroupService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<TrainingGroup[]>) => this.trainingGroups = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.trainingGroupService.query().subscribe(
            (res: HttpResponse<TrainingGroup[]>) => {
                this.trainingGroups = res.body;
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
        this.registerChangeInTrainingGroups();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TrainingGroup) {
        return item.id;
    }
    registerChangeInTrainingGroups() {
        this.eventSubscriber = this.eventManager.subscribe('trainingGroupListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
