package jp.wataya.fridge.inventory;

import jakarta.persistence.*;
import jp.wataya.fridge.category.Category;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false, length = 20)
    private String unit;
    @Column(length = 32)
    private String emoji;
    @Column(name = "display_order", nullable = false)
    private short displayOrder;

    protected InventoryItem() { }
    public InventoryItem(Category category, String name, int quantity, String unit, String emoji, short displayOrder) {
        this.category = category; this.name = name; this.quantity = quantity; this.unit = unit; this.emoji = emoji; this.displayOrder = displayOrder;
    }
    public void changeQuantityBy(int delta) { quantity = Math.max(0, quantity + delta); }
    public Long getId() { return id; }
    public Category getCategory() { return category; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getEmoji() { return emoji; }
    public short getDisplayOrder() { return displayOrder; }
}
