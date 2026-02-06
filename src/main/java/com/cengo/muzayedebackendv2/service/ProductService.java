package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.dto.SaveAssetDTO;
import com.cengo.muzayedebackendv2.domain.dto.SendNotificationDTO;
import com.cengo.muzayedebackendv2.domain.entity.Product;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.domain.entity.enums.ProductState;
import com.cengo.muzayedebackendv2.domain.enums.NotificationType;
import com.cengo.muzayedebackendv2.domain.request.ProductSaveRequest;
import com.cengo.muzayedebackendv2.domain.request.ProductUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.AssetResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.ProductAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistListProductResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductListResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductOrderResponse;
import com.cengo.muzayedebackendv2.domain.response.product.ProductResponse;
import com.cengo.muzayedebackendv2.mapper.ProductMapper;
import com.cengo.muzayedebackendv2.repository.ProductRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import com.cengo.muzayedebackendv2.util.rsql.RSQLSpecificationConverter;
import com.cengo.muzayedebackendv2.validation.product.ProductBuyValidation;
import com.cengo.muzayedebackendv2.validation.product.ProductSaleDecisionValidation;
import com.cengo.muzayedebackendv2.validation.product.ProductSaveValidation;
import com.cengo.muzayedebackendv2.validation.product.ProductUpdateValidation;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class ProductService extends BaseEntityService<Product, ProductRepository> {

    private final ProductMapper productMapper;
    private final ArtistService artistService;
    private final AssetService assetService;
    private final ProductUpdateValidation productUpdateValidation;
    private final ProductSaveValidation productSaveValidation;
    private final ProductBuyValidation productBuyValidation;
    private final ProductSaleDecisionValidation productSaleDecisionValidation;
    private final UserService userService;
    private final NotificationService notificationService;
    private final RSQLSpecificationConverter rsqlConverter;

    protected ProductService(ProductRepository repository, ProductMapper productMapper, @Lazy ArtistService artistService, AssetService assetService, ProductUpdateValidation productUpdateValidation, ProductSaveValidation productSaveValidation, ProductBuyValidation productBuyValidation, ProductSaleDecisionValidation productSaleDecisionValidation, UserService userService, NotificationService notificationService, RSQLSpecificationConverter rsqlConverter) {
        super(repository);
        this.productMapper = productMapper;
        this.artistService = artistService;
        this.assetService = assetService;
        this.productUpdateValidation = productUpdateValidation;
        this.productSaveValidation = productSaveValidation;
        this.productBuyValidation = productBuyValidation;
        this.productSaleDecisionValidation = productSaleDecisionValidation;
        this.userService = userService;
        this.notificationService = notificationService;
        this.rsqlConverter = rsqlConverter;
    }

    @Transactional
    public ProductAdminResponse saveProduct(ProductSaveRequest request, MultipartFile coverImage, List<MultipartFile> images) {
        Product product = save(productMapper.convertToProduct(request));

        productSaveValidation.validate(new ProductSaveValidation.Context(product));

        images = images == null ? Stream.of((MultipartFile) null).toList() : images;

        var coverImageAsset = saveProductCoverImage(coverImage, product.getId());
        var mediaAssets = saveProductMediaImages(images, product.getId());
        var artistName = artistService.getArtistById(product.getArtistId()).fullName();

        return productMapper.convertToProductAdminResponse(product, coverImageAsset, mediaAssets, artistName, null, null);
    }

    @Transactional
    public ProductAdminResponse decideSale(UUID productId, Boolean decision) {
        Product product = getById(productId);

        productSaleDecisionValidation.validate(new ProductSaleDecisionValidation.Context(product));

        if (Boolean.TRUE.equals(decision)) {
            product.setBuyerId(product.getRequesterId());
            product.setState(ProductState.SOLD);

            var dto = SendNotificationDTO.builder()
                    .userId(product.getRequesterId())
                    .type(NotificationType.PRODUCT)
                    .productName(product.getName())
                    .link(NotificationType.PRODUCT.getLink(productId))
                    .build();
            notificationService.saveNotification(dto);
        } else {
            product.setState(ProductState.ON_SALE);
        }

        product.setRequesterId(null);

        return getProductAdminResponse(save(product));
    }

    @Transactional
    public ProductAdminResponse updateProduct(UUID productId, ProductUpdateRequest productUpdateRequest) {
        Product product = getById(productId);

        productUpdateValidation.validate(new ProductUpdateValidation.Context(product));

        productMapper.updateProduct(product, productUpdateRequest);

        return getProductAdminResponse(save(product));
    }

    @Transactional
    public void deleteProductById(UUID productId) {
        var product = getById(productId);

        productUpdateValidation.validate(new ProductUpdateValidation.Context(product));

        delete(product);
    }

    @Transactional
    public void deleteAllProducts(List<Product> products) {
        products.forEach(this::deleteProduct);
    }

    public void deleteProduct(Product product) {
        assetService.deleteAllByReferenceId(product.getId());
        delete(product);
    }

    public Page<ProductAdminResponse> adminGetAllProducts(Pageable pageable) {
        Page<Product> products = findAll(pageable);

        return products.map(this::getProductAdminResponse);
    }

    public ProductAdminResponse adminGetProductById(UUID productId) {
        return getProductAdminResponse(getById(productId));
    }

    public List<Product> findProductsByArtistId(UUID artistId) {
        return getRepository().findAllByArtistId(artistId);
    }

    public Page<ProductListResponse> getAllProducts(Pageable pageable, String productFilter) {
        Specification<Product> rsqlSpec = rsqlConverter.convertToSpec(productFilter);
        Page<Product> products = getRepository().findAll(rsqlSpec, pageable);
        return products.map(this::getProductListResponse);
    }

    public List<ArtistListProductResponse> getArtistsOfProducts() {
        // TODO bütün productları memmory'e getirmek günah
        var products = getRepository().findAll();
        var productCountByArtistId = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getArtistId,
                        Collectors.counting()
                ));

        var artistNames = artistService.getArtistNameInfo(productCountByArtistId.keySet());
        return artistNames.stream().map(artistNameDTO ->
                new ArtistListProductResponse(
                        artistNameDTO.getId(),
                        artistNameDTO.getFullName(),
                        productCountByArtistId.get(artistNameDTO.getId()).intValue()
                )
        ).toList();
    }

    public ProductResponse getProductById(UUID productId) {
        Product product = getById(productId);
        return getProductResponse(product);
    }

    public List<ProductAdminResponse> adminGetRequestProducts() {
        List<Product> products = getRepository().findAllByState(ProductState.SALE_REQUEST);

        return products.stream().map(this::getProductAdminResponse).toList();
    }

    @Transactional
    public void buyProduct(UUID productId, UUID userId) {
        Product product = getById(productId);

        productBuyValidation.validate(new ProductBuyValidation.Context(product));

        product.setRequesterId(userId);
        product.setState(ProductState.SALE_REQUEST);
    }

    public List<ProductOrderResponse> getUserOrders(UUID userId) {
        var orders = Stream.concat(
                getRepository().findAllByRequesterId(userId).stream(),
                getRepository().findAllByBuyerId(userId).stream()
        ).toList();

        return orders.stream().map(this::getProductOrderResponse).toList();
    }


    private ProductOrderResponse getProductOrderResponse(Product product) {
        var coverImageAsset = assetService.getAssetByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_COVER);
        var mediaAssets = assetService.getAssetsByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_MEDIA);
        var artistName = artistService.getArtistById(product.getArtistId()).fullName();

        return productMapper.convertToProductOrderResponse(product, coverImageAsset, mediaAssets, artistName);
    }

    private ProductResponse getProductResponse(Product product) {
        var coverImageAsset = assetService.getAssetByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_COVER);
        var mediaAssets = assetService.getAssetsByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_MEDIA);
        var artistName = artistService.getArtistById(product.getArtistId()).fullName();

        return productMapper.convertToProductResponse(product, coverImageAsset, mediaAssets, artistName);
    }

    private ProductListResponse getProductListResponse(Product product) {
        var coverImageAsset = assetService.getAssetByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_COVER);
        var mediaAssets = assetService.getAssetsByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_MEDIA);
        var artistName = artistService.getArtistById(product.getArtistId()).fullName();

        return productMapper.convertToProductListResponse(product, coverImageAsset, mediaAssets, artistName);
    }

    private ProductAdminResponse getProductAdminResponse(Product product) {
        var coverImageAsset = assetService.getAssetByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_COVER);
        var mediaAssets = assetService.getAssetsByReferenceIdAndType(product.getId(), AssetDomainType.PRODUCT_MEDIA);
        var artistName = artistService.getArtistById(product.getArtistId()).fullName();
        var requester = userService.getBuyerUserById(product.getRequesterId());
        var buyer = userService.getBuyerUserById(product.getBuyerId());

        return productMapper.convertToProductAdminResponse(product, coverImageAsset, mediaAssets, artistName, requester, buyer);
    }

    private AssetResponse saveProductCoverImage(MultipartFile coverImage, UUID id) {
        return assetService.saveAsset(
                SaveAssetDTO.builder()
                        .referenceId(id)
                        .file(coverImage)
                        .domainType(AssetDomainType.PRODUCT_COVER)
                        .build()
        );
    }

    private List<AssetResponse> saveProductMediaImages(List<MultipartFile> images, UUID id) {
        var assetsToSave = IntStream.range(0, images.size())
                .mapToObj(index ->
                        SaveAssetDTO.builder()
                                .referenceId(id)
                                .file(images.get(index))
                                .domainType(AssetDomainType.PRODUCT_MEDIA)
                                .orderNo(index + 1)
                                .build()
                ).toList();

        return assetService.saveAssets(assetsToSave);
    }
}
