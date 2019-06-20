/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RbRecapTestModule } from '../../../test.module';
import { UserStoryDetailComponent } from 'app/entities/user-story/user-story-detail.component';
import { UserStory } from 'app/shared/model/user-story.model';

describe('Component Tests', () => {
  describe('UserStory Management Detail Component', () => {
    let comp: UserStoryDetailComponent;
    let fixture: ComponentFixture<UserStoryDetailComponent>;
    const route = ({ data: of({ userStory: new UserStory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RbRecapTestModule],
        declarations: [UserStoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UserStoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UserStoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userStory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
