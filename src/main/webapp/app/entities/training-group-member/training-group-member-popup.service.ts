import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { TrainingGroupMember } from './training-group-member.model';
import { TrainingGroupMemberService } from './training-group-member.service';

@Injectable()
export class TrainingGroupMemberPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private trainingGroupMemberService: TrainingGroupMemberService

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
                this.trainingGroupMemberService.find(id)
                    .subscribe((trainingGroupMemberResponse: HttpResponse<TrainingGroupMember>) => {
                        const trainingGroupMember: TrainingGroupMember = trainingGroupMemberResponse.body;
                        if (trainingGroupMember.startDate) {
                            trainingGroupMember.startDate = {
                                year: trainingGroupMember.startDate.getFullYear(),
                                month: trainingGroupMember.startDate.getMonth() + 1,
                                day: trainingGroupMember.startDate.getDate()
                            };
                        }
                        if (trainingGroupMember.endDate) {
                            trainingGroupMember.endDate = {
                                year: trainingGroupMember.endDate.getFullYear(),
                                month: trainingGroupMember.endDate.getMonth() + 1,
                                day: trainingGroupMember.endDate.getDate()
                            };
                        }
                        this.ngbModalRef = this.trainingGroupMemberModalRef(component, trainingGroupMember);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.trainingGroupMemberModalRef(component, new TrainingGroupMember());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    trainingGroupMemberModalRef(component: Component, trainingGroupMember: TrainingGroupMember): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.trainingGroupMember = trainingGroupMember;
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
