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

    private int USER_RECORD_LIMIT = 1000;

    @Scheduled(cron = "0 0 0 * * ?") // Execute on midnight
    public void scheduledCheckUserQuotas() {
        checkUserQuotas(USER_RECORD_LIMIT);
    }

    public void checkUserQuotas(int limit) {
        userCrudRepository.findAll()
                .filter(user -> user.getRoles().contains("USER"))
                .flatMap(user -> processUser(user, limit))
                .subscribe();
    }

    private Mono<Void> processUser(UserEntity user, int limit) {
        List<Mono<Integer>> counts = List.of(
                categoryCrudRepository.countByUserId(user.getId()),
                productCrudRepository.countByUserId(user.getId()),
                subcategoryCrudRepository.countByUserId(user.getId()),
                supplierCrudRepository.countByUserId(user.getId()),
                productSupplierCrudRepository.countByUserId(user.getId()),
                locationCrudRepository.countByUserId(user.getId()),
                currencyCrudRepository.countByUserId(user.getId()),
                movementCrudRepository.countByUserId(user.getId()),
                stockCrudRepository.countByUserId(user.getId()));

        return Flux.merge(counts)
                .reduce(0, Integer::sum)
                .flatMap(totalRecords -> {
                    if (totalRecords > limit) {
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
