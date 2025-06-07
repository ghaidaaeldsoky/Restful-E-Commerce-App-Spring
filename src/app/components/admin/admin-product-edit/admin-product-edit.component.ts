import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductDto, ProductsService } from '../../../services/admin/products.service';

interface Product {
  id?: number;
  name: string;
  description: string;
  price: number;
  quantity: number;
  brand: string;
  gender: string;
  size: string;
  imageUrl: string;
}

interface FieldChange {
  field: string;
  oldValue: any;
  newValue: any;
}

@Component({
  selector: 'app-admin-product-edit',
  standalone: false,
  templateUrl: './admin-product-edit.component.html',
  styleUrl: './admin-product-edit.component.css'
})
export class AdminProductEditComponent implements OnInit {

  productId!: number;
  product: ProductDto | null = null;
  originalProduct: ProductDto | null = null;
  isEditMode: boolean = false;
  changedFields: { [key: string]: { oldValue: any; newValue: any } } = {};
  photoFile: File | null = null;


  constructor(private route: ActivatedRoute, private router: Router, private productsService:ProductsService) {}

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));

    // Simulate fetch from DB or service
    // this.product = {
    //   name: `Perfume A`,
    //   description: 'Luxury perfume',
    //   price: 150 ,
    //   quantity: 10,
    //   brand: 'MISK',
    //   gender: 'female',
    //   size: '100ml',
    //   imageUrl: 'https://via.placeholder.com/60',
    // }

    this.isEditMode = true;

  this.productsService.getProductById(this.productId).subscribe({
    next: (data) => {
      this.product = data;
      this.originalProduct = { ...data };
    },
    error: (err) => {
      console.error('Error fetching product', err);
      alert('Error fetching product');
    }
  });
  }



  // initializeNewProduct(): void {
  //   this.product = {
  //     name: '',
  //     description: '',
  //     price: 0,
  //     quantity: 0,
  //     brand: '',
  //     gender: '',
  //     size: '',
  //     photo: 'https://via.placeholder.com/100',
  //   };
  // }

  onImageSelected(event: Event): void {
  const target = event.target as HTMLInputElement;
  const file = target.files?.[0];

  if (file) {
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
    const maxSize = 5 * 1024 * 1024;

    if (!allowedTypes.includes(file.type)) {
      alert('Please select a valid image file (JPG, PNG, or GIF)');
      return;
    }

    if (file.size > maxSize) {
      alert('File size must be less than 5MB');
      return;
    } 

    // Set the file
    this.photoFile = file;

    // Preview the image
    const reader = new FileReader();
    reader.onload = (e) => {
      if (this.product && e.target?.result) {
        const oldImageUrl = this.product.photo;
        this.product.photo = e.target.result as string;

        this.trackChange('photo', this.product.photo, oldImageUrl);
      }
    };
    reader.readAsDataURL(file);
  }
}

  loadProduct(): void {
    const products = JSON.parse(localStorage.getItem('products') || '[]');
    const foundProduct = products.find((p: Product) => p.id === this.productId);
    
    if (foundProduct) {
      this.product = { ...foundProduct };
      this.originalProduct = { ...foundProduct };
    } else {
      // Fallback - create a sample product if not found
      this.product = {
        productId: this.productId,
        name: `Perfume ${this.productId}`,
        description: 'Luxury perfume with long-lasting fragrance',
        price: 150,
        quantity: 10,
        brand: 'MISK',
        gender: 'unisex',
        size: '100ml',
        photo: 'https://via.placeholder.com/100',
      };
      this.originalProduct = { ...this.product };
    }
  }


   trackChange(field: string, newValue: any, oldValue?: any): void {
    if (!this.isEditMode || !this.originalProduct) return;

    const originalValue = oldValue !== undefined ? oldValue : (this.originalProduct as any)[field];
    
    if (newValue !== originalValue) {
      this.changedFields[field] = {
        oldValue: originalValue,
        newValue: newValue
      };
    } else {
      // If value is reverted to original, remove from changed fields
      delete this.changedFields[field];
    }
  }

  hasChanges(): boolean {
    return Object.keys(this.changedFields).length > 0;
  }

  getChanges(): FieldChange[] {
    return Object.entries(this.changedFields).map(([field, change]) => ({
      field: this.getFieldDisplayName(field),
      oldValue: change.oldValue,
      newValue: change.newValue
    }));
  }

  private getFieldDisplayName(field: string): string {
    const fieldNames: { [key: string]: string } = {
      'name': 'Product Name',
      'description': 'Description',
      'price': 'Price',
      'quantity': 'Quantity',
      'brand': 'Brand',
      'gender': 'Gender',
      'size': 'Size',
      'imageUrl': 'Product Image'
    };
    return fieldNames[field] || field;
  }

  resetForm(): void {
    if (this.originalProduct && this.isEditMode) {
      this.product = { ...this.originalProduct };
      this.changedFields = {};
    }
  }

  save(): void {
    if (!this.product) return;

    let products = JSON.parse(localStorage.getItem('products') || '[]');
    
    if (this.isEditMode) {
      // Update existing product
      const index = products.findIndex((p: Product) => p.id === this.productId);
      if (index !== -1) {
        products[index] = { ...this.product };
        
        const changesCount = Object.keys(this.changedFields).length;
        const changeMessage = changesCount > 0 
          ? ` (${changesCount} field${changesCount > 1 ? 's' : ''} updated)`
          : '';
        
        localStorage.setItem('products', JSON.stringify(products));
        alert(`Product "${this.product.name}" has been updated successfully${changeMessage}!`);
      }
    } else {
      // Create new product
      const newId = products.length > 0 ? Math.max(...products.map((p: Product) => p.id || 0)) + 1 : 1;
      this.product.productId = newId;
      
      products.push(this.product);
      // localStorage.setItem('products', JSON.stringify(products));
      alert(`Product "${this.product.name}" has been created successfully!`);
    }

    this.router.navigate(['/products']);
  }

  goBack(): void {
    if (this.hasChanges()) {
      const confirmLeave = confirm(
        'You have unsaved changes. Are you sure you want to leave this page?'
      );
      if (!confirmLeave) return;
    }
    
    this.router.navigate(['/products']);
  }

  // save() {
  //   alert('Saved!');
  //   this.router.navigate(['/products']);
  // }

  preparePatchPayload(): any {
  const dto: any = {};

  for (const field in this.changedFields) {
    const value = (this.product as any)[field];
    if (field === 'photo') continue; // handled as File separately
    dto[field] = value;
  }

  return dto;
}

saveProduct(): void {
  if (!this.product || !this.hasChanges()) {
    alert('No changes to save.');
    return;
  }

  const patchData = this.preparePatchPayload();

  this.productsService.patchProduct(this.productId, patchData, this.photoFile ?? undefined)
    .subscribe({
      next: (updatedProduct) => {
        alert(`Product "${updatedProduct.name}" updated successfully.`);
        this.router.navigate(['/products']);
      },
      error: (err) => {
        console.error('Error updating product:', err);
        alert('Failed to update product');
      }
    });
}


}
