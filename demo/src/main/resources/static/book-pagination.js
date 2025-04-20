let currentPage = 0;
const pageSize = 5;

function cargarLibros(page = 0) {
  fetch(`/api/books/page?page=${page}&size=${pageSize}`, {
    headers: {
      "Authorization": `Bearer ${localStorage.getItem("token")}`
    }
  })
    .then(res => res.json())
    .then(data => {
      mostrarLibros(data.content);
      actualizarBotones(data.totalPages, data.number);
    });
}

function mostrarLibros(libros) {
  const contenedor = document.getElementById("lista-libros");
  contenedor.innerHTML = "";
  libros.forEach(libro => {
    contenedor.innerHTML += `<li><strong>${libro.title}</strong> — ${libro.author}</li>`;
  });
}

function actualizarBotones(totalPages, currentPage) {
  document.getElementById("prev").disabled = currentPage === 0;
  document.getElementById("next").disabled = currentPage >= totalPages - 1;
}

document.getElementById("next").addEventListener("click", () => {
  currentPage++;
  cargarLibros(currentPage);
});

document.getElementById("prev").addEventListener("click", () => {
  currentPage--;
  cargarLibros(currentPage);
});

cargarLibros();
