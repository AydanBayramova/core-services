package az.finalproject.mscourier.entity;

import az.finalproject.mscourier.enums.CourierStatus;
import az.finalproject.mscourier.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "couriers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userAuthId;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    private CourierStatus status;

    private Double currentLat;
    private Double currentLon;
}
