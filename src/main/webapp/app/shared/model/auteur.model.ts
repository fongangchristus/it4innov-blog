import { IArticle } from 'app/shared/model/article.model';

export interface IAuteur {
  id?: number;
  login?: string;
  firstname?: string;
  password?: string;
  profileContentType?: string;
  profile?: any;
  tweeter?: string;
  linkedin?: string;
  facebook?: string;
  description?: string;
  slogan?: string;
  articles?: IArticle[];
}

export class Auteur implements IAuteur {
  constructor(
    public id?: number,
    public login?: string,
    public firstname?: string,
    public password?: string,
    public profileContentType?: string,
    public profile?: any,
    public tweeter?: string,
    public linkedin?: string,
    public facebook?: string,
    public description?: string,
    public slogan?: string,
    public articles?: IArticle[]
  ) {}
}
