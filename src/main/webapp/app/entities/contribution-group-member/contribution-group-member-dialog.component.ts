import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ContributionGroupMember } from './contribution-group-member.model';
import { ContributionGroupMemberPopupService } from './contribution-group-member-popup.service';
import { ContributionGroupMemberService } from './contribution-group-member.service';
import { ContributionGroup, ContributionGroupService } from '../contribution-group';
import { Member, MemberService } from '../member';

@Component({
    selector: 'jhi-contribution-group-member-dialog',
    templateUrl: './contribution-group-member-dialog.component.html'
})
export class ContributionGroupMemberDialogComponent implements OnInit {

    contributionGroupMember: ContributionGroupMember;
    isSaving: boolean;

    contributiongroups: ContributionGroup[];

    members: Member[];
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private contributionGroupMemberService: ContributionGroupMemberService,
        private contributionGroupService: ContributionGroupService,
        private memberService: MemberService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.contributionGroupService.query()
            .subscribe((res: HttpResponse<ContributionGroup[]>) => { this.contributiongroups = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.memberService.query()
            .subscribe((res: HttpResponse<Member[]>) => { this.members = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.contributionGroupMember.id !== undefined) {
            this.subscribeToSaveResponse(
                this.contributionGroupMemberService.update(this.contributionGroupMember));
        } else {
            this.subscribeToSaveResponse(
                this.contributionGroupMemberService.create(this.contributionGroupMember));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ContributionGroupMember>>) {
        result.subscribe((res: HttpResponse<ContributionGroupMember>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ContributionGroupMember) {
        this.eventManager.broadcast({ name: 'contributionGroupMemberListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackContributionGroupById(index: number, item: ContributionGroup) {
        return item.id;
    }

    trackMemberById(index: number, item: Member) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-contribution-group-member-popup',
    template: ''
})
export class ContributionGroupMemberPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contributionGroupMemberPopupService: ContributionGroupMemberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.contributionGroupMemberPopupService
                    .open(ContributionGroupMemberDialogComponent as Component, params['id']);
            } else {
                this.contributionGroupMemberPopupService
                    .open(ContributionGroupMemberDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
