package com.minicoy.demo;

import com.minicoy.demo.model.Category;
import com.minicoy.demo.model.Order;
import com.minicoy.demo.model.OrderItem;
import com.minicoy.demo.model.Product;
import com.minicoy.demo.repository.CategoryRepository;
import com.minicoy.demo.repository.OrderItemRepository;
import com.minicoy.demo.repository.OrderRepository;
import com.minicoy.demo.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			ProductRepository productRepository,
			CategoryRepository categoryRepository,
			OrderRepository orderRepository,
			OrderItemRepository orderItemRepository) {
		return args -> {

			// 1. Create and Save a Category first (The Parent)
			Category beverages = new Category(null, "Hot Beverages", null);
			categoryRepository.save(beverages);

			// Create another category
			Category snacks = new Category(null, "Snacks", null);
			categoryRepository.save(snacks);

			// 2. Create and Save a Product (The Child - linked to the category)
			Product coffee = new Product(null, "Cappuccino", 5.50, "Freshly brewed with milk", beverages);
			productRepository.save(coffee);
			// Add more products
			productRepository.save(new Product(null, "Black Coffee", 5.00, "just coffee", beverages));
			productRepository.save(new Product(null, "Espresso", 6.50, "Get outta bed", beverages));
			productRepository.save(new Product(null, "Croissant", 3.50, "Buttery and flaky", snacks));
			productRepository.save(new Product(null, "Muffin", 4.00, "Blueberry blast", snacks));

			// 3. Create and Save an Order (The Transaction)
			Order myOrder = new Order(null, "Divya", 0.0, new Date());
			orderRepository.save(myOrder); // Save the order header first
			OrderItem item1 = new OrderItem(null, coffee, 2, 5.50, myOrder); // Pass 'myOrder' here!
			orderItemRepository.save(item1);

			System.out.println("---- Cafe Data Seeded into MySQL Successfully! ----");
		};
	}
}