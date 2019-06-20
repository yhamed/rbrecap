/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RbRecapTestModule } from '../../../test.module';
import { DroitUpdateComponent } from 'app/entities/droit/droit-update.component';
import { DroitService } from 'app/entities/droit/droit.service';
import { Droit } from 'app/shared/model/droit.model';

describe('Component Tests', () => {
  describe('Droit Management Update Component', () => {
    let comp: DroitUpdateComponent;
    let fixture: ComponentFixture<DroitUpdateComponent>;
    let service: DroitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RbRecapTestModule],
        declarations: [DroitUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(DroitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DroitUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DroitService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Droit(123);
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
        const entity = new Droit();
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
