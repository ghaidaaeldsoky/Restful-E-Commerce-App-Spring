import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { Product, ApiResponse } from '../../models/product';
import { debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { Options } from '@angular-slider/ngx-slider';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-product',
  standalone: false,
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit, AfterViewInit {
  products: Product[] = [];
  currentPage: number = 1;
  totalPages: number = 1;
  pageSize: number = 6;
  filters = {
    gender: 'all',
    minPrice: 0,
    maxPrice: 1000,
    searchQuery: ''
  };
  sliderOptions: Options = {
    floor: 0,
    ceil: 1000,
    step: 1,
    animate: false,
    showTicks: false,
    showTicksValues: false,
    hideLimitLabels: true,
    hidePointerLabels: true
  };
  toastSuccess: boolean = false;
  toastMessage: string = '';
  showLoginToast: boolean = false;
  selectedGender: string = 'all';
  @ViewChild('toastElement') toastElement!: ElementRef;
  @ViewChild('loginToastElement') loginToastElement!: ElementRef;

  private searchSubject = new Subject<string>();

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Initialize slider options first
    this.initializeSliderOptions();
    
    this.route.queryParams.subscribe(params => {
      this.filters.gender = params['gender'] || 'all';
      this.filters.minPrice = +params['minPrice'] || 0;
      this.filters.maxPrice = +params['maxPrice'] || this.sliderOptions.ceil || 1000;
      this.filters.searchQuery = params['search'] || '';
      this.currentPage = +params['page'] || 1;
      this.selectedGender = this.filters.gender;
      console.log('Loaded filters from query params:', this.filters);
      this.loadProducts();
    });

    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => this.applyFilters());
  }

  ngAfterViewInit(): void {
    this.cdr.detectChanges();
  }

  loadProducts(): void {
    console.log('Loading products with filters:', this.filters);
    this.productService.getFilteredProducts({
      ...this.filters,
      page: this.currentPage,
      pageSize: this.pageSize
    }).subscribe({
      next: (response: ApiResponse) => {
        console.log('API response:', response);
        if (response.success) {
  this.products = response.data.content.map(p => ({
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
  this.totalPages = response.data.totalPages || 1;
  this.currentPage = response.data.number + 1;
        } else {
          this.products = [];
          this.totalPages = 1;
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error loading products:', err);
        this.products = [];
        this.totalPages = 1;
        this.cdr.detectChanges();
      }
    });
  }

  applyFilters(): void {
    console.log('applyFilters called with gender:', this.filters.gender);
    if (this.filters.minPrice > this.filters.maxPrice) {
      [this.filters.minPrice, this.filters.maxPrice] = [this.filters.maxPrice, this.filters.minPrice];
    }
    const currentParams = this.route.snapshot.queryParams;
    const newParams = {
      page: this.currentPage,
      gender: this.filters.gender,
      minPrice: this.filters.minPrice,
      maxPrice: this.filters.maxPrice,
      search: this.filters.searchQuery || null
    };
    if (
      currentParams['gender'] !== newParams.gender ||
      currentParams['minPrice'] !== newParams.minPrice.toString() ||
      currentParams['maxPrice'] !== newParams.maxPrice.toString() ||
      currentParams['search'] !== newParams.search ||
      currentParams['page'] !== newParams.page.toString()
    ) {
      this.currentPage = 1;
      this.router.navigate(['/products'], {
        queryParams: newParams,
        queryParamsHandling: 'merge'
      });
    }
    this.cdr.detectChanges();
  }

  resetFilters(): void {
    this.filters = {
      gender: 'all',
      minPrice: 0,
      maxPrice: this.sliderOptions.ceil || 1000,
      searchQuery: ''
    };
    this.selectedGender = 'all';
    this.applyFilters();
  }

  onSearchChange(): void {
    this.searchSubject.next(this.filters.searchQuery);
  }

  onGenderChange(gender: string): void {
    console.log('onGenderChange called with gender:', gender);
    this.filters.gender = gender;
    this.selectedGender = gender;
    this.cdr.detectChanges();
    this.applyFilters();
  }

  addToCart(event: Event, perfume: Product): void {
    event.preventDefault();
    event.stopPropagation();
    
    if (!this.authService.isLoggedIn()) {
      this.showLoginToast = true;
      this.showToast(false, 'You must log in to add items to the cart.');
      return;
    }

    if (perfume.quantity === 0) {
      this.showToast(false, 'This product is out of stock!');
      return;
    }

    const currentCartQuantity = this.cartService.getItemQuantity(perfume.id);
    if (currentCartQuantity >= perfume.quantity) {
      this.showToast(false, `${perfume.name} is already at maximum quantity in your cart!`);
      return;
    }

    this.cartService.addItem(perfume.id, 1);
    const newQuantity = currentCartQuantity + 1;
    
    if (currentCartQuantity > 0) {
      this.showToast(true, `${perfume.name} quantity updated in cart! Total: ${newQuantity}`);
    } else {
      this.showToast(true, `${perfume.name} added to cart!`);
    }
  }

  showToast(success: boolean, message: string): void {
    console.log('showToast called:', { success, message });
    this.toastSuccess = success;
    this.toastMessage = message;

    setTimeout(() => {
      if (this.showLoginToast && this.loginToastElement?.nativeElement) {
        const loginToast = new (window as any).bootstrap.Toast(this.loginToastElement.nativeElement);
        loginToast.show();
        this.showLoginToast = false;
      } else if (this.toastElement?.nativeElement) {
        const toast = new (window as any).bootstrap.Toast(this.toastElement.nativeElement);
        toast.show();
      }
    }, 100);
  }

  getPageRange(): number[] {
    const startPage = Math.max(1, this.currentPage - 2);
    const endPage = Math.min(this.totalPages, this.currentPage + 2);
    const pages = [];
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }

  getPageParams(page: number): any {
    return {
      page,
      gender: this.filters.gender,
      minPrice: this.filters.minPrice,
      maxPrice: this.filters.maxPrice,
      search: this.filters.searchQuery || null
    };
  }

  private initializeSliderOptions(): void {
    this.productService.getFilteredProducts({ 
      gender: 'all', 
      minPrice: 0, 
      maxPrice: 50000, // large number to get all products(future work)
      searchQuery: '', 
      page: 1, 
      pageSize: 1000 
    }).subscribe({
      next: (response: ApiResponse) => {
        if (response.success && response.data.content.length > 0) {
          const prices = response.data.content.map(p => p.price);
          const maxPrice = Math.max(...prices);
          const ceilPrice = Math.ceil(maxPrice / 100) * 100; // round up to nearest 100
          
          this.sliderOptions = {
            floor: 0,
            ceil: ceilPrice,
            step: 1,
            animate: false,
            showTicks: false,
            showTicksValues: false,
            hideLimitLabels: true,
            hidePointerLabels: true
          };
          
          // Set initial max price 
          if (this.filters.maxPrice === 1000) {
            this.filters.maxPrice = ceilPrice;
          }
          
          console.log('Slider initialized - Max price:', ceilPrice);
        } else {
          this.sliderOptions = {
            floor: 0,
            ceil: 1000,
            step: 1,
            animate: false,
            showTicks: false,
            showTicksValues: false,
            hideLimitLabels: true,
            hidePointerLabels: true
          };
        }
      },
      error: (err) => {
        console.error('Error initializing slider:', err);
        this.sliderOptions = {
          floor: 0,
          ceil: 1000,
          step: 1,
          animate: false,
          showTicks: false,
          showTicksValues: false,
          hideLimitLabels: true,
          hidePointerLabels: true
        };
      }
    });
  }

  private updateSliderOptions(): void {
    this.loadProducts();
  }
}