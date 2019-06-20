import { IUserStory } from 'app/shared/model/user-story.model';

export interface IDroit {
  id?: number;
  name?: string;
  isSwitch?: boolean;
  userStories?: IUserStory[];
}

export class Droit implements IDroit {
  constructor(public id?: number, public name?: string, public isSwitch?: boolean, public userStories?: IUserStory[]) {
    this.isSwitch = this.isSwitch || false;
  }
}
