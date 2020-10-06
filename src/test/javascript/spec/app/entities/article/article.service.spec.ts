import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ArticleService } from 'app/entities/article/article.service';
import { IArticle, Article } from 'app/shared/model/article.model';

describe('Service Tests', () => {
  describe('Article Service', () => {
    let injector: TestBed;
    let service: ArticleService;
    let httpMock: HttpTestingController;
    let elemDefault: IArticle;
    let expectedResult: IArticle | IArticle[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(ArticleService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Article(0, 'AAAAAAA', 'AAAAAAA', 'image/png', 'AAAAAAA', currentDate, false, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Article', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.create(new Article()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Article', () => {
        const returnedFromService = Object.assign(
          {
            libele: 'BBBBBB',
            description: 'BBBBBB',
            couverture: 'BBBBBB',
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            publier: true,
            docMDPath: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Article', () => {
        const returnedFromService = Object.assign(
          {
            libele: 'BBBBBB',
            description: 'BBBBBB',
            couverture: 'BBBBBB',
            dateCreation: currentDate.format(DATE_TIME_FORMAT),
            publier: true,
            docMDPath: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Article', () => {
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
