/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { RbRecapTestModule } from '../../../test.module';
import { UserStoryUpdateComponent } from 'app/entities/user-story/user-story-update.component';
import { UserStoryService } from 'app/entities/user-story/user-story.service';
import { UserStory } from 'app/shared/model/user-story.model';

describe('Component Tests', () => {
  describe('UserStory Management Update Component', () => {
    let comp: UserStoryUpdateComponent;
    let fixture: ComponentFixture<UserStoryUpdateComponent>;
    let service: UserStoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RbRecapTestModule],
        declarations: [UserStoryUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UserStoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UserStoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UserStoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new UserStory(123);
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
        const entity = new UserStory();
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
