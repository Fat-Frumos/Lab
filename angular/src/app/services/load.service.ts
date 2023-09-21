import {Inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, map, Observable, tap, throwError} from 'rxjs';
import {Certificate} from '../model/Certificate';
import {Tag} from "../model/Tag";
import {Category} from "../interfaces/Category";
import {SRC_URL_TOKEN} from "../config";
import {AuthService} from "./auth.service";
import {User} from "../model/User";

@Injectable({
  providedIn: 'root',
})
export class LoadService {

  constructor(
    @Inject(SRC_URL_TOKEN) private srcUrl: string,
    private authService: AuthService,
    private http: HttpClient) {
  }

  loginUser(user: User) {
    return this.http.post(`/login`, user).pipe(
      tap((response: any) => {
        user.access_token = response.access_token;
        user.refresh_token = response.refresh_token;
        user.expired_at = response.expired_at;
        this.authService.setUser(user);
      }),
      catchError(error => {
        console.error('Login failed', error);
        return throwError(() => error);
      })
    );
  }

  getCertificates(page: number, size: number): Observable<Certificate[]> {
    return this.http
    .get<any>(`/certificates?page=${page}&size=${size}`)
    .pipe(map((data: any) => data._embedded.certificateDtoList
      .map(this.mapper))
    );
  }

  getCertificatesByTags(size: number, name: string): Observable<Certificate[]> {
    return this.http
    .get(`/certificates/search?tagNames=${name}&size=${size}`)
    .pipe(map((data: any) => data._embedded.certificateDtoList
      .map(this.mapper))
    );
  }

  getTags(): Observable<string[]> {
    return this.http.get<any>('/tags?size=100')
    .pipe(map((data: any) => data._embedded.tagDtoList
    .map(this.tagMapper)));
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

  private tagMapper = (data: any): Category => {
    return {
      name: data.name,
      tag: data.name,
      url: `${this.srcUrl}/200x150/?${data.name}`,
    };
  }
}
