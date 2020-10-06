import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'auteur',
        loadChildren: () => import('./auteur/auteur.module').then(m => m.BlogAuteurModule),
      },
      {
        path: 'article',
        loadChildren: () => import('./article/article.module').then(m => m.BlogArticleModule),
      },
      {
        path: 'tag',
        loadChildren: () => import('./tag/tag.module').then(m => m.BlogTagModule),
      },
      {
        path: 'categorie',
        loadChildren: () => import('./categorie/categorie.module').then(m => m.BlogCategorieModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class BlogEntityModule {}
