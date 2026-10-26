# Smart Pantry Manager

An Android app (Java, Android Studio) that lets users keep track of the ingredients in
their pantry and suggests recipes they can make with what they have, using strict matching.

## Features

| Feature | Status                                |
|---|---------------------------------------|
| Pantry list (add, edit, delete ingredients with quantity, unit and optional expiry date) | Done (in-memory data)                 |
| Suggested recipes screen with empty-state message | Done (placeholder name-only matching) |
| Recipe detail screen (ingredients and method) | Done (4 sample recipes)               |
| Settings screen (expiring-soon alerts, units) | Done (values saved, not yet used)     |
| Room database and 15-20 seeded recipes | Done                                  |
| Strict matching rules (quantities, units, expiry) | Planned                               |

## Tech

- Java, Android Studio
- Material Components (Material 3), RecyclerView, bottom navigation
- ViewModel + LiveData, repository pattern
- Room database (planned)

## Project structure

```
app/src/main/java/com/example/smartpantry/
  data/        models, repositories (database package added later)
  domain/      RecipeMatcher (matching logic, no Android classes)
  ui/          main, pantry, recipes, settings screens
  util/        constants and helpers
```

## Getting started

1. Clone the repository:
   `git clone https://github.com/Lg12-prog/SmartPantry.git`
2. Open the folder in Android Studio and let Gradle sync.
3. Run the `app` configuration on an emulator or a connected phone.

## Branching and commits

- `main` always builds and runs.
- New work happens on a branch (`feature/database`, `feature/strict-matching`, `fix/...`)
  and is merged into `main` with a pull request.
- Commit messages: short, imperative, e.g. `Add Room database for pantry items`.
