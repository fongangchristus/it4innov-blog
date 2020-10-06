import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IAuteur, Auteur } from 'app/shared/model/auteur.model';
import { AuteurService } from './auteur.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-auteur-update',
  templateUrl: './auteur-update.component.html',
})
export class AuteurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    login: [null, [Validators.required]],
    firstname: [],
    password: [null, [Validators.required]],
    profile: [],
    profileContentType: [],
    tweeter: [],
    linkedin: [],
    facebook: [],
    description: [],
    slogan: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected auteurService: AuteurService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ auteur }) => {
      this.updateForm(auteur);
    });
  }

  updateForm(auteur: IAuteur): void {
    this.editForm.patchValue({
      id: auteur.id,
      login: auteur.login,
      firstname: auteur.firstname,
      password: auteur.password,
      profile: auteur.profile,
      profileContentType: auteur.profileContentType,
      tweeter: auteur.tweeter,
      linkedin: auteur.linkedin,
      facebook: auteur.facebook,
      description: auteur.description,
      slogan: auteur.slogan,
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
    const auteur = this.createFromForm();
    if (auteur.id !== undefined) {
      this.subscribeToSaveResponse(this.auteurService.update(auteur));
    } else {
      this.subscribeToSaveResponse(this.auteurService.create(auteur));
    }
  }

  private createFromForm(): IAuteur {
    return {
      ...new Auteur(),
      id: this.editForm.get(['id'])!.value,
      login: this.editForm.get(['login'])!.value,
      firstname: this.editForm.get(['firstname'])!.value,
      password: this.editForm.get(['password'])!.value,
      profileContentType: this.editForm.get(['profileContentType'])!.value,
      profile: this.editForm.get(['profile'])!.value,
      tweeter: this.editForm.get(['tweeter'])!.value,
      linkedin: this.editForm.get(['linkedin'])!.value,
      facebook: this.editForm.get(['facebook'])!.value,
      description: this.editForm.get(['description'])!.value,
      slogan: this.editForm.get(['slogan'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuteur>>): void {
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
}
