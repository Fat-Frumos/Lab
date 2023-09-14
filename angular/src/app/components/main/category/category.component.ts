import { Component, ViewEncapsulation } from '@angular/core';
import { ICategory } from '../../../interfaces/ICategory';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CategoryComponent {
  categories: ICategory[] = [
    {
      name: 'Travel',
      tag: 'Travel',
      url: 'https://source.unsplash.com/featured/200x150/?Travel',
    },
    {
      name: 'Celebration',
      tag: 'Celebration',
      url: 'https://source.unsplash.com/featured/200x150/?Celebration',
    },
    {
      name: 'Cosmetics',
      tag: 'Cosmetics',
      url: 'https://source.unsplash.com/featured/200x150/?Cosmetics',
    },
    {
      name: 'Holiday',
      tag: 'Holiday',
      url: 'https://source.unsplash.com/featured/200x150/?Holiday',
    },
    {
      name: 'Makeup',
      tag: 'Makeup',
      url: 'https://source.unsplash.com/featured/200x150/?Makeup',
    },
  ];
}
