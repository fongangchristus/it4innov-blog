import { Moment } from 'moment';
import { ITag } from 'app/shared/model/tag.model';

export interface IArticle {
  id?: number;
  libele?: string;
  description?: string;
  couvertureContentType?: string;
  couverture?: any;
  dateCreation?: Moment;
  publier?: boolean;
  docMDPath?: string;
  categorieId?: number;
  auteurId?: number;
  tags?: ITag[];
}

export class Article implements IArticle {
  constructor(
    public id?: number,
    public libele?: string,
    public description?: string,
    public couvertureContentType?: string,
    public couverture?: any,
    public dateCreation?: Moment,
    public publier?: boolean,
    public docMDPath?: string,
    public categorieId?: number,
    public auteurId?: number,
    public tags?: ITag[]
  ) {
    this.publier = this.publier || false;
  }
}
