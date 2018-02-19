/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { MemberadminTestModule } from '../../../test.module';
import { TrainingGroupMemberDialogComponent } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member-dialog.component';
import { TrainingGroupMemberService } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member.service';
import { TrainingGroupMember } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member.model';
import { TrainingGroupService } from '../../../../../../main/webapp/app/entities/training-group';
import { MemberService } from '../../../../../../main/webapp/app/entities/member';

describe('Component Tests', () => {

    describe('TrainingGroupMember Management Dialog Component', () => {
        let comp: TrainingGroupMemberDialogComponent;
        let fixture: ComponentFixture<TrainingGroupMemberDialogComponent>;
        let service: TrainingGroupMemberService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MemberadminTestModule],
                declarations: [TrainingGroupMemberDialogComponent],
                providers: [
                    TrainingGroupService,
                    MemberService,
                    TrainingGroupMemberService
                ]
            })
            .overrideTemplate(TrainingGroupMemberDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TrainingGroupMemberDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrainingGroupMemberService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TrainingGroupMember(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.trainingGroupMember = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'trainingGroupMemberListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new TrainingGroupMember();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.trainingGroupMember = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'trainingGroupMemberListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
