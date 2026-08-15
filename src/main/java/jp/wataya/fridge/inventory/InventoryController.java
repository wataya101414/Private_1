package jp.wataya.fridge.inventory;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InventoryController {
    private final InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService) { this.inventoryService = inventoryService; }
    @GetMapping("/categories")
    public List<InventoryDto.CategoryResponse> categories() { return inventoryService.listCategories(); }
    @PostMapping("/inventory-items")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryDto.ItemResponse create(@Valid @RequestBody InventoryDto.CreateItemRequest request) { return inventoryService.create(request); }
    @PatchMapping("/inventory-items/{id}/quantity")
    public InventoryDto.ItemResponse changeQuantity(@PathVariable Long id, @Valid @RequestBody InventoryDto.QuantityChangeRequest request) {
        return inventoryService.changeQuantity(id, request);
    }
}
