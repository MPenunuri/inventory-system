package com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.repository.user;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.dto.user.UserData;
import com.mapera.inventory_system.infrastructure.adapter.outbound.persistence.entity.UserEntity;

import reactor.core.publisher.Mono;

public interface UserCrudRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Mono<UserEntity> findUserByEmail(String name);

    @Query("""
                SELECT
                    u.id,
                    u.username,
                    u.email,
                    u.password,
                    (SELECT COUNT(*) FROM categories c WHERE c.user_id = u.id) AS registered_categories,
                    (SELECT COUNT(*) FROM subcategories s WHERE s.user_id = u.id) AS registered_subcategories,
                    (SELECT COUNT(*) FROM products p WHERE p.user_id = u.id) AS registered_products,
                    (SELECT COUNT(*) FROM movements m WHERE m.user_id = u.id) AS registered_movements,
                    (SELECT COUNT(*) FROM locations l WHERE l.user_id = u.id) AS registered_locations,
                    (SELECT COUNT(*) FROM stock_list sl WHERE sl.user_id = u.id) AS registered_stocks,
                    (SELECT COUNT(*) FROM suppliers sp WHERE sp.user_id = u.id) AS registered_suppliers,
                    (SELECT COUNT(*) FROM product_supplier ps WHERE ps.user_id = u.id) AS registered_product_supplier_relations,
                    (SELECT COUNT(*) FROM currencies cur WHERE cur.user_id = u.id) AS registered_currencies
                FROM users u
                WHERE u.id = :userId
            """)
    Mono<UserData> getUserInfo(Long userId);
}
