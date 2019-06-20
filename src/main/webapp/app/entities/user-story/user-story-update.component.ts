import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IUserStory, UserStory } from 'app/shared/model/user-story.model';
import { UserStoryService } from './user-story.service';
import { ISprint } from 'app/shared/model/sprint.model';
import { SprintService } from 'app/entities/sprint';
import { IDroit } from 'app/shared/model/droit.model';
import { DroitService } from 'app/entities/droit';

@Component({
  selector: 'jhi-user-story-update',
  templateUrl: './user-story-update.component.html'
})
export class UserStoryUpdateComponent implements OnInit {
  isSaving: boolean;

  sprints: ISprint[];

  droits: IDroit[];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    chiffrage: [null, [Validators.min(1), Validators.max(8)]],
    sprintId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected userStoryService: UserStoryService,
    protected sprintService: SprintService,
    protected droitService: DroitService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ userStory }) => {
      this.updateForm(userStory);
    });
    this.sprintService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISprint[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISprint[]>) => response.body)
      )
      .subscribe((res: ISprint[]) => (this.sprints = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.droitService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDroit[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDroit[]>) => response.body)
      )
      .subscribe((res: IDroit[]) => (this.droits = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(userStory: IUserStory) {
    this.editForm.patchValue({
      id: userStory.id,
      name: userStory.name,
      description: userStory.description,
      chiffrage: userStory.chiffrage,
      sprintId: userStory.sprintId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const userStory = this.createFromForm();
    if (userStory.id !== undefined) {
      this.subscribeToSaveResponse(this.userStoryService.update(userStory));
    } else {
      this.subscribeToSaveResponse(this.userStoryService.create(userStory));
    }
  }

  private createFromForm(): IUserStory {
    const entity = {
      ...new UserStory(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      chiffrage: this.editForm.get(['chiffrage']).value,
      sprintId: this.editForm.get(['sprintId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserStory>>) {
    result.subscribe((res: HttpResponse<IUserStory>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackSprintById(index: number, item: ISprint) {
    return item.id;
  }

  trackDroitById(index: number, item: IDroit) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
