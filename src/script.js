const categoryLists = document.querySelector("#category-lists");
const totalCount = document.querySelector("#total-count");
const dialog = document.querySelector("#item-dialog");
const form = document.querySelector("#item-form");
const toast = document.querySelector("#toast");
let toastTimer;
const icons = { seasoning: "♨", food: "♧" };
const escapeHtml = value => String(value ?? "").replace(/[&<>'"]/g, char => ({ "&":"&amp;", "<":"&lt;", ">":"&gt;", "'":"&#39;", '"':"&quot;" })[char]);

async function request(url, options = {}) {
  const response = await fetch(url, { headers: { "Content-Type": "application/json" }, ...options });
  if (!response.ok) { const body = await response.json().catch(() => ({})); throw new Error(body.message || "操作に失敗しました。"); }
  return response.status === 204 ? null : response.json();
}
function render(categories) {
  totalCount.textContent = categories.reduce((sum, category) => sum + category.items.length, 0);
  categoryLists.innerHTML = categories.map(category => `<section class="inventory-group ${category.code === "seasoning" ? "condiment-group" : "food-group"}"><div class="group-heading"><div class="group-icon">${icons[category.code] || "✦"}</div><div><p class="group-label">${escapeHtml(category.code)}</p><h3>${escapeHtml(category.name)}リスト</h3></div></div><ul class="item-list">${category.items.map(item => `<li class="stock-item"><span class="item-emoji">${escapeHtml(item.emoji || "📦")}</span><span class="item-name">${escapeHtml(item.name)}</span><div class="quantity-control"><button class="quantity-button" type="button" data-id="${item.id}" data-delta="-1" aria-label="${escapeHtml(item.name)}を1${escapeHtml(item.unit)}減らす" ${item.quantity === 0 ? "disabled" : ""}>−</button><output class="quantity">${item.quantity}</output><span class="unit">${escapeHtml(item.unit)}</span><button class="quantity-button" type="button" data-id="${item.id}" data-delta="1" aria-label="${escapeHtml(item.name)}を1${escapeHtml(item.unit)}増やす">＋</button></div></li>`).join("") || '<li class="empty-message">まだ登録されていません。</li>'}</ul><button class="add-button" type="button" data-category="${escapeHtml(category.code)}" data-category-name="${escapeHtml(category.name)}">＋ ${escapeHtml(category.name)}を追加</button></section>`).join("");
}
async function loadInventory() { try { render(await request("/api/categories")); } catch (error) { categoryLists.innerHTML = `<p class="error-message">${escapeHtml(error.message)} サーバーとDBの起動状態を確認してください。</p>`; } }
function showToast(message) { clearTimeout(toastTimer); toast.textContent = message; toast.classList.add("is-visible"); toastTimer = setTimeout(() => toast.classList.remove("is-visible"), 2600); }

categoryLists.addEventListener("click", async event => {
  const quantityButton = event.target.closest("[data-delta]");
  if (quantityButton) { quantityButton.disabled = true; try { await request(`/api/inventory-items/${quantityButton.dataset.id}/quantity`, { method:"PATCH", body:JSON.stringify({ delta:Number(quantityButton.dataset.delta) }) }); await loadInventory(); } catch (error) { showToast(error.message); quantityButton.disabled = false; } return; }
  const addButton = event.target.closest("[data-category]");
  if (addButton) { form.reset(); document.querySelector("#category-code").value = addButton.dataset.category; document.querySelector("#dialog-title").textContent = `${addButton.dataset.categoryName}を追加`; document.querySelector("#form-error").textContent = ""; dialog.showModal(); document.querySelector("#item-name").focus(); }
});
document.querySelector("#close-dialog").addEventListener("click", () => dialog.close());
form.addEventListener("submit", async event => { event.preventDefault(); const payload = Object.fromEntries(new FormData(form)); payload.quantity = Number(payload.quantity); payload.emoji = payload.emoji.trim() || null; try { await request("/api/inventory-items", { method:"POST", body:JSON.stringify(payload) }); dialog.close(); await loadInventory(); showToast("在庫を追加しました。"); } catch (error) { document.querySelector("#form-error").textContent = error.message; } });
loadInventory();
