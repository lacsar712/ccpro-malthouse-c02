package com.malthouse.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "fermenters",
        uniqueConstraints = @UniqueConstraint(name = "uk_fermenter_site_tank", columnNames = {"site_id", "tank_code"})
)
public class Fermenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "site_id", nullable = false)
    private Long siteId;

    @Column(name = "tank_code", nullable = false, length = 64)
    private String tankCode;

    @Column(nullable = false)
    private Double capacityLiters;

    /** idle | fermenting | cip */
    @Column(nullable = false, length = 32)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
        this.siteId = siteId;
    }

    public String getTankCode() {
        return tankCode;
    }

    public void setTankCode(String tankCode) {
        this.tankCode = tankCode;
    }

    public Double getCapacityLiters() {
        return capacityLiters;
    }

    public void setCapacityLiters(Double capacityLiters) {
        this.capacityLiters = capacityLiters;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
