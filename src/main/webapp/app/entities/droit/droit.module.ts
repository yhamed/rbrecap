import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RbRecapSharedModule } from 'app/shared';
import {
  DroitComponent,
  DroitDetailComponent,
  DroitUpdateComponent,
  DroitDeletePopupComponent,
  DroitDeleteDialogComponent,
  droitRoute,
  droitPopupRoute
} from './';

const ENTITY_STATES = [...droitRoute, ...droitPopupRoute];

@NgModule({
  imports: [RbRecapSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [DroitComponent, DroitDetailComponent, DroitUpdateComponent, DroitDeleteDialogComponent, DroitDeletePopupComponent],
  entryComponents: [DroitComponent, DroitUpdateComponent, DroitDeleteDialogComponent, DroitDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RbRecapDroitModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
