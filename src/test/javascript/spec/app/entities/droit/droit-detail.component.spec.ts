/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RbRecapTestModule } from '../../../test.module';
import { DroitDetailComponent } from 'app/entities/droit/droit-detail.component';
import { Droit } from 'app/shared/model/droit.model';

describe('Component Tests', () => {
  describe('Droit Management Detail Component', () => {
    let comp: DroitDetailComponent;
    let fixture: ComponentFixture<DroitDetailComponent>;
    const route = ({ data: of({ droit: new Droit(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [RbRecapTestModule],
        declarations: [DroitDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(DroitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DroitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.droit).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
