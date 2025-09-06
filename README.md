# GitHub User Search App - HCS-IDN Android Assessment

An Android application to search GitHub users, view user lists, and see detailed profiles. Built as an assessment task for HCS-IDN.

🔗 **Live Repository**: [https://github.com/Abifarhan/MyUserGithubHCS](https://github.com/Abifarhan/MyUserGithubHCS)

---

## 📱 Key Features

- 🔍 **Search Screen**: Real-time search for GitHub users
- 👥 **User List Screen**: Display users with avatar and username
- 📄 **User Detail Screen**: View bio, repositories, followers, and following
- 💾 **Local Persistence**: Data stored locally using **Room Database**
- 🔄 **Background Sync**: Notification to open the app every 15 minutes using **WorkManager**
- 🐞 **Debugging**: Integrated **Chucker** for network logging
- 🧪 **Testing**: Unit and UI tests using **JUnit, MockK, and Espresso**

---

## 🛠️ Technologies & Tools

| Category | Technology |
|--------|-----------|
| Language | Kotlin |
| Architecture | Clean Architecture (MVVM) |
| DI | Hilt |
| Networking | Retrofit, OkHttp, Moshi |
| Persistence | Room Database |
| Image Loading | Glide |
| Concurrency | Kotlin Coroutines |
| Testing | JUnit, MockK, Espresso |
| Debugging | Chucker |
| Dependency Management | Version Catalog (`libs.versions.toml`) |
| Background | WorkManager |

---

## 📁 Project Structure

- `:app` – Presentation layer: Activities, Fragments, ViewModels
- `:domain` – Domain layer: UseCases, business logic
- `:data` – Data layer: Repository, API, Room, Data Sources

---

## 🧪 Testing

This app includes solid test coverage:

- ✅ **Unit Tests**: All UseCases, Repository, and ViewModels are tested
- ✅ **UI Tests**: Key screens tested with Espresso
- ✅ **Mocking**: `MockK` used to isolate business logic

Run tests:
```bash
./gradlew :app:testDebugUnitTest    # Unit tests
./gradlew connectedDebugAndroidTest # UI tests
```

🚀 How to Run

1. Clone the repository:
```bash
git clone https://github.com/Abifarhan/MyUserGithubHCS.git
```

1. Open in Android Studio 

2. Build & Run 
   Select an emulator or physical device
   Click Run (▶️)
         

3. Using the app:
   - On the home screen, type a username in the SearchView
   - Click a user to view their profile

### 🧩 Additional Features (Nice-to-Have)

- ✅ **Chucker** – Debug network requests and responses
- ✅ **Version Catalog** – Clean dependency version management
- ✅ **WorkManager** – Background sync every 15 minutes
- ✅ **Moshi** – JSON parsing from GitHub API
- ✅ **UI Testing** – Verified user interactions

📝 **Chosen Architecture: MVVM + Clean Architecture**

**Why?**

- MVVM works seamlessly with LiveData and ViewModel, separating UI from business logic.
- Clean Architecture ensures modular, testable, and maintainable code.
- Separated layers (presentation, domain, data) enable better dependency injection and testing.

📬 **Contact**

For any questions or clarifications, feel free to reach out:

- 📧 **Email**: [abifarhan04@gmail.com](mailto:abifarhan04@gmail.com)
- 💼 **LinkedIn**: [linkedin.com/in/abifarhan](https://linkedin.com/in/abifarhan)
- 🐙 **GitHub**: [@Abifarhan](https://github.com/Abifarhan)

💡 Reflection: Challenges & Improvements
🧩 Challenges

1. Unit Testing with Hilt & MockK:
    Unit testing using mocks (via MockK) helped improve efficiency and precision in isolating business logic. However, integrating Hilt for dependency injection in tests required careful setup to replace real dependencies with mocks while maintaining test reliability. 

2. WorkManager for Periodic Notifications:
    Scheduling notifications every 15 minutes using PeriodicWorkRequest faced limitations due to Android’s system-level background execution optimizations. WorkManager does not guarantee exact timing.
    Solution: Use setInitialDelay() to control the first run and apply ExistingPeriodicWorkPolicy.REPLACE to prevent duplicate workers and ensure only one instance runs at a time. 



🛠 Improvements

1. Focus on Effective TDD:
    Prioritize writing unit tests for UseCase and Repository layers before UI integration. This ensures business logic is correct and robust from the start. 

2. Apply Clean Code & Separation of Concerns:
    Clearly separate data, domain, and presentation layers. This makes the codebase more maintainable, readable, and easier to test in isolation. 

3. Use MockK Minimally and Wisely:
    Only mock critical external dependencies (e.g., APIs, databases). Avoid over-mocking, which can obscure real behavior and make tests harder to understand and maintain. 



📚 Lessons Learned

1. TDD is About Quality, Not Quantity:
    One well-written test that validates core business logic is more valuable than ten trivial tests checking only getters or basic setters. 

2. Clean Architecture Enables Better Testing:
    With properly separated layers, UseCase classes can be tested independently—without needing UI components, database, or network. 

3. Avoid Over-Engineering:
    Favor solutions that are working, simple, and easy to understand over complex, clever implementations. Simplicity leads to better long-term maintainability. 
     