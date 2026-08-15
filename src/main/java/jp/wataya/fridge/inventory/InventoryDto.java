package jp.wataya.fridge.inventory;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public final class InventoryDto {
    private InventoryDto() { }
    public record ItemResponse(Long id, String name, int quantity, String unit, String emoji) { }
    public record CategoryResponse(String code, String name, List<ItemResponse> items) { }
    public record CreateItemRequest(
        @NotBlank @Size(max = 32) String categoryCode,
        @NotBlank @Size(max = 100) String name,
        @NotNull @Min(0) Integer quantity,
        @NotBlank @Size(max = 20) String unit,
        @Size(max = 32) String emoji
    ) { }
    public record QuantityChangeRequest(@NotNull @Min(-1000) @Max(1000) Integer delta) { }
}
