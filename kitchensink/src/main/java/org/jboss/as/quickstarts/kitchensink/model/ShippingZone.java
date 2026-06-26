package org.jboss.as.quickstarts.kitchensink.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shipping_zones")
public class ShippingZone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zone_name")
    private String zoneName;

    @Column(name = "zip_range_start")
    private String zipRangeStart;

    @Column(name = "zip_range_end")
    private String zipRangeEnd;

    @Column(name = "base_rate_per_lb", precision = 6, scale = 4)
    private BigDecimal baseRatePerLb;

    @Column(name = "min_days")
    private Integer minDays;

    @Column(name = "max_days")
    private Integer maxDays;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZipRangeStart() {
        return zipRangeStart;
    }

    public void setZipRangeStart(String zipRangeStart) {
        this.zipRangeStart = zipRangeStart;
    }

    public String getZipRangeEnd() {
        return zipRangeEnd;
    }

    public void setZipRangeEnd(String zipRangeEnd) {
        this.zipRangeEnd = zipRangeEnd;
    }

    public BigDecimal getBaseRatePerLb() {
        return baseRatePerLb;
    }

    public void setBaseRatePerLb(BigDecimal baseRatePerLb) {
        this.baseRatePerLb = baseRatePerLb;
    }

    public Integer getMinDays() {
        return minDays;
    }

    public void setMinDays(Integer minDays) {
        this.minDays = minDays;
    }

    public Integer getMaxDays() {
        return maxDays;
    }

    public void setMaxDays(Integer maxDays) {
        this.maxDays = maxDays;
    }
}
