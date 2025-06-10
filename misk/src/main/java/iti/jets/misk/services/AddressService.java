package iti.jets.misk.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import iti.jets.misk.entities.Useraddress;
import iti.jets.misk.exceptions.AddressNotFoundException;
import iti.jets.misk.repositories.AddressRepo;
import iti.jets.misk.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AddressService {

    @Autowired
    AddressRepo addressRepo;

    @Autowired
    UserRepository userRepository;

    public void deleteAddress(Integer addressId) {
        Useraddress address = addressRepo.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException("cannot found the address"));

        address.setUser(null);

        addressRepo.save(address);
    }

    public boolean addListOfAddresses(int id, List<Useraddress> addresses) {
        return userRepository.addListOfAddresses(id, addresses);
    }

}
