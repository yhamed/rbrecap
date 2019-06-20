import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RbRecapSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [RbRecapSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [RbRecapSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RbRecapSharedModule {
  static forRoot() {
    return {
      ngModule: RbRecapSharedModule
    };
  }
}
