# 🛍️ Fake Store App

Overview
The Fake Store App is an Android application that interacts with [FakeStoreAPI](https://fakestoreapi.com) to display and manage products. 📱✨ The app is built using Kotlin and Jetpack Compose, following the MVVM architecture with various modern Android development tools. 🚀

## 🛠️ Tech Stack

* **Programming Language:** Kotlin 💻
* **UI Framework:** Jetpack Compose 🎨
* **Networking:** Ktor 🌐
* **Dependency Injection:** Hilt 💉
* **Local Storage:** Room Database 💾, DataStore Preferences ⚙️
* **Design Pattern:** MVVM with Flow, Sealed Class State, Repository Pattern 🏗️
* **Testing:** Unit Tests with Mockito ✅

## 🏛️ Architecture

The app follows the MVVM (Model-View-ViewModel) architecture, ensuring a clean separation of concerns. 🧹

### Layers:

* **UI Layer (View & ViewModel):**
    * Jetpack Compose is used for building UI components. 🖌️
    * ViewModel handles UI state and business logic directly. 🧠
    * Sealed class is used for representing different states (Loading, Success, Error). 🚦
* **Data Layer:**
    * Repository handles data fetching from both remote (Ktor) and local (Room, DataStore Preferences) sources. 📂
    * Uses Flow to provide reactive data streams. 🌊

## 📡 API Integration

The app fetches product data from [FakeStoreAPI](https://fakestoreapi.com) using Ktor. 🔗

* **Base URL:** `https://fakestoreapi.com`
* **Endpoints Used:**
    * `GET /products` - Fetch all products. 📦
    * `POST /auth/login` - User authentication. 🔑

## 💾 Data Persistence

* **Room Database:**
    * Stores products and cart items locally. 🛒
    * Provides offline support. 📶❌
* **DataStore Preferences:**
    * Stores user settings. ⚙️

## 💉 Dependency Injection

Hilt is used for dependency injection, ensuring better code modularity and testability. 🧪

## ✅ Unit Testing

Uses JUnit and Mockito for mocking dependencies. 🧪

* Ensures UI state work correctly. 👍

---

## 📸 Screenshots

<div align="center">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/1.jpeg" width="250">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/2.jpeg" width="250">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/3.jpeg" width="250">
</div>

<div align="center">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/4.jpeg" width="250">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/5.jpeg" width="250">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/6.jpeg" width="250">
</div>

<div align="center">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/7.jpeg" width="250">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/8.jpeg" width="250">
  <img src="https://raw.githubusercontent.com/firdausmaulan/Fake-Store/refs/heads/master/screenshot/9.jpeg" width="250">
</div>

---
