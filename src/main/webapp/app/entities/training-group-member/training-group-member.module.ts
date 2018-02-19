import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MemberadminSharedModule } from '../../shared';
import {
    TrainingGroupMemberService,
    TrainingGroupMemberPopupService,
    TrainingGroupMemberComponent,
    TrainingGroupMemberDetailComponent,
    TrainingGroupMemberDialogComponent,
    TrainingGroupMemberPopupComponent,
    TrainingGroupMemberDeletePopupComponent,
    TrainingGroupMemberDeleteDialogComponent,
    trainingGroupMemberRoute,
    trainingGroupMemberPopupRoute,
} from './';

const ENTITY_STATES = [
    ...trainingGroupMemberRoute,
    ...trainingGroupMemberPopupRoute,
];

@NgModule({
    imports: [
        MemberadminSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TrainingGroupMemberComponent,
        TrainingGroupMemberDetailComponent,
        TrainingGroupMemberDialogComponent,
        TrainingGroupMemberDeleteDialogComponent,
        TrainingGroupMemberPopupComponent,
        TrainingGroupMemberDeletePopupComponent,
    ],
    entryComponents: [
        TrainingGroupMemberComponent,
        TrainingGroupMemberDialogComponent,
        TrainingGroupMemberPopupComponent,
        TrainingGroupMemberDeleteDialogComponent,
        TrainingGroupMemberDeletePopupComponent,
    ],
    providers: [
        TrainingGroupMemberService,
        TrainingGroupMemberPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MemberadminTrainingGroupMemberModule {}
