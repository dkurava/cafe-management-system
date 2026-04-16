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
// only seed if empty!
			if (categoryRepository.count() > 0) {
				System.out.println("---- Data already exists! Skipping seed ----");
				return;
			}

			// ── CATEGORIES ──
			Category hotBeverages = new Category(null, "Hot Beverages", null);
			Category coldBeverages = new Category(null, "Cold Beverages", null);
			Category snacks = new Category(null, "Snacks", null);
			Category desserts = new Category(null, "Desserts", null);

			categoryRepository.save(hotBeverages);
			categoryRepository.save(coldBeverages);
			categoryRepository.save(snacks);
			categoryRepository.save(desserts);

			// ── HOT BEVERAGES ──
			productRepository.save(new Product(null,
					"Cappuccino", 5.50,
					"Espresso with steamed milk foam",
					hotBeverages));
			productRepository.save(new Product(null,
					"Latte", 5.00,
					"Espresso with lots of steamed milk",
					hotBeverages));
			productRepository.save(new Product(null,
					"Espresso", 3.50,
					"Strong concentrated coffee shot",
					hotBeverages));
			productRepository.save(new Product(null,
					"Americano", 4.00,
					"Espresso diluted with hot water",
					hotBeverages));
			productRepository.save(new Product(null,
					"Hot Chocolate", 4.50,
					"Rich creamy chocolate drink",
					hotBeverages));
			productRepository.save(new Product(null,
					"Chai Latte", 4.50,
					"Spiced tea with steamed milk",
					hotBeverages));

			// ── COLD BEVERAGES ──
			productRepository.save(new Product(null,
					"Iced Latte", 5.50,
					"Chilled espresso with cold milk",
					coldBeverages));
			productRepository.save(new Product(null,
					"Cold Brew", 5.00,
					"Slow steeped cold coffee",
					coldBeverages));
			productRepository.save(new Product(null,
					"Iced Matcha", 5.50,
					"Japanese green tea over ice",
					coldBeverages));
			productRepository.save(new Product(null,
					"Frappuccino", 6.00,
					"Blended iced coffee drink",
					coldBeverages));
			productRepository.save(new Product(null,
					"Lemonade", 3.50,
					"Fresh squeezed lemonade",
					coldBeverages));

			// ── SNACKS ──
			productRepository.save(new Product(null,
					"Croissant", 3.50,
					"Buttery flaky pastry",
					snacks));
			productRepository.save(new Product(null,
					"Blueberry Muffin", 4.00,
					"Freshly baked blueberry muffin",
					snacks));
			productRepository.save(new Product(null,
					"Banana Bread", 4.50,
					"Moist homemade banana bread",
					snacks));
			productRepository.save(new Product(null,
					"Avocado Toast", 7.00,
					"Sourdough with avocado spread",
					snacks));
			productRepository.save(new Product(null,
					"Granola Bar", 3.00,
					"Oats honey and nut bar",
					snacks));

			// ── DESSERTS ──
			productRepository.save(new Product(null,
					"Chocolate Cake", 6.00,
					"Rich dark chocolate layer cake",
					desserts));
			productRepository.save(new Product(null,
					"Cheesecake", 6.50,
					"New York style baked cheesecake",
					desserts));
			productRepository.save(new Product(null,
					"Tiramisu", 6.00,
					"Italian coffee dessert",
					desserts));
			productRepository.save(new Product(null,
					"Macaron", 2.50,
					"French almond meringue cookie",
					desserts));
			productRepository.save(new Product(null,
					"Brownie", 4.00,
					"Fudgy chocolate walnut brownie",
					desserts));

			System.out.println("---- Full Coffee Shop Menu Seeded! ----");
		};
	}
}