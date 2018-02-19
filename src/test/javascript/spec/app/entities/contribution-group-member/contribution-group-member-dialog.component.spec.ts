/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { MemberadminTestModule } from '../../../test.module';
import { ContributionGroupMemberDialogComponent } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member-dialog.component';
import { ContributionGroupMemberService } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.service';
import { ContributionGroupMember } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.model';
import { ContributionGroupService } from '../../../../../../main/webapp/app/entities/contribution-group';
import { MemberService } from '../../../../../../main/webapp/app/entities/member';

describe('Component Tests', () => {

    describe('ContributionGroupMember Management Dialog Component', () => {
        let comp: ContributionGroupMemberDialogComponent;
        let fixture: ComponentFixture<ContributionGroupMemberDialogComponent>;
        let service: ContributionGroupMemberService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MemberadminTestModule],
                declarations: [ContributionGroupMemberDialogComponent],
                providers: [
                    ContributionGroupService,
                    MemberService,
                    ContributionGroupMemberService
                ]
            })
            .overrideTemplate(ContributionGroupMemberDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContributionGroupMemberDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContributionGroupMemberService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ContributionGroupMember(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.contributionGroupMember = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'contributionGroupMemberListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ContributionGroupMember();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.contributionGroupMember = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'contributionGroupMemberListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
