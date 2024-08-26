package com.mapera.inventory_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mapera.inventory_system.domain.aggregate.inventory_product.InventoryProduct;
import com.mapera.inventory_system.domain.entity.Location;
import com.mapera.inventory_system.domain.entity.Stock;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner runner() {
		return args -> {
			InventoryProduct product = new InventoryProduct(0, "Iphone");
			Location location = new Location(1, "centro", "guadalupe 85");
			product.stockManager.addStock(1, location, 0, 0);
			Stock stock = product.stockManager.getStockInLocation(1);
			log.info("Stock!" + stock.toString());
		};
	}
}
