import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISprint } from 'app/shared/model/sprint.model';

@Component({
  selector: 'jhi-sprint-detail',
  templateUrl: './sprint-detail.component.html'
})
export class SprintDetailComponent implements OnInit {
  sprint: ISprint;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ sprint }) => {
      this.sprint = sprint;
    });
  }

  previousState() {
    window.history.back();
  }
}
