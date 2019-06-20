import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { RbRecapSharedModule } from 'app/shared';
import {
  UserStoryComponent,
  UserStoryDetailComponent,
  UserStoryUpdateComponent,
  UserStoryDeletePopupComponent,
  UserStoryDeleteDialogComponent,
  userStoryRoute,
  userStoryPopupRoute
} from './';

const ENTITY_STATES = [...userStoryRoute, ...userStoryPopupRoute];

@NgModule({
  imports: [RbRecapSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    UserStoryComponent,
    UserStoryDetailComponent,
    UserStoryUpdateComponent,
    UserStoryDeleteDialogComponent,
    UserStoryDeletePopupComponent
  ],
  entryComponents: [UserStoryComponent, UserStoryUpdateComponent, UserStoryDeleteDialogComponent, UserStoryDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RbRecapUserStoryModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
