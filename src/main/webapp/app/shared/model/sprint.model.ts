import { IUserStory } from 'app/shared/model/user-story.model';

export interface ISprint {
  id?: number;
  sprintNumber?: number;
  active?: boolean;
  userStories?: IUserStory[];
}

export class Sprint implements ISprint {
  constructor(public id?: number, public sprintNumber?: number, public active?: boolean, public userStories?: IUserStory[]) {
    this.active = this.active || false;
  }
}
