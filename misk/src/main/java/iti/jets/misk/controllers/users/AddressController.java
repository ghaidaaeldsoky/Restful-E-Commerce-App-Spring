package iti.jets.misk.controllers.users;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import iti.jets.misk.dtos.ApiResponse;
import iti.jets.misk.entities.Useraddress;
import iti.jets.misk.services.AddressService;
import jakarta.websocket.server.PathParam;

@Tag(name = "Address")
@RestController
@RequestMapping("/addresses")
public class AddressController {

  @Autowired
  AddressService addressService;

  @Operation(summary = "delete user address")
  @DeleteMapping
  @PreAuthorize("hasAuthority('USER')")
  String deleteUserAddress() {

    Integer id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

    addressService.deleteAddress(id);

    return "address is deleted";
  }

  @Operation(summary = "Add list of addresses for user")
  @PreAuthorize("hasAuthority('USER')")
  @PostMapping
  public ResponseEntity<ApiResponse<String>> addListOfAddresses(@RequestBody List<Useraddress> addresses) {

    try {
      Integer id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

      addressService.addListOfAddresses(id, addresses);

      return ResponseEntity.ok(ApiResponse.success("addresses added Succefully", null));
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      return ResponseEntity.ok(ApiResponse.error("cannot added addresses"));
    }

  }

  @Operation(summary = "Delete user specific address")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('USER')")
  ResponseEntity<ApiResponse<String>> deleteUserAddress(@PathVariable int id) {

    try {
      addressService.deleteAddress(id);
      return ResponseEntity.ok(ApiResponse.success("addresses deleted Succefully", null));
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      return ResponseEntity.ok(ApiResponse.error("cannot delete addresses"));
    }

  }

}
