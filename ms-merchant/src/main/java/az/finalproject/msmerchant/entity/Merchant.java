package az.finalproject.msmerchant.entity;

import az.finalproject.msmerchant.enums.MerchantStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "merchants")
public class Merchant {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String category;
    private String addressText;
    private Double latitude;
    private Double longitude;
    @Enumerated(EnumType.STRING)
    private MerchantStatus status;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<Product> products;

}