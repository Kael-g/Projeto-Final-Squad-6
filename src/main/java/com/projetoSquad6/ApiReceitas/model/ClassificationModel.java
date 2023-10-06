package com.projetoSquad6.ApiReceitas.model;

import com.projetoSquad6.ApiReceitas.enums.ClassificationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_classifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClassification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ClassificationEnum classification;
}