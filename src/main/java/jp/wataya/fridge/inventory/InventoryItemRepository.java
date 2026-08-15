package jp.wataya.fridge.inventory;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByCategoryIdOrderByDisplayOrderAscIdAsc(Long categoryId);
    boolean existsByCategoryIdAndName(Long categoryId, String name);
}
