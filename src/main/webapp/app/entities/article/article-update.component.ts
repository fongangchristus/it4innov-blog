import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IArticle, Article } from 'app/shared/model/article.model';
import { ArticleService } from './article.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { ICategorie } from 'app/shared/model/categorie.model';
import { CategorieService } from 'app/entities/categorie/categorie.service';
import { IAuteur } from 'app/shared/model/auteur.model';
import { AuteurService } from 'app/entities/auteur/auteur.service';

type SelectableEntity = ICategorie | IAuteur;

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;
  categories: ICategorie[] = [];
  auteurs: IAuteur[] = [];

  editForm = this.fb.group({
    id: [],
    libele: [null, [Validators.required]],
    description: [],
    couverture: [],
    couvertureContentType: [],
    dateCreation: [],
    publier: [null, [Validators.required]],
    docMDPath: [],
    categorieId: [],
    auteurId: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected articleService: ArticleService,
    protected categorieService: CategorieService,
    protected auteurService: AuteurService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      if (!article.id) {
        const today = moment().startOf('day');
        article.dateCreation = today;
      }

      this.updateForm(article);

      this.categorieService.query().subscribe((res: HttpResponse<ICategorie[]>) => (this.categories = res.body || []));

      this.auteurService.query().subscribe((res: HttpResponse<IAuteur[]>) => (this.auteurs = res.body || []));
    });
  }

  updateForm(article: IArticle): void {
    this.editForm.patchValue({
      id: article.id,
      libele: article.libele,
      description: article.description,
      couverture: article.couverture,
      couvertureContentType: article.couvertureContentType,
      dateCreation: article.dateCreation ? article.dateCreation.format(DATE_TIME_FORMAT) : null,
      publier: article.publier,
      docMDPath: article.docMDPath,
      categorieId: article.categorieId,
      auteurId: article.auteurId,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('blogApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.createFromForm();
    if (article.id !== undefined) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  private createFromForm(): IArticle {
    return {
      ...new Article(),
      id: this.editForm.get(['id'])!.value,
      libele: this.editForm.get(['libele'])!.value,
      description: this.editForm.get(['description'])!.value,
      couvertureContentType: this.editForm.get(['couvertureContentType'])!.value,
      couverture: this.editForm.get(['couverture'])!.value,
      dateCreation: this.editForm.get(['dateCreation'])!.value
        ? moment(this.editForm.get(['dateCreation'])!.value, DATE_TIME_FORMAT)
        : undefined,
      publier: this.editForm.get(['publier'])!.value,
      docMDPath: this.editForm.get(['docMDPath'])!.value,
      categorieId: this.editForm.get(['categorieId'])!.value,
      auteurId: this.editForm.get(['auteurId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
