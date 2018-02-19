import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TrainingGroupMember } from './training-group-member.model';
import { TrainingGroupMemberPopupService } from './training-group-member-popup.service';
import { TrainingGroupMemberService } from './training-group-member.service';

@Component({
    selector: 'jhi-training-group-member-delete-dialog',
    templateUrl: './training-group-member-delete-dialog.component.html'
})
export class TrainingGroupMemberDeleteDialogComponent {

    trainingGroupMember: TrainingGroupMember;

    constructor(
        private trainingGroupMemberService: TrainingGroupMemberService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.trainingGroupMemberService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'trainingGroupMemberListModification',
                content: 'Deleted an trainingGroupMember'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-training-group-member-delete-popup',
    template: ''
})
export class TrainingGroupMemberDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private trainingGroupMemberPopupService: TrainingGroupMemberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.trainingGroupMemberPopupService
                .open(TrainingGroupMemberDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
