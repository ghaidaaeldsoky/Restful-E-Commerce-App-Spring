import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

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
  product: Product | null = null;
  originalProduct: Product | null = null;
  isEditMode: boolean = false;
  changedFields: { [key: string]: { oldValue: any; newValue: any } } = {};


  constructor(private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id === 'new') {
      this.initializeNewProduct();
    } else {
      this.productId = Number(id);
      this.isEditMode = true;
      this.loadProduct();
    }

    // Simulate fetch from DB or service
    const all = JSON.parse(localStorage.getItem('products') || '[]');
    this.product = {
      name: `Perfume A`,
      description: 'Luxury perfume',
      price: 150 ,
      quantity: 10,
      brand: 'MISK',
      gender: 'female',
      size: '100ml',
      imageUrl: 'https://via.placeholder.com/60',
    }
  }

  initializeNewProduct(): void {
    this.product = {
      name: '',
      description: '',
      price: 0,
      quantity: 0,
      brand: '',
      gender: '',
      size: '',
      imageUrl: 'https://via.placeholder.com/100',
    };
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
        id: this.productId,
        name: `Perfume ${this.productId}`,
        description: 'Luxury perfume with long-lasting fragrance',
        price: 150,
        quantity: 10,
        brand: 'MISK',
        gender: 'unisex',
        size: '100ml',
        imageUrl: 'https://via.placeholder.com/100',
      };
      this.originalProduct = { ...this.product };
    }
  }

   onImageSelected(event: Event): void {
    const target = event.target as HTMLInputElement;
    const file = target.files?.[0];
    
    if (file) {
      // Validate file type
      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
      if (!allowedTypes.includes(file.type)) {
        alert('Please select a valid image file (JPG, PNG, or GIF)');
        return;
      }

      // Validate file size (max 5MB)
      const maxSize = 5 * 1024 * 1024;
      if (file.size > maxSize) {
        alert('File size must be less than 5MB');
        return;
      }

      // Create a FileReader to convert file to base64
      const reader = new FileReader();
      reader.onload = (e) => {
        if (this.product && e.target?.result) {
          const oldImageUrl = this.product.imageUrl;
          this.product.imageUrl = e.target.result as string;
          
          if (this.isEditMode) {
            this.trackChange('imageUrl', this.product.imageUrl, oldImageUrl);
          }
        }
      };
      reader.readAsDataURL(file);
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
      this.product.id = newId;
      
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

}
