import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
import {Certificate} from '../model/Certificate';
import {Tag} from "../model/Tag";
import {Category} from "../interfaces/Category";


@Injectable({
  providedIn: 'root',
})
export class LoadService {

  private apiUrl: string = 'https://gift-store.onrender.com/api';

  constructor(
    private http: HttpClient) {
  }

  getCertificates(page: number, size: number): Observable<Certificate[]> {
    return this.http
    .get(`${this.apiUrl}/certificates?page=${page}&size=${size}`)
    .pipe(map((data: any) => data._embedded.certificateDtoList
      .map(this.mapper))
    );
  }

  getTags(): Observable<string[]> {
    return this.http.get<string[]>(this.apiUrl + '/tags').pipe(
      map((data: any) => data._embedded.tagDtoList.map(this.tagMapper)));
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
      favorite: false,
      checkout: false,
      path: data.path,
      tags: data.tags.map((tag: any): Tag => ({id: tag.id, name: tag.name})),
    };
  }

  private tagMapper(data: any): Category {
    return {
      name: data.name,
      tag: data.name,
      url: `https://source.unsplash.com/featured/200x150/?${data.name}`,
    };
  }
}
