/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { MemberadminTestModule } from '../../../test.module';
import { ContributionGroupMemberDetailComponent } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member-detail.component';
import { ContributionGroupMemberService } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.service';
import { ContributionGroupMember } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.model';

describe('Component Tests', () => {

    describe('ContributionGroupMember Management Detail Component', () => {
        let comp: ContributionGroupMemberDetailComponent;
        let fixture: ComponentFixture<ContributionGroupMemberDetailComponent>;
        let service: ContributionGroupMemberService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MemberadminTestModule],
                declarations: [ContributionGroupMemberDetailComponent],
                providers: [
                    ContributionGroupMemberService
                ]
            })
            .overrideTemplate(ContributionGroupMemberDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContributionGroupMemberDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContributionGroupMemberService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ContributionGroupMember(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.contributionGroupMember).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
