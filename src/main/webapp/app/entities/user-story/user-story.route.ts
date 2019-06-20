import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { UserStory } from 'app/shared/model/user-story.model';
import { UserStoryService } from './user-story.service';
import { UserStoryComponent } from './user-story.component';
import { UserStoryDetailComponent } from './user-story-detail.component';
import { UserStoryUpdateComponent } from './user-story-update.component';
import { UserStoryDeletePopupComponent } from './user-story-delete-dialog.component';
import { IUserStory } from 'app/shared/model/user-story.model';

@Injectable({ providedIn: 'root' })
export class UserStoryResolve implements Resolve<IUserStory> {
  constructor(private service: UserStoryService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUserStory> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<UserStory>) => response.ok),
        map((userStory: HttpResponse<UserStory>) => userStory.body)
      );
    }
    return of(new UserStory());
  }
}

export const userStoryRoute: Routes = [
  {
    path: '',
    component: UserStoryComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.userStory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserStoryDetailComponent,
    resolve: {
      userStory: UserStoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.userStory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserStoryUpdateComponent,
    resolve: {
      userStory: UserStoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.userStory.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserStoryUpdateComponent,
    resolve: {
      userStory: UserStoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.userStory.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const userStoryPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UserStoryDeletePopupComponent,
    resolve: {
      userStory: UserStoryResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.userStory.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
