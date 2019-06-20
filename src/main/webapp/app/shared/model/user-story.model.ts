import { ITag } from 'app/shared/model/tag.model';
import { IDroit } from 'app/shared/model/droit.model';

export interface IUserStory {
  id?: number;
  name?: string;
  description?: string;
  chiffrage?: number;
  sprintId?: number;
  tags?: ITag[];
  droits?: IDroit[];
}

export class UserStory implements IUserStory {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public chiffrage?: number,
    public sprintId?: number,
    public tags?: ITag[],
    public droits?: IDroit[]
  ) {}
}
