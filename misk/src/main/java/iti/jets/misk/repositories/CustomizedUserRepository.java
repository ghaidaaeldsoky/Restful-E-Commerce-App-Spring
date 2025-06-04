package iti.jets.misk.repositories;

import java.util.List;
import java.util.Set;

import iti.jets.misk.entities.Useraddress;
import jakarta.persistence.EntityManager;

public interface CustomizedUserRepository {

      public boolean addListOfAddresses(int id, List<Useraddress> address);

}
