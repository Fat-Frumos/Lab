import {TestBed} from '@angular/core/testing';

import {CertificateService} from '../certificate.service';
import {FilterPipe} from "../../pipe/filter.pipe";
import {LocalStorageService} from "../local-storage.service";

describe('CertificateService', () => {
  let service: CertificateService;
  let filterPipe: FilterPipe;
  let localStorageService: LocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CertificateService,
        FilterPipe,
        LocalStorageService,
      ],
    });
    service = TestBed.inject(CertificateService);
    filterPipe = TestBed.inject(FilterPipe);
    localStorageService = TestBed.inject(LocalStorageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should filter certificates', () => {
    const mockCertificates = [{
      id: '1',
      name: 'test',
      description: 'test',
      shortDescription: 'test',
      company: 'test',
      price: 100,
      duration: 1,
      favorite: false,
      checkout: false,
      createDate: new Date(),
      lastUpdate: new Date(),
      path: 'test',
      tags: new Set([{id: 1, name: 'test'}])
    }];
    const mockCriteria = {name: 'test', tag: 'test'};
    spyOn(localStorageService, 'getCertificatesFromLocalStorage').and.returnValue(mockCertificates);
    spyOn(filterPipe, 'transform').and.callThrough();

    service.criteria = mockCriteria;
    service.filter();

    expect(filterPipe.transform).toHaveBeenCalledWith(mockCertificates, mockCriteria);
    expect(service.certificates$).toEqual(mockCertificates);
  });
});
