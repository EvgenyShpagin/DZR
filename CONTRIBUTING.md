# Contributing to DZR

This document outlines the development standards and guidelines for the DZR project. Please follow
these rules to ensure consistency and maintainability across the codebase.

## 🎨 Compose Preview Guidelines

We strictly separate the purpose of Previews in the `main` source set versus the `screenshotTest`
source set to keep our development environment fast and our regression testing robust.

### 1. Responsibility Separation

| Source Set           | Purpose                                                                            | Scope                                                                                                                                |
|:---------------------|:-----------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------|
| **`main`**           | **Dev / Visual Check**<br>Quick verification during development in Android Studio. | **Minimal**<br>1-2 previews per component.<br>Standard "Happy Path" state.<br>Light/Dark theme check.                                |
| **`screenshotTest`** | **QA / Regression**<br>Automated visual regression testing and edge-case coverage. | **Exhaustive**<br>All states (Loading, Error, Empty).<br>All variants (Font Scales, Screen Sizes).<br>Edge cases (Long text, Nulls). |

### 2. Naming Convention

All Previews must follow a strict naming pattern to ensure test logs are readable and searchable.

**For `main` source set:**

**Pattern:** `ComponentNamePreview` or `ComponentNameOptionPreview`

* **ComponentName**: The PascalCase name of the Composable (e.g., `Track`, `PlayableHeader`).
* **Option** (Optional): Specific configuration or variant (e.g., `WithSubtitle`, `LongTitle`).

**For `screenshotTest` source set:**

**Pattern:** `[ComponentName]_[State]_[Variant]_Preview`

* **ComponentName**: The PascalCase name of the Composable (e.g., `Track`, `PlayableHeader`).
* **State** (Optional): The logical state being shown.
    * Use specific states like `Enabled`, `Disabled`, `Loading`, `Error`, `Empty` when applicable.
    * Omit state if there is only a single preview or if the component is stateless (e.g.
      DzrNavigationBar, SectionHeader).
* **Variant** (Optional): Specific configuration or edge case (e.g., `Dark`, `FontScale150`,
  `LongText`, `NoCover`).

**Examples:**

* `Button_Enabled_Icon_Preview`
* `Track_LongTitle_Preview`
* `PlayableHeader_SmallScreen_Preview`

### 3. Component Strategy

| Source Set           | Purpose                                                                            | Scope                                                                                                                                |
|:---------------------|:-----------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------|
| **`main`**           | **Dev / Visual Check**<br>Quick verification during development in Android Studio. | **Minimal**<br>1-2 previews per component.<br>Standard "Happy Path" state.<br>Light/Dark theme check.                                |
| **`screenshotTest`** | **QA / Regression**<br>Automated visual regression testing and edge-case coverage. | **Exhaustive**<br>All states (Loading, Error, Empty).<br>All variants (Font Scales, Screen Sizes).<br>Edge cases (Long text, Nulls). |

### 4. Technical Checklist

* **Theme Wrapper:** Always wrap Previews in `DzrTheme`.
* **No Name Param:** Do not use the `name` parameter in `@Preview`. The function name itself should
  be descriptive enough following the convention.
* **Annotations:**
    * Use `@PreviewLightDark` for all `main` source set previews.
    * **MUST** use `@PreviewTest` in `screenshotTest` (required for the screenshot tool).
* **Backgrounds:**
    * **Transparent Components:** If a component is transparent or relies on a background for
      contrast (e.g., `Track`, `Icon`, `OutlinedButton`), **MUST** wrap it in a `Surface` in
      both source sets to ensure visibility in Dark Mode and Disabled state.

### 5. Optimization & Best Practices

Based on official Android recommendations:

* **Avoid Combinatorial Explosion:** Do not test every combination of properties if they are
  independent.
    * *Bad:* Button x (Light/Dark) x (Small/Medium/Large) x (FontScale 1.0/1.5) = 12 screenshots.
    * *Good:* Button x (Light/Dark) + Button x (Small/Medium/Large) + Button x (FontScale 1.5) = 6
      screenshots.
    * *Why:* A button's layout reaction to large text is usually the same regardless of the color
      theme. Test properties in isolation when possible.
    * **Solution:** Use **`@ThemeAndFontScalePreviews`** (defined in `debug` source set of
      `core:design-system`).
      This single annotation automatically generates the necessary isolated snapshots.
      It is available in `debug` builds (including `screenshotTest`).
* **Platform Consistency:** Text rendering and shadows vary significantly between OSs (
  Windows/Mac/Linux).
    * Generate your "Goldens" (reference images) in the same environment where you run validation (
      e.g., CI/Linux).
    * If running locally, expect small diffs if your OS differs from CI.
