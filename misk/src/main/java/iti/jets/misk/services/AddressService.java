package iti.jets.misk.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iti.jets.misk.entities.Useraddress;
import iti.jets.misk.repositories.AddressRepo;
import iti.jets.misk.repositories.UserRepository;

@Service
public class AddressService {

    @Autowired
    AddressRepo addressRepo;

    @Autowired
     UserRepository userRepository;

    public void deleteAddress(Integer addressId)
    {
        addressRepo.deleteById(addressId);
    }
    public boolean addListOfAddresses(int id, List<Useraddress> addresses)
    {
        return userRepository.addListOfAddresses(id, addresses);
    }

    

}
