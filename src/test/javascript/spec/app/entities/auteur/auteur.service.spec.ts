import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuteurService } from 'app/entities/auteur/auteur.service';
import { IAuteur, Auteur } from 'app/shared/model/auteur.model';

describe('Service Tests', () => {
  describe('Auteur Service', () => {
    let injector: TestBed;
    let service: AuteurService;
    let httpMock: HttpTestingController;
    let elemDefault: IAuteur;
    let expectedResult: IAuteur | IAuteur[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AuteurService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Auteur(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'image/png',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Auteur', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Auteur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Auteur', () => {
        const returnedFromService = Object.assign(
          {
            login: 'BBBBBB',
            firstname: 'BBBBBB',
            password: 'BBBBBB',
            profile: 'BBBBBB',
            tweeter: 'BBBBBB',
            linkedin: 'BBBBBB',
            facebook: 'BBBBBB',
            description: 'BBBBBB',
            slogan: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Auteur', () => {
        const returnedFromService = Object.assign(
          {
            login: 'BBBBBB',
            firstname: 'BBBBBB',
            password: 'BBBBBB',
            profile: 'BBBBBB',
            tweeter: 'BBBBBB',
            linkedin: 'BBBBBB',
            facebook: 'BBBBBB',
            description: 'BBBBBB',
            slogan: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Auteur', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});