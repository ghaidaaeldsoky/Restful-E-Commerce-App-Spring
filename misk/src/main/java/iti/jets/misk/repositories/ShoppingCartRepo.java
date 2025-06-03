package iti.jets.misk.repositories;

import iti.jets.misk.entities.Shoppingcart;
import iti.jets.misk.entities.ShoppingcartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepo extends JpaRepository<Shoppingcart, ShoppingcartId> {
    List<Shoppingcart> findByIdUserId(Integer userId);

    @Modifying
    @Query("DELETE FROM Shoppingcart sc WHERE sc.id.userId = :userId")
    void clearCartForUser(@Param("userId") Integer userId);

    @Query("SELECT COUNT(sc) FROM Shoppingcart sc WHERE sc.id.userId = :userId")
    Long countByIdUserId(@Param("userId") Integer userId);
}