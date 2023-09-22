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
import {Certificate} from '../model/Certificate';
import {Tag} from "../model/Tag";
import {Category} from "../interfaces/Category";
import {BASE_URL_TOKEN, SRC_URL_TOKEN} from "../config";
import {AuthService} from "./auth.service";
import {User} from "../model/User";
import {ModalService} from "../components/modal/modal.service";
import {IMessage} from "../interfaces/IMessage";
import {FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root',
})
export class LoadService {

  constructor(
    @Inject(SRC_URL_TOKEN) private srcUrl: string,
    @Inject(BASE_URL_TOKEN) private baseUrl: string,
    private readonly modalService: ModalService,
    private readonly authService: AuthService,
    private readonly http: HttpClient) {
  }

  public sendOrders(user: User): IMessage {
    let message: IMessage = {name: '', href: ''};
    const ids = user.certificates.map((certificate) => certificate.id);
    const url = `${this.baseUrl}/orders/${user.username}?certificateIds=${ids}`;
    const requestBody = {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${user.access_token}`,
      },
      body: JSON.stringify({
        certificateIds: ids.join(","),
      }),
    };
    fetch(url, requestBody)
    .then((response) => {
      if (!response.ok) {
        message = response.status === 401
          ? {name: `Unauthorized user`, href: '/login'}
          : {name: `Failed to send orders`, href: '/checkout'};
      }
      return response.json();
    })
    .then((data) => {
        message = {name: data.toString(), href: '/'}
      }
    )
    .catch((error) => {
      message = {name: error.toString(), href: '/login'}
    });
    return message;
  }


  async saveForm(form: FormGroup): Promise<IMessage> {
    console.log(form);
    const certificateData = this.extractFormData(form);
    console.log(certificateData);

    const accessToken = this.authService.getUser().access_token;
    const headers = new HttpHeaders({
      Authorization: `Bearer ${accessToken}`,
      'Content-Type': 'application/json',
    });

    const requestBody = JSON.stringify(certificateData);

    const options: RequestInit = {
      method: 'POST',
      headers: headers.keys().reduce((result, key) => {
        result[key] = headers.get(key)!;
        return result;
      }, {} as Record<string, string>),
      body: requestBody,
    };

    let message: IMessage;
    try {
      const response = await fetch(`${this.baseUrl}/certificates`, options);
      if (response.ok) {
        const jsonData = await response.json();
        localStorage.setItem('certificate', JSON.stringify(jsonData));
        message = {name: 'Certificate created successfully', href: '/details'};
      } else {
        message = {name: 'Failed to create certificate.', href: '/'};
      }
    } catch (error) {
      message = {name: 'Error:' + error, href: '/'};
    }
    return message;
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

  loginUser(user: User): Observable<any> {
    return this.http.post(`/login`, user).pipe(
      tap((response: any): void => {
        user.access_token = response.access_token;
        user.refresh_token = response.refresh_token;
        user.expired_at = response.expired_at;
        this.authService.setUser(user);
      }),
      catchError(error => {
        console.error('Login failed', error);
        return throwError(() =>
          this.modalService.show(error.toString()));
      })
    );
  }

  signup(formData: any): Observable<any> {
    return from(import('../components/item/item.component')).pipe(
      mergeMap((module) => {
        const ItemComponent = module.ItemComponent;
        return this.http.post(`/signup`, formData).pipe(
          tap((response: any) =>
            this.modalService.open({
              component: ItemComponent,
              context: {
                message: {name: `The user ${response.username} ${formData.firstName} has been registered`}, //TODO
              }
            })
          ),
          catchError(error => {
            console.error('Login failed', error);
            return throwError(() => error.toString());
          })
        );
      })
    );
  }

  showMessage(message: string) {
    this.modalService.show(message);
  }

  redirect(message: IMessage) {
    this.showMessage(message.name)
    this.authService.redirect(message.href);
  }

  back() {
    this.authService.goBack();
  }

  extractFormData(form: FormGroup): any {
    const expirationDate = form.get('expired')?.value;
    const currentDate = new Date();
    const selectedDate = new Date(expirationDate);
    let durationInDays: number = 31;
    if (!isNaN(selectedDate.getTime())) {
      durationInDays = Math.floor((currentDate.getTime() - selectedDate.getTime()) / (24 * 60 * 60 * 1000)
      );
      console.log('Duration in days:', durationInDays);
    } else {
      console.error('Invalid expiration date:', expirationDate);
    }

    const imageFile = form.get('file')?.value;
    let path = '';

    if (imageFile) {
      path = `${this.baseUrl}/upload/${imageFile.name}`;
    }

    // const selectedTags = formData.tags
    // .filter((tag: { selected: boolean; value: string }) => tag.selected)
    // .map((checkbox: { selected: boolean; value: string }) => ({name: checkbox.value}));


    return {
      name: form.get('name')?.value,
      description: form.get('description')?.value,
      company: form.get('company')?.value,
      price: parseFloat(form.get('price')?.value),
      shortDescription: form.get('shortDescription')?.value,
      duration: durationInDays,
      path: path,
      tags: [],
    };
  }
}
