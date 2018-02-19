import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TrainingGroupMemberComponent } from './training-group-member.component';
import { TrainingGroupMemberDetailComponent } from './training-group-member-detail.component';
import { TrainingGroupMemberPopupComponent } from './training-group-member-dialog.component';
import { TrainingGroupMemberDeletePopupComponent } from './training-group-member-delete-dialog.component';

export const trainingGroupMemberRoute: Routes = [
    {
        path: 'training-group-member',
        component: TrainingGroupMemberComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.trainingGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'training-group-member/:id',
        component: TrainingGroupMemberDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.trainingGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const trainingGroupMemberPopupRoute: Routes = [
    {
        path: 'training-group-member-new',
        component: TrainingGroupMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.trainingGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'training-group-member/:id/edit',
        component: TrainingGroupMemberPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.trainingGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'training-group-member/:id/delete',
        component: TrainingGroupMemberDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'memberadminApp.trainingGroupMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
