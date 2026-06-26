package org.jboss.as.quickstarts.kitchensink.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;
import org.jboss.as.quickstarts.kitchensink.data.ProductRepository;
import org.jboss.as.quickstarts.kitchensink.model.Product;
import org.jboss.as.quickstarts.kitchensink.service.PricingService;
import org.jboss.as.quickstarts.kitchensink.service.VendorSelectionService;

@Path("/products")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResourceRESTService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private PricingService pricingService;

    @Inject
    private VendorSelectionService vendorSelectionService;

    /**
     * GET /products
     * Returns all products sorted by name.
     */
    @GET
    @Path("")
    public Response listProducts() {
        List<Product> products = productRepository.findAll();
        return Response.ok(products).build();
    }

    /**
     * GET /products/{id}
     * Returns a single product by ID, or 404 if not found.
     */
    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") Long id) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Product not found: " + id)
                .build();
        }
        return Response.ok(product).build();
    }

    /**
     * GET /products/category/{category}
     * Returns all products in a given category.
     */
    @GET
    @Path("/category/{category}")
    public Response getProductsByCategory(@PathParam("category") String category) {
        List<Product> products = productRepository.findByCategory(category);
        return Response.ok(products).build();
    }

    /**
     * GET /products/{id}/vendors?quantity=N
     * Returns ranked vendor list for a product at a given quantity.
     */
    @GET
    @Path("/{id}/vendors")
    public Response getVendorsForProduct(
            @PathParam("id") Long id,
            @QueryParam("quantity") @DefaultValue("1") int quantity) {
        Product product = productRepository.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Product not found: " + id)
                .build();
        }
        List<VendorSelectionService.VendorPriceResult> vendors =
            vendorSelectionService.getVendorPricesForProduct(id, quantity);
        return Response.ok(vendors).build();
    }

    /**
     * GET /products/{id}/price?vendorId=N&quantity=N
     * Returns the calculated unit price for a product/vendor/quantity combination.
     */
    @GET
    @Path("/{id}/price")
    public Response getPrice(
            @PathParam("id") Long id,
            @QueryParam("vendorId") Long vendorId,
            @QueryParam("quantity") @DefaultValue("1") int quantity) {
        if (vendorId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("vendorId query parameter is required")
                .build();
        }
        Product product = productRepository.findById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Product not found: " + id)
                .build();
        }
        try {
            BigDecimal unitPrice = pricingService.calculatePrice(id, vendorId, quantity);
            return Response.ok(unitPrice).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Could not calculate price: " + e.getMessage())
                .build();
        }
    }
}
