import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RbRecapSharedModule } from 'app/shared';
import {
  SprintComponent,
  SprintDetailComponent,
  SprintUpdateComponent,
  SprintDeletePopupComponent,
  SprintDeleteDialogComponent,
  sprintRoute,
  sprintPopupRoute
} from './';

const ENTITY_STATES = [...sprintRoute, ...sprintPopupRoute];

@NgModule({
  imports: [RbRecapSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [SprintComponent, SprintDetailComponent, SprintUpdateComponent, SprintDeleteDialogComponent, SprintDeletePopupComponent],
  entryComponents: [SprintComponent, SprintUpdateComponent, SprintDeleteDialogComponent, SprintDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RbRecapSprintModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
