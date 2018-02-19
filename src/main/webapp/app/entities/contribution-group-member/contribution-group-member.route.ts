import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { ContributionGroupMemberComponent } from './contribution-group-member.component';
import { ContributionGroupMemberDetailComponent } from './contribution-group-member-detail.component';
import { ContributionGroupMemberPopupComponent } from './contribution-group-member-dialog.component';
import { ContributionGroupMemberDeletePopupComponent } from './contribution-group-member-delete-dialog.component';

export const contributionGroupMemberRoute: Routes = [
    {
        path: 'contribution-group-member',
        component: ContributionGroupMemberComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.contributionGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contribution-group-member/:id',
        component: ContributionGroupMemberDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.contributionGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contributionGroupMemberPopupRoute: Routes = [
    {
        path: 'contribution-group-member-new',
        component: ContributionGroupMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.contributionGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contribution-group-member/:id/edit',
        component: ContributionGroupMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.contributionGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contribution-group-member/:id/delete',
        component: ContributionGroupMemberDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.contributionGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
