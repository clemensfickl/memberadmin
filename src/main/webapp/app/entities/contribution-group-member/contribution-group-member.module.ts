import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MemberadminSharedModule } from '../../shared';
import {
    ContributionGroupMemberService,
    ContributionGroupMemberPopupService,
    ContributionGroupMemberComponent,
    ContributionGroupMemberDetailComponent,
    ContributionGroupMemberDialogComponent,
    ContributionGroupMemberPopupComponent,
    ContributionGroupMemberDeletePopupComponent,
    ContributionGroupMemberDeleteDialogComponent,
    contributionGroupMemberRoute,
    contributionGroupMemberPopupRoute,
} from './';

const ENTITY_STATES = [
    ...contributionGroupMemberRoute,
    ...contributionGroupMemberPopupRoute,
];

@NgModule({
    imports: [
        MemberadminSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        ContributionGroupMemberComponent,
        ContributionGroupMemberDetailComponent,
        ContributionGroupMemberDialogComponent,
        ContributionGroupMemberDeleteDialogComponent,
        ContributionGroupMemberPopupComponent,
        ContributionGroupMemberDeletePopupComponent,
    ],
    entryComponents: [
        ContributionGroupMemberComponent,
        ContributionGroupMemberDialogComponent,
        ContributionGroupMemberPopupComponent,
        ContributionGroupMemberDeleteDialogComponent,
        ContributionGroupMemberDeletePopupComponent,
    ],
    providers: [
        ContributionGroupMemberService,
        ContributionGroupMemberPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MemberadminContributionGroupMemberModule {}
