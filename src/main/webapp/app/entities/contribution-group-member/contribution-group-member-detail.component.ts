import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { ContributionGroupMember } from './contribution-group-member.model';
import { ContributionGroupMemberService } from './contribution-group-member.service';

@Component({
    selector: 'jhi-contribution-group-member-detail',
    templateUrl: './contribution-group-member-detail.component.html'
})
export class ContributionGroupMemberDetailComponent implements OnInit, OnDestroy {

    contributionGroupMember: ContributionGroupMember;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private contributionGroupMemberService: ContributionGroupMemberService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContributionGroupMembers();
    }

    load(id) {
        this.contributionGroupMemberService.find(id)
            .subscribe((contributionGroupMemberResponse: HttpResponse<ContributionGroupMember>) => {
                this.contributionGroupMember = contributionGroupMemberResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContributionGroupMembers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contributionGroupMemberListModification',
            (response) => this.load(this.contributionGroupMember.id)
        );
    }
}
