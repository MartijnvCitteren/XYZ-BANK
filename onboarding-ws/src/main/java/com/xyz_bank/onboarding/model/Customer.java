package com.xyz_bank.onboarding.model;

import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.model.enums.Currency;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Account> accounts;

     public String getOriginalAccountNumber() {
        return this.getAccounts().getFirst().getIban();
    }

    public AccountType getAccountType() {
         return this.getAccounts().getFirst().getType();
    }

    public BigDecimal getBalance() {
         return this.getAccounts().getFirst().getBalance();
    }

    public Currency getCurrency() {
         return this.getAccounts().getFirst().getCurrency();
    }

    @PrePersist
    private void automaticallySetCustomerInRelatedEntities() {
        if (address != null && address.getCustomer() == null) {
            address.setCustomer(this);
        }
        if (accounts != null && !accounts.isEmpty()) {
            accounts.forEach(account -> account.setCustomer(this));
        }
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer other)){
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
