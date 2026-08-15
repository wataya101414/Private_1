package jp.wataya.fridge.category;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 32)
    private String code;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    protected Category() { }
    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public int getDisplayOrder() { return displayOrder; }
}
