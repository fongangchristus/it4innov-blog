import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from './tag.service';
import { IArticle } from 'app/shared/model/article.model';
import { ArticleService } from 'app/entities/article/article.service';

@Component({
  selector: 'jhi-tag-update',
  templateUrl: './tag-update.component.html',
})
export class TagUpdateComponent implements OnInit {
  isSaving = false;
  articles: IArticle[] = [];

  editForm = this.fb.group({
    id: [],
    libele: [null, [Validators.required]],
    description: [],
    articles: [],
  });

  constructor(
    protected tagService: TagService,
    protected articleService: ArticleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tag }) => {
      this.updateForm(tag);

      this.articleService.query().subscribe((res: HttpResponse<IArticle[]>) => (this.articles = res.body || []));
    });
  }

  updateForm(tag: ITag): void {
    this.editForm.patchValue({
      id: tag.id,
      libele: tag.libele,
      description: tag.description,
      articles: tag.articles,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tag = this.createFromForm();
    if (tag.id !== undefined) {
      this.subscribeToSaveResponse(this.tagService.update(tag));
    } else {
      this.subscribeToSaveResponse(this.tagService.create(tag));
    }
  }

  private createFromForm(): ITag {
    return {
      ...new Tag(),
      id: this.editForm.get(['id'])!.value,
      libele: this.editForm.get(['libele'])!.value,
      description: this.editForm.get(['description'])!.value,
      articles: this.editForm.get(['articles'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITag>>): void {
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

  trackById(index: number, item: IArticle): any {
    return item.id;
  }

  getSelected(selectedVals: IArticle[], option: IArticle): IArticle {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
