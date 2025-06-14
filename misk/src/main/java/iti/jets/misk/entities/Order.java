package iti.jets.misk.entities;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


@Entity
//(name = "order", catalog = "misk")
@Table(name = "`order`")
public class Order implements java.io.Serializable {

    private int orderId;
    private User user;
    private Useraddress useraddress;
    private BigDecimal totalAmount;
    private Timestamp orderDate;
    private Set<Orderitems> orderitemses = new HashSet<Orderitems>();

    public Order() {
    }

    public Order(int orderId, User user, Useraddress useraddress) {
        this.orderId = orderId;
        this.user = user;
        this.useraddress = useraddress;
    }

    public Order(int orderId, User user, Useraddress useraddress, BigDecimal totalAmount, Timestamp orderDate,
            Set<Orderitems> orderitemses) {
        this.orderId = orderId;
        this.user = user;
        this.useraddress = useraddress;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.orderitemses = orderitemses;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    public int getOrderId() {
        return this.orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    public Useraddress getUseraddress() {
        return this.useraddress;
    }

    public void setUseraddress(Useraddress useraddress) {
        this.useraddress = useraddress;
    }

    @Column(name = "total_amount", precision = 10, scale = 2)
    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", length = 19)
    public Timestamp getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = CascadeType.REMOVE)
    public Set<Orderitems> getOrderitemses() {
        return this.orderitemses;
    }

    public void setOrderitemses(Set<Orderitems> orderitemses) {
        this.orderitemses = orderitemses;
    }

}
