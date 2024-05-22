package Designovel.Capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "wconcept_variable")
public class WConceptVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer variableId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false)
    })
    private Product product;

    private String productName;
    private Integer likes;
    private Boolean soldOut;

}