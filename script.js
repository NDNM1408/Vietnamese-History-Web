const historicalEntities = [
  {
    type: "Character",
    name: "Trung Sisters",
    description: "Warrior sisters who rebelled against Chinese rule.",
  },
  {
    type: "Dynasty",
    name: "Le Loi",
    description: "Founder of the Le dynasty and a national hero.",
  },
  {
    type: "Event",
    name: "Nguyen Hue",
    description: "Led the Tay Son rebellion against the ruling class.",
  },
  // Add more entities as needed
];

const searchInput = document.getElementById("searchInput");
const resultContainer = document.getElementById("result");
const modal = document.getElementById("modal");
const modalTitle = document.getElementById("modalTitle");
const modalDescription = document.getElementById("modalDescription");

function filterEntities(entityType, tabElement) {
  // Remove the "active" class from all tabs
  document
    .querySelectorAll(".menu a")
    .forEach((tab) => tab.classList.remove("active"));

  // Add the "active" class to the clicked tab
  tabElement.classList.add("active");

  searchEntities("", entityType);
}

function searchEntities(query, entityType) {
  resultContainer.innerHTML = "";

  const filteredEntities = historicalEntities.filter(
    (entity) =>
      (entityType === "All" || entity.type === entityType) &&
      entity.name.toLowerCase().includes(query.toLowerCase())
  );

  filteredEntities.forEach((entity) => {
    const listItem = document.createElement("li");
    listItem.className = "entity";
    listItem.innerHTML = `<strong>${entity.name}</strong><br>${entity.description}`;
    listItem.addEventListener("click", () =>
      openModal(entity.name, entity.description)
    );
    resultContainer.appendChild(listItem);
  });
}

function openModal(title, description) {
  modalTitle.textContent = title;
  modalDescription.textContent = description;
  modal.style.display = "flex";
}

function closeModal() {
  modal.style.display = "none";
}

searchInput.addEventListener("input", function () {
  const selectedTab = document.querySelector(".menu a.active");
  const selectedEntityType = selectedTab ? selectedTab.innerText.trim() : "";
  searchEntities(this.value, selectedEntityType);
});

window.addEventListener("click", function (event) {
  if (event.target === modal) {
    closeModal();
  }
});

// Initial search to display all entities
searchEntities("");
