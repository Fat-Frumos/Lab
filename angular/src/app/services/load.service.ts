import {Inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {
  catchError,
  from,
  map,
  mergeMap,
  Observable,
  tap,
  throwError
} from 'rxjs';
import {ICertificate} from '../model/entity/ICertificate';
import {ITag} from "../model/entity/ITag";
import {ICategory} from "../interfaces/ICategory";
import {BASE_URL_TOKEN, SRC_URL_TOKEN} from "../config";
import {AuthService} from "./auth.service";
import {IUser} from "../model/entity/IUser";
import {IMessage} from "../interfaces/IMessage";
import {FormGroup} from "@angular/forms";
import {SpinnerService} from "../components/spinner/spinner.service";
import {LoginState} from "../model/enum/LoginState";
import {ModalService} from "../components/modal/modal.service";
import {NavigateService} from "./navigate.service";
import {StatusCode} from "../model/enum/HttpStatusCode";

@Injectable({
  providedIn: 'root',
})
export class LoadService {

  constructor(
    @Inject(SRC_URL_TOKEN) private srcUrl: string,
    @Inject(BASE_URL_TOKEN) private baseUrl: string,
    private readonly navigator: NavigateService,
    private readonly modalService: ModalService,
    private readonly authService: AuthService,
    private readonly http: HttpClient) {
  }

  showByText(code: number, text: string) {
    const message: IMessage = StatusCode.getMessageForStatus(code, text);
    this.showMessage(message);
  }

  showByStatus(code: number) {
    const message: IMessage = StatusCode.getMessageForStatus(code, '');
    this.showMessage(message);
  }

  showMessage(message: IMessage) {
    this.modalService.showMessage(message);
    if (message.href !== '') {
      this.navigator.redirect(message.href)
    }
  }

  back(): void {
    this.navigator.back();
  }

  loginState(state: LoginState) {
    this.authService.loginState.next(state);
    console.log(this.authService.loginState.value);
  }

  public sendOrders(user: IUser, callback: (statusCode: number) => void): void {
    const ids = user.certificates.map((certificate) => certificate.id);
    const url = `${this.baseUrl}/orders/${user.username}?certificateIds=${ids}`;
    const requestBody = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${user.access_token}`,
      },
      body: JSON.stringify({certificateIds: ids.join(","),}),
    };
    fetch(url, requestBody) //TODO http
    .then((response) => {
      callback(response.status);
    })
    .catch((error) => {
      callback(error.status);
    });
  }

  async saveForm(form: FormGroup): Promise<number> {
    const certificateData = this.extractFormData(form);
    console.log(certificateData);

    const accessToken = this.authService.getUser().access_token;
    const headers = new HttpHeaders({
      Authorization: `Bearer ${accessToken}`,
      'Content-Type': 'application/json',
    });

    const options: RequestInit = {
      method: 'POST',
      headers: headers.keys().reduce((record: Record<string, string>, key: string) => {
        record[key] = headers.get(key)!;
        return record;
      }, {} as Record<string, string>),
      body: JSON.stringify(certificateData),
    };

    let statusCode: number;
    try {
      const response = await fetch(`${this.baseUrl}/certificates`, options);
      if (response.ok) {
        const jsonData = await response.json();
        localStorage.setItem('certificate', JSON.stringify(jsonData));
      }
      statusCode = response.status;
    } catch (error) {
      console.error(error)
      statusCode = 500;
    }
    return statusCode;
  }


  saveImage(form: any) {
    const fileControl = form.get('file');
    if (fileControl) {
      const imageData = new FormData();
      imageData.append('file', fileControl);
      this.http
      .post(`${this.baseUrl}/upload`, imageData)
      .pipe(
        catchError((error) => {
          return throwError(() => error.toString());
        })
      )
      .subscribe();
    }
  }

  getCertificates(page: number, size: number): Observable<ICertificate[]> {
    SpinnerService.toggle();
    return this.http
    .get<any>(`/certificates?page=${page}&size=${size}`)
    .pipe(map((data: any) => data._embedded.certificateDtoList
      .map(this.mapper))
    );
  }

  getCertificatesByTags(size: number, name: string): Observable<ICertificate[]> {
    return this.http
    .get(`/certificates/search?tagNames=${name}&size=${size}`)
    .pipe(map((data: any) => data._embedded.certificateDtoList
      .map(this.mapper))
    );
  }

  getTags(): Observable<string[]> {
    return this.http.get<any>('/tags?size=25')
    .pipe(map((data: any) => data._embedded.tagDtoList
    .map(this.tagMapper)));
  }

  loginUser(user: IUser): Observable<any> {
    return this.http.post(`/login`, user).pipe(
      tap((response: any): void => {
        user.access_token = response.access_token;
        user.refresh_token = response.refresh_token;
        user.expired_at = response.expired_at;
        user.certificates = [];
        user.state = LoginState.LOGGED_IN;
        this.authService.setUser(user);
      }),
      catchError(error => {
        return throwError(() => {
          this.showByStatus(error.status);
        });
      })
    );
  }

  signup(formData: any): Observable<any> {
    console.log(formData.toString())
    return from(import('../components/item/item.component')).pipe(
      mergeMap(() => {
        return this.http.post(`/signup`, formData).pipe(
          tap((response: any) =>
            this.showByText(20101, `${response.username} ${formData.firstName}`)
          )
        );
      })
    );
  }

  private extractFormData(form: FormGroup): any {
    const expirationDate = form.get('expired')?.value;
    const currentDate = new Date();
    const selectedDate = new Date(expirationDate);
    let durationInDays: number = 31; //TODO less more default
    if (!isNaN(selectedDate.getTime())) {
      durationInDays = Math.floor((currentDate.getTime() - selectedDate.getTime()) / (24 * 60 * 60 * 1000));
      console.log('Duration in days:', durationInDays);
    } else {
      this.showByStatus(405);
      return;
    }

    const imageFile = form.get('file')?.value;
    let path: string = imageFile ? `${this.baseUrl}/upload/${imageFile.name}` : '';
    //TODO selectedTags
    const selectedTags = form.get('tags')?.value
    .filter((tag: { selected: boolean; value: string }) => tag.selected)
    .map((checkbox: {
      selected: boolean;
      value: string
    }) => ({name: checkbox.value}));

    return {
      name: form.get('name')?.value,
      description: form.get('description')?.value,
      company: form.get('company')?.value,
      price: parseFloat(form.get('price')?.value),
      shortDescription: form.get('shortDescription')?.value,
      duration: durationInDays,
      path: path,
      tags: selectedTags,
    };
  }

  private mapper(data: any): ICertificate {
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
      tags: data.tags.map((tag: any): ITag => ({id: tag.id, name: tag.name})),
    };
  }

  private tagMapper = (data: any): ICategory => {
    return {
      name: data.name,
      tag: data.name,
      url: `${this.srcUrl}/200x150/?${data.name}`,
    };
  }
}
