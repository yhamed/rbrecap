import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUserStory } from 'app/shared/model/user-story.model';

type EntityResponseType = HttpResponse<IUserStory>;
type EntityArrayResponseType = HttpResponse<IUserStory[]>;

@Injectable({ providedIn: 'root' })
export class UserStoryService {
  public resourceUrl = SERVER_API_URL + 'api/user-stories';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/user-stories';

  constructor(protected http: HttpClient) {}

  create(userStory: IUserStory): Observable<EntityResponseType> {
    return this.http.post<IUserStory>(this.resourceUrl, userStory, { observe: 'response' });
  }

  update(userStory: IUserStory): Observable<EntityResponseType> {
    return this.http.put<IUserStory>(this.resourceUrl, userStory, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserStory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserStory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserStory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
