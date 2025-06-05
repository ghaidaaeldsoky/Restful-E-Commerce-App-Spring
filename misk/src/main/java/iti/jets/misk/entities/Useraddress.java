package iti.jets.misk.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


@ToString(exclude = {"user", "orders"})
@Entity
@Table(name="useraddress")
public class Useraddress  implements java.io.Serializable {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id", unique=true, nullable=false)
     private Integer addressId;
     @Column(name="state", nullable=false, length=14)
     private String state;
     @Column(name="city", length=255)
     private String city;
     
    @Column(name="street", nullable=false, length=65535)
     private String street;
     @Column(name="department_number")
     private Long departmentNumber;
     
@OneToMany(fetch=FetchType.LAZY, mappedBy="useraddress")
     private Set<Order> orders = new HashSet<Order>(0);

     @ManyToOne(fetch=FetchType.LAZY)
     @JoinColumn(name="user_id")
      private User user;

    public Useraddress() {
    }

	
    public Useraddress(int addressId, String state, String city, String street) {
        this.addressId = addressId;
        this.state = state;
        this.city = city;
        this.street = street;
    }
    public Useraddress(int addressId, User user, String state, String city, String street, Long departmentNumber, Set<Order> orders) {
       this.addressId = addressId;
       this.user = user;
       this.state = state;
       this.city = city;
       this.street = street;
       this.departmentNumber = departmentNumber;
       this.orders = orders;
    }
   

    public int getAddressId() {
        return this.addressId;
    }
    
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }


    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    

    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }

    
  
    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    
    public String getStreet() {
        return this.street;
    }
    
    public void setStreet(String street) {
        this.street = street;
    }

    
    
    public Long getDepartmentNumber() {
        return this.departmentNumber;
    }
    
    public void setDepartmentNumber(Long departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }
    
    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return
                 state + '\'' +
                 city + '\'' +
                street + '\'' +
                + departmentNumber;

    }
}


