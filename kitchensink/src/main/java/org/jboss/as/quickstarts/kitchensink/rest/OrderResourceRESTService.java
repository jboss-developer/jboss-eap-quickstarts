package org.jboss.as.quickstarts.kitchensink.rest;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import org.jboss.as.quickstarts.kitchensink.model.Order;
import org.jboss.as.quickstarts.kitchensink.service.OrderService;

@Path("/orders")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResourceRESTService {

    @Inject
    private OrderService orderService;

    /**
     * POST /orders/cart/{memberId}
     * Adds a product to the member's draft cart.
     * Body: { "productId": N, "quantity": N }
     */
    @POST
    @Path("/cart/{memberId}")
    public Response addToCart(
            @PathParam("memberId") Long memberId,
            Map<String, Object> body) {
        if (body == null || !body.containsKey("productId") || !body.containsKey("quantity")) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Request body must contain productId and quantity")
                .build();
        }
        Long productId = ((Number) body.get("productId")).longValue();
        int quantity = ((Number) body.get("quantity")).intValue();
        if (quantity <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("quantity must be greater than zero")
                .build();
        }
        orderService.addToCart(memberId, productId, quantity);
        return Response.status(Response.Status.CREATED)
            .entity("Item added to cart")
            .build();
    }

    /**
     * DELETE /orders/cart/{memberId}/{productId}
     * Removes a product from the member's draft cart.
     */
    @DELETE
    @Path("/cart/{memberId}/{productId}")
    public Response removeFromCart(
            @PathParam("memberId") Long memberId,
            @PathParam("productId") Long productId) {
        orderService.removeFromCart(memberId, productId);
        return Response.ok("Item removed from cart").build();
    }

    /**
     * GET /orders/cart/{memberId}/preview?zip=XXXXX&expedite=true|false
     * Returns a full order preview with pricing, discount, and shipping breakdown.
     */
    @GET
    @Path("/cart/{memberId}/preview")
    public Response previewOrder(
            @PathParam("memberId") Long memberId,
            @QueryParam("zip") String zip,
            @QueryParam("expedite") @DefaultValue("false") boolean expedite) {
        if (zip == null || zip.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("zip query parameter is required")
                .build();
        }
        try {
            OrderService.OrderPreview preview = orderService.previewOrder(memberId, zip, expedite);
            return Response.ok(preview).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Could not preview order: " + e.getMessage())
                .build();
        }
    }

    /**
     * POST /orders/submit/{memberId}?zip=XXXXX&expedite=true|false
     * Submits the order via the process_order() stored procedure.
     * Returns the new order ID.
     */
    @POST
    @Path("/submit/{memberId}")
    public Response submitOrder(
            @PathParam("memberId") Long memberId,
            @QueryParam("zip") String zip,
            @QueryParam("expedite") @DefaultValue("false") boolean expedite) {
        if (zip == null || zip.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("zip query parameter is required")
                .build();
        }
        try {
            Long orderId = orderService.submitOrder(memberId, zip, expedite);
            return Response.status(Response.Status.CREATED)
                .entity(Map.of("orderId", orderId))
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Could not submit order: " + e.getMessage())
                .build();
        }
    }

    /**
     * GET /orders/{orderId}
     * Returns a single order by ID, or 404 if not found.
     */
    @GET
    @Path("/{orderId}")
    public Response getOrder(@PathParam("orderId") Long orderId) {
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Order not found: " + orderId)
                .build();
        }
        return Response.ok(order).build();
    }

    /**
     * GET /orders/member/{memberId}
     * Returns the order history for a member.
     */
    @GET
    @Path("/member/{memberId}")
    public Response getOrderHistory(@PathParam("memberId") Long memberId) {
        List<Order> orders = orderService.getOrderHistory(memberId);
        return Response.ok(orders).build();
    }
}
