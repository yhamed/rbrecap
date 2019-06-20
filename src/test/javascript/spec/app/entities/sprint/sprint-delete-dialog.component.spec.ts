/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { RbRecapTestModule } from '../../../test.module';
import { SprintDeleteDialogComponent } from 'app/entities/sprint/sprint-delete-dialog.component';
import { SprintService } from 'app/entities/sprint/sprint.service';

describe('Component Tests', () => {
  describe('Sprint Management Delete Component', () => {
    let comp: SprintDeleteDialogComponent;
    let fixture: ComponentFixture<SprintDeleteDialogComponent>;
    let service: SprintService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RbRecapTestModule],
        declarations: [SprintDeleteDialogComponent]
      })
        .overrideTemplate(SprintDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SprintDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SprintService);
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
