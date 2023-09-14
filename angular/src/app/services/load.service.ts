import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { Certificate } from '../model/Certificate';
import {Tag} from "../model/Tag";

@Injectable({
  providedIn: 'root',
})
export class LoadService {
  private apiUrl: string = 'https://gift-store.onrender.com/api/certificates';

  constructor(private http: HttpClient) {}

  getCertificates(page: number, size: number): Observable<Certificate[]> {
    return this.http
      .get(`${this.apiUrl}?page=${page}&size=${size}`)
      .pipe(
        map((data: any) => data._embedded.certificateDtoList.map(this.mapper))
      );
  }

  private mapper(data: any): Certificate {
    return {
      id: data.id,
      name: data.name,
      description: data.description,
      shortDescription: data.shortDescription,
      company: data.company,
      price: data.price,
      duration: data.duration,
      createDate: new Date(data.createDate),
      lastUpdate: new Date(data.lastUpdateDate),
      path: data.path,
      tags: data.tags.map((tag: any): Tag => ({ id: tag.id, name: tag.name })),
    };
  }
}
