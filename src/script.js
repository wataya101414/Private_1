const totalCount = document.querySelector("#total-count");
const toast = document.querySelector("#toast");
let toastTimer;

function updateSummary() { totalCount.textContent = document.querySelectorAll(".stock-item").length; }
function updateQuantity(item, nextCount) {
  const count = Math.max(0, nextCount);
  const quantity = item.querySelector(".quantity");
  const decreaseButton = item.querySelector('[data-action="decrease"]');
  item.dataset.count = count;
  quantity.value = count;
  quantity.textContent = count;
  decreaseButton.disabled = count === 0;
}
document.querySelectorAll(".quantity-button").forEach((button) => {
  button.addEventListener("click", () => {
    const item = button.closest(".stock-item");
    updateQuantity(item, Number(item.dataset.count) + (button.dataset.action === "increase" ? 1 : -1));
  });
});
document.querySelectorAll(".stock-item").forEach((item) => updateQuantity(item, Number(item.dataset.count)));
document.querySelectorAll(".add-button").forEach((button) => {
  button.addEventListener("click", () => {
    clearTimeout(toastTimer);
    toast.textContent = `${button.dataset.category}の追加機能は準備中です`;
    toast.classList.add("is-visible");
    toastTimer = setTimeout(() => toast.classList.remove("is-visible"), 2600);
  });
});
updateSummary();
