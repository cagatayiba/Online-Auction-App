package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.entity.Product;
import com.cengo.muzayedebackendv2.domain.entity.enums.ProductState;
import com.cengo.muzayedebackendv2.domain.request.ProductSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.ProductUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ProductAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductListResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductOrderResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductResponse;
import com.cengo.muzayedebackendv2.domain.response.user.UserBuyerResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ProductMapper {

    Product convertToProduct(ProductSaveRequest request);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    ProductAdminResponse convertToProductAdminResponse(Product product, AssetResponse coverImage,
                                                       List<AssetResponse> mediaAssets, String artistName,
                                                       UserBuyerResponse requester, UserBuyerResponse buyer);

    @Mapping(target = "id", source = "product.id")
    @Mapping(target = "bannerText", source = "product.state", qualifiedByName = "mapBanner")
    ProductListResponse convertToProductListResponse(Product product, AssetResponse coverImage,
                                                     List<AssetResponse> mediaAssets, String artistName);

    @Mapping(target = "id", source = "product.id")
    ProductOrderResponse convertToProductOrderResponse(Product product, AssetResponse coverImage,
                                                       List<AssetResponse> mediaAssets, String artistName);


    @Mapping(target = "id", source = "product.id")
    ProductResponse convertToProductResponse(Product product, AssetResponse coverImage,
                                             List<AssetResponse> mediaAssets, String artistName);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest productUpdateRequest);


    @Named("mapBanner")
    default String mapBanner(ProductState state) {
        return state.getBannerText();
    }

}
