# 🛠 Workspace & Development Rules

Даний проєкт базується на принципах **Clean Code** та суворій типізації Java. Дотримання цих правил є обов'язковим для всіх контриб'юторів.

### 1. ☕ Java Code Style
- **Formatting**: Використовуємо Google Java Style. 4 пробіли для відступу, ніяких Tab.
- **Line Length**: Максимальна довжина рядка — 120 символів.
- **Naming**:
  - Класи: `PascalCase` (іменники, напр. `UserRegistry`).
  - Методи/Змінні: `camelCase` (дієслова для методів, напр. `processOrder`).
  - Константи: `UPPER_SNAKE_CASE`.
- **Imports**: Заборонено використовувати wildcards (`import *`). Тільки прямі імпорти.

### 2. ✨ Clean Code & Architecture
- **Single Responsibility (SRP)**: Один клас — одна відповідальність. Один метод — одна дія.
- **Method Size**: Намагайтеся тримати методи до 20-25 рядків.
- **Arguments**: Максимум 3 аргументи на метод. Якщо потрібно більше — використовуйте об'єкт-параметр або Builder.
- **No Nulls**: Повертайте `Optional<T>` замість `null`. Використовуйте `@NonNull` анотації для параметрів.
- **Don't Repeat Yourself (DRY)**: Виносьте логіку, що дублюється, у приватні методи або утиліти.

### 3. 🛡 Error Handling
- **Exceptions**: Не "ковтайте" помилки. Порожній блок `catch` заборонений.
- **Checked Exceptions**: Мінімізуйте використання checked exceptions на користь runtime exceptions.
- **Validation**: Перевіряйте стан об'єктів на вході (fail-fast principle).
