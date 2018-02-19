import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MemberadminMemberModule } from './member/member.module';
import { MemberadminTrainingGroupModule } from './training-group/training-group.module';
import { MemberadminTrainingGroupMemberModule } from './training-group-member/training-group-member.module';
import { MemberadminContributionGroupModule } from './contribution-group/contribution-group.module';
import { MemberadminContributionGroupEntryModule } from './contribution-group-entry/contribution-group-entry.module';
import { MemberadminContributionGroupMemberModule } from './contribution-group-member/contribution-group-member.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        MemberadminMemberModule,
        MemberadminTrainingGroupModule,
        MemberadminTrainingGroupMemberModule,
        MemberadminContributionGroupModule,
        MemberadminContributionGroupEntryModule,
        MemberadminContributionGroupMemberModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MemberadminEntityModule {}
