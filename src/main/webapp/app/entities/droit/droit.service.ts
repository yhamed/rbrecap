import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDroit } from 'app/shared/model/droit.model';

type EntityResponseType = HttpResponse<IDroit>;
type EntityArrayResponseType = HttpResponse<IDroit[]>;

@Injectable({ providedIn: 'root' })
export class DroitService {
  public resourceUrl = SERVER_API_URL + 'api/droits';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/droits';

  constructor(protected http: HttpClient) {}

  create(droit: IDroit): Observable<EntityResponseType> {
    return this.http.post<IDroit>(this.resourceUrl, droit, { observe: 'response' });
  }

  update(droit: IDroit): Observable<EntityResponseType> {
    return this.http.put<IDroit>(this.resourceUrl, droit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDroit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDroit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDroit[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
