package iti.jets.misk;

import iti.jets.misk.dtos.OrderDto;
import iti.jets.misk.entities.Order;
//import iti.jets.misk.services.EmailService;
import iti.jets.misk.repositories.OrderRepo;
import iti.jets.misk.services.OrderService;
import iti.jets.misk.utils.ValidationResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@EnableAsync
@EnableCaching
@SpringBootApplication
public class MiskApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext run = SpringApplication.run(MiskApplication.class, args);
	//	System.out.println(run.getBean(OrderRepo.class).getClass());
//		//ValidationResult validationResult = bean.validateOrder(1, 1);
//		//System.out.println(validationResult.isValid());
//		//System.out.println(validationResult.getMessage());
//
//
//
//
////		// Print the response
////		System.out.println(response.getStatusCode());
////		System.out.println(response.getBody());
//
//
////
////		String response = bean.placeOrder(1);
////		System.out.println(response);
//////
////		// Print the response
////		System.out.println(response.getStatusCode());
////		System.out.println(response.getBody());
//
//
//		var allOrders = bean.getAllOrders(2, 2);
//		System.out.println("************************");
//		var s = bean.getAllOrders(2, 2);
////		for (OrderDto allOrder : allOrders) {
////			allOrder.toStrings();
////		}
//		System.out.println("************************");
//
//		String response = bean.placeOrder(1);
//
//		var allOrderss = bean.getAllOrders(2, 2);
//
////
//		EmailService bean = run.getBean(EmailService.class);
//		String message = "This is a test email from Misk Application" +
//				"كـــــــل خيـــــر وانتــــم بعـــام ";
//		bean.sendSimpleEmail("ghaidaaeldsoky@gmail.com", "Test Email", message);


	}

}
