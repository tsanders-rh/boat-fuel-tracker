package com.boatfuel.entity;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * FuelUp entity with legacy Hibernate annotations
 * Konveyor violations: Hibernate-specific annotations, old @Index usage
 */
@Entity
@Table(name = "FUEL_UPS")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class FuelUp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FUEL_UP_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Temporal(TemporalType.DATE)
    @Column(name = "FUEL_DATE", nullable = false)
    @Index(name = "IDX_FUEL_DATE") // Old Hibernate @Index (deprecated)
    private Date date;

    @Column(name = "GALLONS", precision = 10, scale = 2, nullable = false)
    private BigDecimal gallons;

    @Column(name = "PRICE_PER_GALLON", precision = 10, scale = 2, nullable = false)
    private BigDecimal pricePerGallon;

    @Column(name = "TOTAL_COST", precision = 10, scale = 2)
    private BigDecimal totalCost;

    @Column(name = "ENGINE_HOURS", precision = 10, scale = 1)
    private BigDecimal engineHours;

    @Column(name = "LOCATION", length = 500)
    @Type(type = "text") // Hibernate-specific type
    private String location;

    @Column(name = "NOTES", length = 2000)
    @org.hibernate.annotations.Type(type = "text") // Hibernate-specific
    private String notes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    @org.hibernate.annotations.CreationTimestamp
    private Date createdAt;

    // Default constructor
    public FuelUp() {
    }

    // Constructor
    public FuelUp(User user, Date date, BigDecimal gallons, BigDecimal pricePerGallon) {
        this.user = user;
        this.date = date;
        this.gallons = gallons;
        this.pricePerGallon = pricePerGallon;
        this.totalCost = gallons.multiply(pricePerGallon);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getGallons() {
        return gallons;
    }

    public void setGallons(BigDecimal gallons) {
        this.gallons = gallons;
        calculateTotalCost();
    }

    public BigDecimal getPricePerGallon() {
        return pricePerGallon;
    }

    public void setPricePerGallon(BigDecimal pricePerGallon) {
        this.pricePerGallon = pricePerGallon;
        calculateTotalCost();
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getEngineHours() {
        return engineHours;
    }

    public void setEngineHours(BigDecimal engineHours) {
        this.engineHours = engineHours;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    private void calculateTotalCost() {
        if (gallons != null && pricePerGallon != null) {
            this.totalCost = gallons.multiply(pricePerGallon);
        }
    }
}
