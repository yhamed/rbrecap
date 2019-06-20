import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IDroit, Droit } from 'app/shared/model/droit.model';
import { DroitService } from './droit.service';
import { IUserStory } from 'app/shared/model/user-story.model';
import { UserStoryService } from 'app/entities/user-story';

@Component({
  selector: 'jhi-droit-update',
  templateUrl: './droit-update.component.html'
})
export class DroitUpdateComponent implements OnInit {
  isSaving: boolean;

  userstories: IUserStory[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    isSwitch: [],
    userStories: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected droitService: DroitService,
    protected userStoryService: UserStoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ droit }) => {
      this.updateForm(droit);
    });
    this.userStoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserStory[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserStory[]>) => response.body)
      )
      .subscribe((res: IUserStory[]) => (this.userstories = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(droit: IDroit) {
    this.editForm.patchValue({
      id: droit.id,
      name: droit.name,
      isSwitch: droit.isSwitch,
      userStories: droit.userStories
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const droit = this.createFromForm();
    if (droit.id !== undefined) {
      this.subscribeToSaveResponse(this.droitService.update(droit));
    } else {
      this.subscribeToSaveResponse(this.droitService.create(droit));
    }
  }

  private createFromForm(): IDroit {
    const entity = {
      ...new Droit(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      isSwitch: this.editForm.get(['isSwitch']).value,
      userStories: this.editForm.get(['userStories']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDroit>>) {
    result.subscribe((res: HttpResponse<IDroit>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

  trackUserStoryById(index: number, item: IUserStory) {
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
