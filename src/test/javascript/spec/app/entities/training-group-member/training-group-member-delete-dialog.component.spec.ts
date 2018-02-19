/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { MemberadminTestModule } from '../../../test.module';
import { TrainingGroupMemberDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member-delete-dialog.component';
import { TrainingGroupMemberService } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member.service';

describe('Component Tests', () => {

    describe('TrainingGroupMember Management Delete Component', () => {
        let comp: TrainingGroupMemberDeleteDialogComponent;
        let fixture: ComponentFixture<TrainingGroupMemberDeleteDialogComponent>;
        let service: TrainingGroupMemberService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MemberadminTestModule],
                declarations: [TrainingGroupMemberDeleteDialogComponent],
                providers: [
                    TrainingGroupMemberService
                ]
            })
            .overrideTemplate(TrainingGroupMemberDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TrainingGroupMemberDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrainingGroupMemberService);
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
