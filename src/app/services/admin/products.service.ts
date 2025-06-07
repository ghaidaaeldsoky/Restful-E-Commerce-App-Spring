import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ProductDto {
  productId: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  brand: string;
  gender: string;
  size: string;
  photo: string; // this is your imageUrl
}

export interface ProductResponse {
  content: ProductDto[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number; // current page
}


@Injectable({
  providedIn: 'root'
})
export class ProductsService {

  private apiUrl = 'http://localhost:8085/admin/products';

  constructor(private http: HttpClient) { }

  getProducts(page: number, size: number, searchTerm: string): Observable<ProductResponse> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('isDeleted',false)


    if (searchTerm) {
      params = params.set('name', searchTerm);
    }

    return this.http.get<ProductResponse>(this.apiUrl, { params });
  }
}
