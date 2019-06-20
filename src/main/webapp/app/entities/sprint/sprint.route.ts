import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Sprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';
import { SprintComponent } from './sprint.component';
import { SprintDetailComponent } from './sprint-detail.component';
import { SprintUpdateComponent } from './sprint-update.component';
import { SprintDeletePopupComponent } from './sprint-delete-dialog.component';
import { ISprint } from 'app/shared/model/sprint.model';

@Injectable({ providedIn: 'root' })
export class SprintResolve implements Resolve<ISprint> {
  constructor(private service: SprintService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISprint> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Sprint>) => response.ok),
        map((sprint: HttpResponse<Sprint>) => sprint.body)
      );
    }
    return of(new Sprint());
  }
}

export const sprintRoute: Routes = [
  {
    path: '',
    component: SprintComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.sprint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SprintDetailComponent,
    resolve: {
      sprint: SprintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.sprint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SprintUpdateComponent,
    resolve: {
      sprint: SprintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.sprint.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SprintUpdateComponent,
    resolve: {
      sprint: SprintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.sprint.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const sprintPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SprintDeletePopupComponent,
    resolve: {
      sprint: SprintResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.sprint.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
