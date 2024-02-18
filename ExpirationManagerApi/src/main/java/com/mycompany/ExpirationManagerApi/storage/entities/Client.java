package com.mycompany.ExpirationManagerApi.storage.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(unique = true)
    private String passport;

    @Column
    private LocalDate birthday;

    @Builder.Default
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<Card> cardList  = new ArrayList<Card>();
}
