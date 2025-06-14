package iti.jets.misk.repositories;

import iti.jets.misk.entities.Useraddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Useraddress,Integer> {


}
