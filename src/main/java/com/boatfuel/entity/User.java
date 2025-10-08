package com.boatfuel.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User entity with Hibernate-specific annotations (anti-pattern)
 * Konveyor will flag: Proprietary Hibernate annotations, should use standard JPA
 */
@Entity
@Table(name = "USERS")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2") // Hibernate-specific
    @Column(name = "USER_ID", length = 50)
    private String userId;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "DISPLAY_NAME", length = 255)
    private String displayName;

    @Column(name = "PASSWORD_HASH", length = 255)
    private String passwordHash;

    @Column(name = "IS_ADMIN")
    @Type(type = "yes_no") // Hibernate-specific type
    private Boolean isAdmin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    @org.hibernate.annotations.CreationTimestamp // Hibernate-specific
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGIN")
    private Date lastLogin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FuelUp> fuelUps;

    // Default constructor
    public User() {
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<FuelUp> getFuelUps() {
        return fuelUps;
    }

    public void setFuelUps(List<FuelUp> fuelUps) {
        this.fuelUps = fuelUps;
    }
}
