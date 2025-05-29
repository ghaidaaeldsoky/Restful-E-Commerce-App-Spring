package iti.jets.misk.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


@ToString
@Entity
@Table(name = "user", catalog = "misk2", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer userId;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;
    
    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday", length = 10)
    private Date birthday;
    
    @Column(name = "job", length = 50)
    private String job;
    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit;
    @Column(name = "interests", length = 65535)
    private String interests;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
   private Set<Order> orders = new HashSet<Order>(0);

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Useraddress> useraddresses = new HashSet<Useraddress>(0);
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
   private Set<Shoppingcart> shoppingcarts = new HashSet<Shoppingcart>(0);

    public User() {
    }

    public User(int userId, String name, String phoneNumber, String email, String password, boolean isAdmin) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public User(int userId, String name, String phoneNumber, String email, String password, Date birthday, String job,
            BigDecimal creditLimit, String interests, boolean isAdmin, Set<Order> orders,
            Set<Useraddress> useraddresses, Set<Shoppingcart> shoppingcarts) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.job = job;
        this.creditLimit = creditLimit;
        this.interests = interests;
        this.isAdmin = isAdmin;
       // this.orders = orders;
        this.useraddresses = useraddresses;
       // this.shoppingcarts = shoppingcarts;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

   
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

 
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(String job) {
        this.job = job;
    }


    public BigDecimal getCreditLimit() {
        return this.creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }


    public String getInterests() {
        return this.interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

  
    public boolean isIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

   
    public Set<Useraddress> getUseraddresses() {
        return this.useraddresses;
    }

    public void setUseraddresses(Set<Useraddress> useraddresses) {
        this.useraddresses = useraddresses;
    }

   
    public Set<Shoppingcart> getShoppingcarts() {
        return this.shoppingcarts;
    }

    public void setShoppingcarts(Set<Shoppingcart> shoppingcarts) {
        this.shoppingcarts = shoppingcarts;
    }

}

