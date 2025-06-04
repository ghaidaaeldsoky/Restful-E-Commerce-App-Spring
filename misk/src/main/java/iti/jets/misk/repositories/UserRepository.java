package iti.jets.misk.repositories;

import iti.jets.misk.entities.Order;
import iti.jets.misk.entities.User;
import iti.jets.misk.entities.Useraddress;
import jakarta.persistence.EntityManager;

import java.util.Optional;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> ,CustomizedUserRepository{
    List<User> findAll(); // It should not return DTO

    Optional<User> findById(int id);

     Optional<User> findByEmail(String email);

     User save(User user);


     @Query("select u.creditLimit from User u where u.id=:id")
  BigDecimal getUserCreditCardLimit(@Param("id")int id);

   @Modifying // tell spring it is not a select statement
@Transactional  //atabase transaction boundary. It ensures that all the operations inside the method either complete successfully or roll back if any error
  @Query("update User u  set u.creditLimit=:newLimit  where u.id=:id")
   void updateCreditLimit(@Param("id")int userId,@Param("newLimit") BigDecimal newLimit);

}
