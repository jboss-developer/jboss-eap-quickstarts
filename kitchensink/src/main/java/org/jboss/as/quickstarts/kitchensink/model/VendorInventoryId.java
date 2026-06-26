package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class VendorInventoryId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "vendor_id")
    private Long vendorId;

    @Column(name = "product_id")
    private Long productId;

    public VendorInventoryId() {
    }

    public VendorInventoryId(Long vendorId, Long productId) {
        this.vendorId = vendorId;
        this.productId = productId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VendorInventoryId that = (VendorInventoryId) o;
        return Objects.equals(vendorId, that.vendorId) &&
               Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vendorId, productId);
    }
}
