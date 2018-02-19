import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ContributionGroupMember } from './contribution-group-member.model';
import { ContributionGroupMemberPopupService } from './contribution-group-member-popup.service';
import { ContributionGroupMemberService } from './contribution-group-member.service';

@Component({
    selector: 'jhi-contribution-group-member-delete-dialog',
    templateUrl: './contribution-group-member-delete-dialog.component.html'
})
export class ContributionGroupMemberDeleteDialogComponent {

    contributionGroupMember: ContributionGroupMember;

    constructor(
        private contributionGroupMemberService: ContributionGroupMemberService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contributionGroupMemberService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contributionGroupMemberListModification',
                content: 'Deleted an contributionGroupMember'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-contribution-group-member-delete-popup',
    template: ''
})
export class ContributionGroupMemberDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contributionGroupMemberPopupService: ContributionGroupMemberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.contributionGroupMemberPopupService
                .open(ContributionGroupMemberDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
