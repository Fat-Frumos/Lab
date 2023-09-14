import { Component, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class AppComponent {
  // public host = "http://localhost:8080/api";
  public host: string = 'https://gift-store.onrender.com/api';
  public username = localStorage.getItem('user');
}
