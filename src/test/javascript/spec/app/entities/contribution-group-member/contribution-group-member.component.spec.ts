/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MemberadminTestModule } from '../../../test.module';
import { ContributionGroupMemberComponent } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.component';
import { ContributionGroupMemberService } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.service';
import { ContributionGroupMember } from '../../../../../../main/webapp/app/entities/contribution-group-member/contribution-group-member.model';

describe('Component Tests', () => {

    describe('ContributionGroupMember Management Component', () => {
        let comp: ContributionGroupMemberComponent;
        let fixture: ComponentFixture<ContributionGroupMemberComponent>;
        let service: ContributionGroupMemberService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MemberadminTestModule],
                declarations: [ContributionGroupMemberComponent],
                providers: [
                    ContributionGroupMemberService
                ]
            })
            .overrideTemplate(ContributionGroupMemberComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContributionGroupMemberComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContributionGroupMemberService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ContributionGroupMember(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.contributionGroupMembers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
