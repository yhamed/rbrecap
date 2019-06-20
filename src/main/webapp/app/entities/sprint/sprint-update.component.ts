import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ISprint, Sprint } from 'app/shared/model/sprint.model';
import { SprintService } from './sprint.service';

@Component({
  selector: 'jhi-sprint-update',
  templateUrl: './sprint-update.component.html'
})
export class SprintUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    sprintNumber: [null, [Validators.required, Validators.min(0)]],
    active: []
  });

  constructor(protected sprintService: SprintService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ sprint }) => {
      this.updateForm(sprint);
    });
  }

  updateForm(sprint: ISprint) {
    this.editForm.patchValue({
      id: sprint.id,
      sprintNumber: sprint.sprintNumber,
      active: sprint.active
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const sprint = this.createFromForm();
    if (sprint.id !== undefined) {
      this.subscribeToSaveResponse(this.sprintService.update(sprint));
    } else {
      this.subscribeToSaveResponse(this.sprintService.create(sprint));
    }
  }

  private createFromForm(): ISprint {
    const entity = {
      ...new Sprint(),
      id: this.editForm.get(['id']).value,
      sprintNumber: this.editForm.get(['sprintNumber']).value,
      active: this.editForm.get(['active']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISprint>>) {
    result.subscribe((res: HttpResponse<ISprint>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
