// script.js (replace your current file with this)

// Base server URL
const SERVER_URL = "http://localhost:8080";

// Helper: read token at call-time (so updated token after login is used)
function getToken() {
  return localStorage.getItem("token");
}

// Helper to safely parse JSON (returns null for empty body)
async function parseJSON(response) {
  const text = await response.text();
  if (!text) return null;
  try {
    return JSON.parse(text);
  } catch (e) {
    return null;
  }
}

/* ----------------------
   Auth: login & register
   ---------------------- */
async function login() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  try {
    const res = await fetch(`${SERVER_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    const data = await parseJSON(res);
    if (!res.ok) throw new Error(data?.message || res.statusText || "Login failed");

    if (!data || !data.token) throw new Error("No token received from server");
    localStorage.setItem("token", data.token);
    window.location.href = "todos.html";
  } catch (err) {
    alert(err.message);
  }
}
document.getElementById("new-todo").addEventListener("keypress", function (event) {
  if (event.key === "Enter") {
    addTodo(); // call your method
  }
});

async function register() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  try {
    const res = await fetch(`${SERVER_URL}/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    const data = await parseJSON(res);
    if (!res.ok) throw new Error(data?.message || res.statusText || "Registration failed");

    alert("Registration successful! Please login.");
    window.location.href = "login.html";
  } catch (err) {
    alert(err.message);
  }
}

/* ----------------------
   Todos: create / read / update / delete
   ---------------------- */

function createTodoCard(todo) {
  const card = document.createElement("div");
  card.className = "todo-card";

  const checkbox = document.createElement("input");
  checkbox.type = "checkbox";
  checkbox.className = "todo-checkbox";
  checkbox.checked = todo.isCompleted;
  checkbox.addEventListener("change", function () {
    const updatedTodo = { ...todo, isCompleted: checkbox.checked };
    updateTodoStatus(updatedTodo);
  });

  const span = document.createElement("span");
  span.textContent = todo.title;

  if (todo.isCompleted) {
    span.style.textDecoration = "line-through";
    span.style.color = "#aaa";
  }

  const deleteButton = document.createElement("button");
  deleteButton.textContent = "X";
  deleteButton.onclick = function () { deleteTodo(todo.id); };

  card.appendChild(checkbox);
  card.appendChild(span);
  card.appendChild(deleteButton);

  return card;
}

async function loadTodos() {
  const token = getToken();
  if (!token) {
    alert("Please login first!");
    window.location.href = "login.html";
    return;
  }

  try {
    const res = await fetch(`${SERVER_URL}/api/v1/todo`, {
      method: "GET",
      headers: { "Authorization": `Bearer ${token}` }
    });

    const data = await parseJSON(res);
    if (!res.ok) throw new Error(data?.message || res.statusText || "Failed to load todos");

    const todos = Array.isArray(data) ? data : [];
    const todoList = document.getElementById("todo-list");
    todoList.innerHTML = "";

    if (!todos || todos.length === 0) {
      todoList.innerHTML = `<p id="empty-message">No Todos yet. Please add some below!</p>`;
    } else {
      todos.forEach(todo => todoList.appendChild(createTodoCard(todo)));
    }
  } catch (err) {
    alert(err.message);
    const todoList = document.getElementById("todo-list");
    if (todoList) todoList.innerHTML = `<p style="color:red">Failed to load Todos!</p>`;
  }
}

async function addTodo() {
  const input = document.getElementById("new-todo");
  if (!input) return;
  const todoText = input.value.trim();
  if (!todoText) return;

  const token = getToken();
  try {
    // console.log(token);
    const res = await fetch(`${SERVER_URL}/api/v1/todo/create`, {   // removed /create
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify({
        title: todoText,
        description: todoText,
        isCompleted: true,
        email: "a@gmail.com"
      })   // simplified body
    });

    const data = await parseJSON(res);
    if (!res.ok) throw new Error(data?.message || res.statusText || "Failed to add todo");

    input.value = "";
    await loadTodos();
  } catch (err) {
    alert(err.message);
  }
}

async function updateTodoStatus(todo) {
  const token = getToken();
  try {
    // Controller expects PUT /api/v1/todo (body contains id)
    const res = await fetch(`${SERVER_URL}/api/v1/todo`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(todo)
    });

    const data = await parseJSON(res);
    if (!res.ok) throw new Error(data?.message || res.statusText || "Failed to update todo");

    await loadTodos();
  } catch (err) {
    alert(err.message);
  }
}

async function deleteTodo(id) {
  const token = getToken();
  try {
    const res = await fetch(`${SERVER_URL}/api/v1/todo/${id}`, {
      method: "DELETE",
      headers: { "Authorization": `Bearer ${token}` }
    });

    if (!res.ok) {
      const data = await parseJSON(res);
      throw new Error(data?.message || res.statusText || "Failed to delete todo");
    }

    await loadTodos();
  } catch (err) {
    alert(err.message);
  }
}

/* ----------------------
   Page initialization
   ---------------------- */
document.addEventListener("DOMContentLoaded", function () {
  if (document.getElementById("todo-list")) {
    loadTodos();
  }
});
