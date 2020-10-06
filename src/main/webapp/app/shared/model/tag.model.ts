import { IArticle } from 'app/shared/model/article.model';

export interface ITag {
  id?: number;
  libele?: string;
  description?: string;
  articles?: IArticle[];
}

export class Tag implements ITag {
  constructor(public id?: number, public libele?: string, public description?: string, public articles?: IArticle[]) {}
}
