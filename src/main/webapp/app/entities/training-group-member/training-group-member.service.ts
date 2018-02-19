import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TrainingGroupMember } from './training-group-member.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<TrainingGroupMember>;

@Injectable()
export class TrainingGroupMemberService {

    private resourceUrl =  SERVER_API_URL + 'api/training-group-members';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/training-group-members';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(trainingGroupMember: TrainingGroupMember): Observable<EntityResponseType> {
        const copy = this.convert(trainingGroupMember);
        return this.http.post<TrainingGroupMember>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(trainingGroupMember: TrainingGroupMember): Observable<EntityResponseType> {
        const copy = this.convert(trainingGroupMember);
        return this.http.put<TrainingGroupMember>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<TrainingGroupMember>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<TrainingGroupMember[]>> {
        const options = createRequestOption(req);
        return this.http.get<TrainingGroupMember[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TrainingGroupMember[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<TrainingGroupMember[]>> {
        const options = createRequestOption(req);
        return this.http.get<TrainingGroupMember[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<TrainingGroupMember[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: TrainingGroupMember = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<TrainingGroupMember[]>): HttpResponse<TrainingGroupMember[]> {
        const jsonResponse: TrainingGroupMember[] = res.body;
        const body: TrainingGroupMember[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to TrainingGroupMember.
     */
    private convertItemFromServer(trainingGroupMember: TrainingGroupMember): TrainingGroupMember {
        const copy: TrainingGroupMember = Object.assign({}, trainingGroupMember);
        copy.startDate = this.dateUtils
            .convertLocalDateFromServer(trainingGroupMember.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateFromServer(trainingGroupMember.endDate);
        return copy;
    }

    /**
     * Convert a TrainingGroupMember to a JSON which can be sent to the server.
     */
    private convert(trainingGroupMember: TrainingGroupMember): TrainingGroupMember {
        const copy: TrainingGroupMember = Object.assign({}, trainingGroupMember);
        copy.startDate = this.dateUtils
            .convertLocalDateToServer(trainingGroupMember.startDate);
        copy.endDate = this.dateUtils
            .convertLocalDateToServer(trainingGroupMember.endDate);
        return copy;
    }
}
