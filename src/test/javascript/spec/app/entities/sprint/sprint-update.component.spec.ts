/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RbRecapTestModule } from '../../../test.module';
import { SprintUpdateComponent } from 'app/entities/sprint/sprint-update.component';
import { SprintService } from 'app/entities/sprint/sprint.service';
import { Sprint } from 'app/shared/model/sprint.model';

describe('Component Tests', () => {
  describe('Sprint Management Update Component', () => {
    let comp: SprintUpdateComponent;
    let fixture: ComponentFixture<SprintUpdateComponent>;
    let service: SprintService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RbRecapTestModule],
        declarations: [SprintUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SprintUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SprintUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SprintService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Sprint(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Sprint();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
