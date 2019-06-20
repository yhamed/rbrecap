import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { IUserStory } from 'app/shared/model/user-story.model';
import { UserStoryService } from 'app/entities/user-story';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html'
})
export class TagUpdateComponent implements OnInit {
  isSaving: boolean;

  userstories: IUserStory[];

  editForm = this.fb.group({
    id: [],
    key: [null, [Validators.required]],
    name: [null, [Validators.required]],
    userStoryId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected tagService: TagService,
    protected userStoryService: UserStoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.updateForm(tag);
    });
    this.userStoryService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUserStory[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserStory[]>) => response.body)
      )
      .subscribe((res: IUserStory[]) => (this.userstories = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(tag: ITag) {
    this.editForm.patchValue({
      id: tag.id,
      key: tag.key,
      name: tag.name,
      userStoryId: tag.userStoryId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const tag = this.createFromForm();
    if (tag.id !== undefined) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  private createFromForm(): ITag {
    const entity = {
      ...new Tag(),
      id: this.editForm.get(['id']).value,
      key: this.editForm.get(['key']).value,
      name: this.editForm.get(['name']).value,
      userStoryId: this.editForm.get(['userStoryId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>) {
    result.subscribe((res: HttpResponse<ITag>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
