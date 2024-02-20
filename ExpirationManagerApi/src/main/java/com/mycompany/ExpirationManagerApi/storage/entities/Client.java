package com.mycompany.ExpirationManagerApi.storage.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    private String lastName;

    @Builder.Default
    @Column(nullable = false)
    private String patronymicName = "";

    @Column(unique = true, nullable = false)
    @NotBlank
    private String passport;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private LocalDate birthday;

    @Builder.Default
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<Card> cardList  = new ArrayList<Card>();
}
