import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Product, ProductDto, ApiResponse } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
 private mockProducts: ProductDto[] = [
  {
    productId: 1,
    name: 'Oud Rose',
    description: 'Rich rose scent with hints of oud.',
    price: 950,
    quantity: 10,
    brand: 'MISK',
    gender: 'UNISEX',
    size: '100ml',
    photo: 'img/gallery/1.jpg'
  },
  {
    productId: 2,
    name: 'Amber Glow',
    description: 'Warm and glowing amber perfume.',
    price: 850,
    quantity: 0,
    brand: 'MISK',
    gender: 'FEMALE',
    size: '50ml',
    photo: 'img/gallery/2.jpg'
  },
  {
    productId: 3,
    name: 'Citrus Woods',
    description: 'Fresh citrus with woody undertones.',
    price: 780,
    quantity: 6,
    brand: 'MISK',
    gender: 'MALE',
    size: '100ml',
    photo: 'img/gallery/3.jpg'
  },
  {
    productId: 4,
    name: 'YSL Black Opium',
    description: 'Glamorous & addictive scent for women.',
    price: 1199,
    quantity: 25,
    brand: 'Yves Saint Laurent',
    gender: 'FEMALE',
    size: '90ml',
    photo: 'img/gallery/4.jpg'
  },
  {
    productId: 5,
    name: 'Dior Sauvage',
    description: 'Fresh and fierce fragrance for men.',
    price: 1350,
    quantity: 20,
    brand: 'Dior',
    gender: 'MALE',
    size: '100ml',
    photo: 'img/gallery/5.jpg'
  },
  {
    productId: 6,
    name: 'Creed Aventus',
    description: 'Bold scent for confident individuals.',
    price: 2200,
    quantity: 15,
    brand: 'Creed',
    gender: 'UNISEX',
    size: '75ml',
    photo: 'img/gallery/6.jpg'
  },
  {
    productId: 7,
    name: 'Gucci Bloom',
    description: 'Elegant white floral fragrance.',
    price: 950,
    quantity: 18,
    brand: 'Gucci',
    gender: 'FEMALE',
    size: '100ml',
    photo: 'img/gallery/1.jpg'
  },
  {
    productId: 8,
    name: 'Versace Eros',
    description: 'Vibrant masculine fragrance.',
    price: 890,
    quantity: 30,
    brand: 'Versace',
    gender: 'MALE',
    size: '100ml',
    photo: 'img/gallery/2.jpg'
  },
  {
    productId: 9,
    name: 'Tom Ford Oud Wood',
    description: 'Luxurious smoky oud scent.',
    price: 1600,
    quantity: 12,
    brand: 'Tom Ford',
    gender: 'UNISEX',
    size: '50ml',
    photo: 'img/gallery/3.jpg'
  },
  {
    productId: 10,
    name: 'Jo Malone Peony & Blush Suede',
    description: 'Soft floral perfume for daily wear.',
    price: 1100,
    quantity: 22,
    brand: 'Jo Malone',
    gender: 'FEMALE',
    size: '100ml',
    photo: 'img/gallery/4.jpg'
  }
];


 getFilteredProducts(filters: { gender: string, minPrice: number, maxPrice: number, searchQuery: string, page: number, pageSize: number }): Observable<ApiResponse> {
    console.log('Filtering with:', filters);
    const filtered = this.mockProducts.filter(p => {
      const matchesGender = filters.gender === 'all' || p.gender.toLowerCase() === filters.gender.toLowerCase();
      const matchesPrice = p.price >= filters.minPrice && p.price <= filters.maxPrice;
      const matchesSearch = !filters.searchQuery || 
        p.name.toLowerCase().includes(filters.searchQuery.toLowerCase()) ||
        p.brand.toLowerCase().includes(filters.searchQuery.toLowerCase());
      console.log(`Product: ${p.name}, Gender match: ${matchesGender}, Price match: ${matchesPrice}, Search match: ${matchesSearch}`);
      return matchesGender && matchesPrice && matchesSearch;
    });

    console.log('Filtered products:', filtered);

    const start = (filters.page - 1) * filters.pageSize;
    const paginated = filtered.slice(start, start + filters.pageSize);
    const totalPages = Math.ceil(filtered.length / filters.pageSize);

    const response: ApiResponse = {
      success: true,
      message: 'Products retrieved successfully',
      data: {
        content: paginated,
        pageable: {
          pageNumber: filters.page - 1,
          pageSize: filters.pageSize
        },
        totalPages,
        totalElements: filtered.length,
        number: filters.page - 1,
        size: filters.pageSize,
        first: filters.page === 1,
        last: filters.page === totalPages,
        empty: paginated.length === 0
      }
    };

    return of(response);
  }

  getProductById(id: number): Product | undefined {
    const product = this.mockProducts.find(p => p.productId === id);
    if (!product) return undefined;
    return {
      id: product.productId,
      name: product.name,
      description: product.description,
      price: product.price,
      quantity: product.quantity,
      brand: product.brand,
      size: product.size,
      gender: product.gender.toLowerCase(),
      imageUrl: product.photo
    };
  }

  getTrendingProducts(): Product[] {
    return this.mockProducts.slice(0, 6).map(p => ({
      id: p.productId,
      name: p.name,
      description: p.description,
      price: p.price,
      quantity: p.quantity,
      brand: p.brand,
      size: p.size,
      gender: p.gender.toLowerCase(),
      imageUrl: p.photo
    }));
  }
}
