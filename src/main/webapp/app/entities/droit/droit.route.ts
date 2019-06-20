import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Droit } from 'app/shared/model/droit.model';
import { DroitService } from './droit.service';
import { DroitComponent } from './droit.component';
import { DroitDetailComponent } from './droit-detail.component';
import { DroitUpdateComponent } from './droit-update.component';
import { DroitDeletePopupComponent } from './droit-delete-dialog.component';
import { IDroit } from 'app/shared/model/droit.model';

@Injectable({ providedIn: 'root' })
export class DroitResolve implements Resolve<IDroit> {
  constructor(private service: DroitService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IDroit> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Droit>) => response.ok),
        map((droit: HttpResponse<Droit>) => droit.body)
      );
    }
    return of(new Droit());
  }
}

export const droitRoute: Routes = [
  {
    path: '',
    component: DroitComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.droit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: DroitDetailComponent,
    resolve: {
      droit: DroitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.droit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: DroitUpdateComponent,
    resolve: {
      droit: DroitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.droit.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: DroitUpdateComponent,
    resolve: {
      droit: DroitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.droit.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const droitPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: DroitDeletePopupComponent,
    resolve: {
      droit: DroitResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'rbRecapApp.droit.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
