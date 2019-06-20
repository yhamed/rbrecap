import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDroit } from 'app/shared/model/droit.model';

@Component({
  selector: 'jhi-droit-detail',
  templateUrl: './droit-detail.component.html'
})
export class DroitDetailComponent implements OnInit {
  droit: IDroit;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ droit }) => {
      this.droit = droit;
    });
  }

  previousState() {
    window.history.back();
  }
}
