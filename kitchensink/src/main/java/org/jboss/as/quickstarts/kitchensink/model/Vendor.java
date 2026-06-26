package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vendors")
public class Vendor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(name = "fulfillment_rating", precision = 3, scale = 1)
    private BigDecimal fulfillmentRating;

    @Column(name = "avg_shipping_days")
    private Integer avgShippingDays;

    @Column(name = "contact_email")
    private String contactEmail;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFulfillmentRating() {
        return fulfillmentRating;
    }

    public void setFulfillmentRating(BigDecimal fulfillmentRating) {
        this.fulfillmentRating = fulfillmentRating;
    }

    public Integer getAvgShippingDays() {
        return avgShippingDays;
    }

    public void setAvgShippingDays(Integer avgShippingDays) {
        this.avgShippingDays = avgShippingDays;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
