package com.xyz_bank.onboarding.model;

import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.model.enums.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String iban;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @Setter(AccessLevel.PACKAGE)
    private Customer customer;

    public Account(String iban, AccountType type, Currency currency, BigDecimal balance, Customer customer) {
        this.iban = iban;
        this.type = type;
        this.currency = currency;
        this.balance = balance;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", type=" + type +
                ", currency=" + currency +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Account account)){
            return false;
        }
        return Objects.equals(account.iban, iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
