package com.xyz_bank.onboarding.model;

import com.xyz_bank.onboarding.model.enums.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Country country;

    private String city;
    private String street;
    private String zipCode;
    private String houseNumber;

    @OneToOne(mappedBy = "address")
    private Customer customer;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", country=" + country +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Address other)){
            return false;
        }

        return Objects.equals(country, other.getCountry()) &&
                Objects.equals(city, other.getCity()) &&
                Objects.equals(street, other.getStreet()) &&
                Objects.equals(zipCode, other.getZipCode()) &&
                Objects.equals(houseNumber, other.getHouseNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
