import { Injectable } from '@angular/core';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private products: Product[] = [
    {
      id: 1,
      name: 'Oud Rose',
      description: 'Rich rose scent with hints of oud.',
      price: 950,
      quantity: 10,
      brand: 'MISK',
      gender: 'Unisex',
      size: '100ml',
      imageUrl: 'img/gallery/1.jpg'
    },
    {
      id: 2,
      name: 'Amber Glow',
      description: 'Warm and glowing amber perfume.',
      price: 850,
      quantity: 0,
      brand: 'MISK',
      gender: 'Women',
      size: '50ml',
      imageUrl: 'img/gallery/2.jpg'
    },
    {
      id: 3,
      name: 'Citrus Woods',
      description: 'Fresh citrus with woody undertones.',
      price: 780,
      quantity: 6,
      brand: 'MISK',
      gender: 'Men',
      size: '100ml',
      imageUrl: 'img/gallery/3.jpg'
    }
  ];

  getTrendingProducts(): Product[] {
    return this.products;
  }
}
