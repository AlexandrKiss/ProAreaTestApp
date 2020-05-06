package co.proarea.configuration;

import co.proarea.dto.ProductUnitDTO;
import co.proarea.models.*;
import co.proarea.services.ProductService;
import co.proarea.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class AppConfig {
    Product burger = new Product("Burger",1L);
    ProductUnit bigMac = new ProductUnit("Big Mac", 1L, 4.35,burger);
    ProductUnit quarterPounder = new ProductUnit("Quarter Pounder", 2L, 4.40,burger);
    ProductUnit quarterPounderDeluxe = new ProductUnit("Quarter Pounder Deluxe", 3L, 5.0,burger);

    Product chicken = new Product("Chicken",2L);
    ProductUnit buttermilkCrispyChicken = new ProductUnit("Buttermilk Crispy Chicken", 4L, 5.0,chicken);
    ProductUnit artisanGrilledChicken = new ProductUnit("Artisan Grilled Chicken", 5L, 5.0,chicken);

    Role userRole = new Role("ROLE_USER");
    Role adminRole = new Role("ROLE_ADMIN");

    User admin = new User("admin","Admin","Admin", "admin@mail.com","admin");
    User user = new User("user","FirstName","LastName", "user@mail.com","user");

    @Bean
    public CommandLineRunner demo(final ProductService productUnitService, final ProductService productService,
                                  final UserService userService) {
        return new CommandLineRunner() {
            @Override
            public void run(String... strings) throws Exception {
                productService.addProduct(burger);
                productService.addProduct(chicken);
                productUnitService.addProductUnit(new ProductUnitDTO(bigMac));
                productUnitService.addProductUnit(new ProductUnitDTO(quarterPounder));
                productUnitService.addProductUnit(new ProductUnitDTO(quarterPounderDeluxe));
                productUnitService.addProductUnit(new ProductUnitDTO(buttermilkCrispyChicken));
                productUnitService.addProductUnit(new ProductUnitDTO(artisanGrilledChicken));

                userService.addRole(adminRole);
                userService.addRole(userRole);

                userService.register(admin);
                userService.register(user);
            }
        };
    }
}