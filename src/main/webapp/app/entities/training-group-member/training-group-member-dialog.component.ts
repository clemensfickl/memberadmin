import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TrainingGroupMember } from './training-group-member.model';
import { TrainingGroupMemberPopupService } from './training-group-member-popup.service';
import { TrainingGroupMemberService } from './training-group-member.service';
import { TrainingGroup, TrainingGroupService } from '../training-group';
import { Member, MemberService } from '../member';

@Component({
    selector: 'jhi-training-group-member-dialog',
    templateUrl: './training-group-member-dialog.component.html'
})
export class TrainingGroupMemberDialogComponent implements OnInit {

    trainingGroupMember: TrainingGroupMember;
    isSaving: boolean;

    traininggroups: TrainingGroup[];

    members: Member[];
    startDateDp: any;
    endDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private trainingGroupMemberService: TrainingGroupMemberService,
        private trainingGroupService: TrainingGroupService,
        private memberService: MemberService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.trainingGroupService.query()
            .subscribe((res: HttpResponse<TrainingGroup[]>) => { this.traininggroups = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.memberService.query()
            .subscribe((res: HttpResponse<Member[]>) => { this.members = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.trainingGroupMember.id !== undefined) {
            this.subscribeToSaveResponse(
                this.trainingGroupMemberService.update(this.trainingGroupMember));
        } else {
            this.subscribeToSaveResponse(
                this.trainingGroupMemberService.create(this.trainingGroupMember));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TrainingGroupMember>>) {
        result.subscribe((res: HttpResponse<TrainingGroupMember>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TrainingGroupMember) {
        this.eventManager.broadcast({ name: 'trainingGroupMemberListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTrainingGroupById(index: number, item: TrainingGroup) {
        return item.id;
    }

    trackMemberById(index: number, item: Member) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-training-group-member-popup',
    template: ''
})
export class TrainingGroupMemberPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private trainingGroupMemberPopupService: TrainingGroupMemberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.trainingGroupMemberPopupService
                    .open(TrainingGroupMemberDialogComponent as Component, params['id']);
            } else {
                this.trainingGroupMemberPopupService
                    .open(TrainingGroupMemberDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
