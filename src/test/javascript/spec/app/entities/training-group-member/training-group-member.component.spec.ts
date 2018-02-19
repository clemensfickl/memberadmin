/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MemberadminTestModule } from '../../../test.module';
import { TrainingGroupMemberComponent } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member.component';
import { TrainingGroupMemberService } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member.service';
import { TrainingGroupMember } from '../../../../../../main/webapp/app/entities/training-group-member/training-group-member.model';

describe('Component Tests', () => {

    describe('TrainingGroupMember Management Component', () => {
        let comp: TrainingGroupMemberComponent;
        let fixture: ComponentFixture<TrainingGroupMemberComponent>;
        let service: TrainingGroupMemberService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MemberadminTestModule],
                declarations: [TrainingGroupMemberComponent],
                providers: [
                    TrainingGroupMemberService
                ]
            })
            .overrideTemplate(TrainingGroupMemberComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TrainingGroupMemberComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TrainingGroupMemberService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new TrainingGroupMember(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.trainingGroupMembers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
