import { IArticle } from 'app/shared/model/article.model';

export interface ICategorie {
  id?: number;
  libelle?: string;
  description?: string;
  articles?: IArticle[];
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public libelle?: string, public description?: string, public articles?: IArticle[]) {}
}
