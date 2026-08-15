package jp.wataya.fridge.inventory;

import java.util.List;
import jp.wataya.fridge.category.Category;
import jp.wataya.fridge.category.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class InventoryService {
    private final CategoryRepository categoryRepository;
    private final InventoryItemRepository itemRepository;
    public InventoryService(CategoryRepository categoryRepository, InventoryItemRepository itemRepository) {
        this.categoryRepository = categoryRepository; this.itemRepository = itemRepository;
    }
    public List<InventoryDto.CategoryResponse> listCategories() {
        return categoryRepository.findAllByOrderByDisplayOrderAscIdAsc().stream().map(category ->
            new InventoryDto.CategoryResponse(category.getCode(), category.getName(),
                itemRepository.findByCategoryIdOrderByDisplayOrderAscIdAsc(category.getId()).stream().map(this::toResponse).toList())
        ).toList();
    }
    @Transactional
    public InventoryDto.ItemResponse create(InventoryDto.CreateItemRequest request) {
        Category category = categoryRepository.findByCode(request.categoryCode())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "カテゴリが見つかりません。"));
        String name = request.name().trim();
        if (itemRepository.existsByCategoryIdAndName(category.getId(), name)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "同じカテゴリに同名の在庫があります。");
        }
        InventoryItem item = new InventoryItem(category, name, request.quantity(), request.unit().trim(), request.emoji(), (short)0);
        return toResponse(itemRepository.save(item));
    }
    @Transactional
    public InventoryDto.ItemResponse changeQuantity(Long id, InventoryDto.QuantityChangeRequest request) {
        InventoryItem item = itemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "在庫が見つかりません。"));
        item.changeQuantityBy(request.delta());
        return toResponse(item);
    }
    private InventoryDto.ItemResponse toResponse(InventoryItem item) {
        return new InventoryDto.ItemResponse(item.getId(), item.getName(), item.getQuantity(), item.getUnit(), item.getEmoji());
    }
}
