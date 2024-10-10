package com.mapera.inventory_system.application.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.category.CategoryCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.currency.CurrencyCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.location.LocationCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.movement.MovementCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product.ProductCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.product_supplier.ProductSupplierCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.stock.StockCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.subcategory.SubcategoryCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.supplier.SupplierCrudRepository;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user.UserCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class QuotaApplicationService {

    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    CategoryCrudRepository categoryCrudRepository;

    @Autowired
    SubcategoryCrudRepository subcategoryCrudRepository;

    @Autowired
    ProductCrudRepository productCrudRepository;

    @Autowired
    SupplierCrudRepository supplierCrudRepository;

    @Autowired
    ProductSupplierCrudRepository productSupplierCrudRepository;

    @Autowired
    LocationCrudRepository locationCrudRepository;

    @Autowired
    CurrencyCrudRepository currencyCrudRepository;

    @Autowired
    MovementCrudRepository movementCrudRepository;

    @Autowired
    StockCrudRepository stockCrudRepository;

    private final int PRODUCT_LIMIT = 100;
    private final int MOVEMENT_LIMIT = 2000;
    private final int OTHER_LIMIT = 50;

    @Scheduled(cron = "0 0 0 * * ?") // Midnight execution
    public void scheduledCheckUserQuotas() {
        userCrudRepository.findAll()
                .filter(user -> user.getRoles().contains("USER"))
                .flatMap(this::processUser)
                .subscribe();
    }

    private Mono<Void> processUser(UserEntity user) {
        List<Mono<Boolean>> checks = List.of(
                productCrudRepository.countByUserId(user.getId()).map(count -> count <= PRODUCT_LIMIT),
                movementCrudRepository.countByUserId(user.getId()).map(count -> count <= MOVEMENT_LIMIT),
                categoryCrudRepository.countByUserId(user.getId()).map(count -> count <= OTHER_LIMIT),
                subcategoryCrudRepository.countByUserId(user.getId()).map(count -> count <= OTHER_LIMIT),
                supplierCrudRepository.countByUserId(user.getId()).map(count -> count <= OTHER_LIMIT),
                productSupplierCrudRepository.countByUserId(user.getId()).map(count -> count <= OTHER_LIMIT),
                locationCrudRepository.countByUserId(user.getId()).map(count -> count <= OTHER_LIMIT),
                currencyCrudRepository.countByUserId(user.getId()).map(count -> count <= OTHER_LIMIT),
                stockCrudRepository.countByUserId(user.getId()).map(count -> count <= OTHER_LIMIT));

        return Flux.merge(checks)
                .all(passed -> passed)
                .flatMap(allWithinLimit -> {
                    if (!allWithinLimit) {
                        String roles = user.getRoles();
                        if (roles.contains("USER")) {
                            roles = roles.replace("USER", "")
                                    .replaceAll(",,", ",")
                                    .replaceAll("^,|,$", "");
                            user.setRoles(roles.isEmpty() ? "" : roles);
                            return userCrudRepository.save(user).then();
                        }
                    }
                    return Mono.empty();
                });
    }
}
