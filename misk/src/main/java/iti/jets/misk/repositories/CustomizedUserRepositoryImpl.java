package iti.jets.misk.repositories;

import java.util.List;
import java.util.Set;

import iti.jets.misk.entities.User;
import iti.jets.misk.entities.Useraddress;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

    @PersistenceContext
	private EntityManager em;


    @Override
    @Transactional
    public boolean addListOfAddresses(int id, List<Useraddress> addresses) {

      User user =  em.find(User.class, id);
 for (Useraddress address : addresses) {
            user.getUseraddresses().add(address);

            address.setUser(user);
        }

        em.merge(user);

      return true;
       
    }



}
