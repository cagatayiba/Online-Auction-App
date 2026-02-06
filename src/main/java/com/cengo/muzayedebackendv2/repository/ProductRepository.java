package com.cengo.muzayedebackendv2.repository;

import com.cengo.muzayedebackendv2.domain.entity.Product;
import com.cengo.muzayedebackendv2.domain.entity.enums.ProductState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByArtistId(UUID artistId);

    List<Product> findAllByState(ProductState productState);

    List<Product> findAllByRequesterId(UUID userId);

    List<Product> findAllByBuyerId(UUID userId);
}
