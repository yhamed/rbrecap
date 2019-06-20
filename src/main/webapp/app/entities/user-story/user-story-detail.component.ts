import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserStory } from 'app/shared/model/user-story.model';

@Component({
  selector: 'jhi-user-story-detail',
  templateUrl: './user-story-detail.component.html'
})
export class UserStoryDetailComponent implements OnInit {
  userStory: IUserStory;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userStory }) => {
      this.userStory = userStory;
    });
  }

  previousState() {
    window.history.back();
  }
}
