/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RbRecapTestModule } from '../../../test.module';
import { UserStoryDeleteDialogComponent } from 'app/entities/user-story/user-story-delete-dialog.component';
import { UserStoryService } from 'app/entities/user-story/user-story.service';

describe('Component Tests', () => {
  describe('UserStory Management Delete Component', () => {
    let comp: UserStoryDeleteDialogComponent;
    let fixture: ComponentFixture<UserStoryDeleteDialogComponent>;
    let service: UserStoryService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RbRecapTestModule],
        declarations: [UserStoryDeleteDialogComponent]
      })
        .overrideTemplate(UserStoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserStoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserStoryService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
