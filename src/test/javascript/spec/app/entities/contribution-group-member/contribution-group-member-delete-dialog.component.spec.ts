/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { MemberadminTestModule } from '../../../test.module';
import { ContributionGroupMemberDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member-delete-dialog.component';
import { ContributionGroupMemberService } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.service';

describe('Component Tests', () => {

    describe('ContributionGroupMember Management Delete Component', () => {
        let comp: ContributionGroupMemberDeleteDialogComponent;
        let fixture: ComponentFixture<ContributionGroupMemberDeleteDialogComponent>;
        let service: ContributionGroupMemberService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MemberadminTestModule],
                declarations: [ContributionGroupMemberDeleteDialogComponent],
                providers: [
                    ContributionGroupMemberService
                ]
            })
            .overrideTemplate(ContributionGroupMemberDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContributionGroupMemberDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContributionGroupMemberService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
