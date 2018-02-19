import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TrainingGroupMember } from './training-group-member.model';
import { TrainingGroupMemberService } from './training-group-member.service';

@Component({
    selector: 'jhi-training-group-member-detail',
    templateUrl: './training-group-member-detail.component.html'
})
export class TrainingGroupMemberDetailComponent implements OnInit, OnDestroy {

    trainingGroupMember: TrainingGroupMember;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private trainingGroupMemberService: TrainingGroupMemberService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTrainingGroupMembers();
    }

    load(id) {
        this.trainingGroupMemberService.find(id)
            .subscribe((trainingGroupMemberResponse: HttpResponse<TrainingGroupMember>) => {
                this.trainingGroupMember = trainingGroupMemberResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTrainingGroupMembers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'trainingGroupMemberListModification',
            (response) => this.load(this.trainingGroupMember.id)
        );
    }
}
