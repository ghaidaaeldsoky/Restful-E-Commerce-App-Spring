package iti.jets.misk;

import iti.jets.misk.dtos.OrderDto;
import iti.jets.misk.entities.Order;
import iti.jets.misk.services.OrderService;
import iti.jets.misk.utils.ValidationResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootApplication
public class MiskApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext run = SpringApplication.run(MiskApplication.class, args);
		OrderService bean = run.getBean(OrderService.class);
		ValidationResult validationResult = bean.validateOrder(1, 1);
		System.out.println(validationResult.isValid());
		System.out.println(validationResult.getMessage());


//
//		ResponseEntity response = bean.placeOrder(1, 1);
//
//		// Print the response
//		System.out.println(response.getStatusCode());
//		System.out.println(response.getBody());


//
//		String response = bean.placeOrder(1);
//		System.out.println(response);
////
//		// Print the response
//		System.out.println(response.getStatusCode());
//		System.out.println(response.getBody());


//		var allOrders = bean.getAllOrders(2, 2);
//		for (OrderDto allOrder : allOrders) {
//			allOrder.toStrings();
//		}


	}

}
