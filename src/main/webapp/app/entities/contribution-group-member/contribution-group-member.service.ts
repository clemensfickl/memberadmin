import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { ContributionGroupMember } from './contribution-group-member.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ContributionGroupMember>;

@Injectable()
export class ContributionGroupMemberService {

    private resourceUrl =  SERVER_API_URL + 'api/contribution-group-members';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/contribution-group-members';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(contributionGroupMember: ContributionGroupMember): Observable<EntityResponseType> {
        const copy = this.convert(contributionGroupMember);
        return this.http.post<ContributionGroupMember>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(contributionGroupMember: ContributionGroupMember): Observable<EntityResponseType> {
        const copy = this.convert(contributionGroupMember);
        return this.http.put<ContributionGroupMember>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ContributionGroupMember>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ContributionGroupMember[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContributionGroupMember[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContributionGroupMember[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<ContributionGroupMember[]>> {
        const options = createRequestOption(req);
        return this.http.get<ContributionGroupMember[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ContributionGroupMember[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ContributionGroupMember = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ContributionGroupMember[]>): HttpResponse<ContributionGroupMember[]> {
        const jsonResponse: ContributionGroupMember[] = res.body;
        const body: ContributionGroupMember[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ContributionGroupMember.
     */
    private convertItemFromServer(contributionGroupMember: ContributionGroupMember): ContributionGroupMember {
        const copy: ContributionGroupMember = Object.assign({}, contributionGroupMember);
        copy.startDate = this.dateUtils
            .convertLocalDateFromServer(contributionGroupMember.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateFromServer(contributionGroupMember.endDate);
        return copy;
    }

    /**
     * Convert a ContributionGroupMember to a JSON which can be sent to the server.
     */
    private convert(contributionGroupMember: ContributionGroupMember): ContributionGroupMember {
        const copy: ContributionGroupMember = Object.assign({}, contributionGroupMember);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(contributionGroupMember.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(contributionGroupMember.endDate);
        return copy;
    }
}
