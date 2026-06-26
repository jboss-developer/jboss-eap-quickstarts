package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "vendor_inventory")
public class VendorInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private VendorInventoryId id;

    @Column(name = "markup_percent", precision = 6, scale = 2)
    private BigDecimal markupPercent;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    public VendorInventoryId getId() {
        return id;
    }

    public void setId(VendorInventoryId id) {
        this.id = id;
    }

    public BigDecimal getMarkupPercent() {
        return markupPercent;
    }

    public void setMarkupPercent(BigDecimal markupPercent) {
        this.markupPercent = markupPercent;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
}
