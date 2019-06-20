import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'user-story',
        loadChildren: './user-story/user-story.module#RbRecapUserStoryModule'
      },
      {
        path: 'sprint',
        loadChildren: './sprint/sprint.module#RbRecapSprintModule'
      },
      {
        path: 'tag',
        loadChildren: './tag/tag.module#RbRecapTagModule'
      },
      {
        path: 'droit',
        loadChildren: './droit/droit.module#RbRecapDroitModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RbRecapEntityModule {}
