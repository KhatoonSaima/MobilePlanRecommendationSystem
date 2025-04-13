# 📱 Mobile Plan Recommendation System

This is a **Java Swing-based desktop GUI application** that helps users find the best mobile plans based on their preferences. It includes multiple features such as **plan recommendation**, **spell checking**, **word completion**, **page ranking**, and a **customer support form** — all integrated into a clean, tabbed interface.

---

## 🛠 Features

- **Home Tab**: Choose the best recommendation based on data usage or price.
- **Search Plan**: Search for mobile plans using keywords.
- **Features Tab**:
  - 🔤 **Word Completion**: Suggests completions for partially typed words.
  - 📝 **Spell Checking**: Detects and corrects misspelled words.
  - 📈 **Page Ranking**: Simulates ranking for given search queries.
- **Support Tab**: Submit a complaint or inquiry form with your email and mobile number.

---

## 📁 Project Structure

The codebase follows a modular structure for clarity and ease of maintenance. All Java classes can be placed in the same directory.

| Class File           | Description                                                              |
|----------------------|--------------------------------------------------------------------------|
| `Main.java`          | Entry point for the app; sets up the main application frame              |
| `HomePanel.java`     | Handles the logic and UI for plan recommendation                         |
| `SearchPanel.java`   | Implements keyword-based plan search                                     |
| `FeaturesPanel.java` | Switches between word completion, spell check, and page ranking views    |
| `SupportPanel.java`  | UI form for submitting user support/complaint requests                   |

---

## 🧑‍💻 How to Run

### 🔸 Option 1: Using Terminal (Command Line)

1. Open terminal in the project directory.
2. Compile all Java files:
   ```bash
   javac *.java
   ```
3. Run the application:
   ```bash
   java Main
   ```

### 🔸 Option 2: Using Visual Studio Code

1. Open the project folder in **VS Code**.
2. Ensure the **Java Extension Pack** is installed.
3. Open `Main.java` and click **Run**, or press `Ctrl + F5` to run without debugging.

### 🔸 Option 3: Using Eclipse IDE

1. Open **Eclipse** and go to `File > Import > General > Existing Projects into Workspace`.
2. Select the project folder and click **Finish**.
3. Open `Main.java`, right-click, and choose **Run As > Java Application**.

### 🔸 Option 4: Using IntelliJ IDEA

1. Open **IntelliJ IDEA** and select `Open` to load the project folder.
2. Make sure your JDK is set up in the Project Structure.
3. Right-click `Main.java` and select **Run 'Main'**.

---

## 📌 Note

Ensure you have **JDK 8 or higher** installed and configured in your system PATH.
