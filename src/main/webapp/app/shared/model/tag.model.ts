export interface ITag {
  id?: number;
  key?: string;
  name?: string;
  userStoryId?: number;
}

export class Tag implements ITag {
  constructor(public id?: number, public key?: string, public name?: string, public userStoryId?: number) {}
}
