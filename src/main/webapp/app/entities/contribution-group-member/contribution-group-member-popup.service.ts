import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ContributionGroupMember } from './contribution-group-member.model';
import { ContributionGroupMemberService } from './contribution-group-member.service';

@Injectable()
export class ContributionGroupMemberPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private contributionGroupMemberService: ContributionGroupMemberService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.contributionGroupMemberService.find(id)
                    .subscribe((contributionGroupMemberResponse: HttpResponse<ContributionGroupMember>) => {
                        const contributionGroupMember: ContributionGroupMember = contributionGroupMemberResponse.body;
                        if (contributionGroupMember.startDate) {
                            contributionGroupMember.startDate = {
                                year: contributionGroupMember.startDate.getFullYear(),
                                month: contributionGroupMember.startDate.getMonth() + 1,
                                day: contributionGroupMember.startDate.getDate()
                            };
                        }
                        if (contributionGroupMember.endDate) {
                            contributionGroupMember.endDate = {
                                year: contributionGroupMember.endDate.getFullYear(),
                                month: contributionGroupMember.endDate.getMonth() + 1,
                                day: contributionGroupMember.endDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.contributionGroupMemberModalRef(component, contributionGroupMember);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.contributionGroupMemberModalRef(component, new ContributionGroupMember());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    contributionGroupMemberModalRef(component: Component, contributionGroupMember: ContributionGroupMember): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.contributionGroupMember = contributionGroupMember;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
