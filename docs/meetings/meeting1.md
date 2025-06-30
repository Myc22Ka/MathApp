# 📋 Spotkanie z doktorem Dariuszem Myszorem nr 1 Duolingo dla matematyków
*Data: 30.06.2025*

---

## 🎯 Tematy do omówienia

### 1. 🛠️ Stack technologiczny

| Komponent          | Technologia |
|--------------------|-------------|
| **Backend**        | Java SpringBoot |
| **Frontend**       | JavaScript (TypeScript), prawdopodobnie React |
| **Baza danych**    | PostgreSQL |
| **AI?**            | Python |
| **Konteneryzacja** | Docker, docker-compose |
| **Kontrola wersji** | GitHub |

---

### 2. 📚 Czym jest biblioteka Symja?

> **Symja** (skrót od *Symbolic Java*) to otwartoźródłowa biblioteka matematyczna napisana w języku Java, służąca do symbolicznego i numerycznego przetwarzania wyrażeń matematycznych.

---

### 3. 🎲 Proces generowania zadań

Generowanie zadań będzie **złożonym wieloetapowym procesem**. Aby wygenerować zadanie, potrzebne będą następujące komponenty:

#### 📝 Wymagane elementy:

1. **Model wyrażenia matematycznego**
    - Przetwarzanie napisów
    - Wykonywanie operacji matematycznych

2. **Testy**
    - Armada testów dla każdego modelu
    - Zapewnienie poprawności implementacji

3. **Parser**
    - Zrozumienie napisu użytkownika
    - Konwersja na odpowiedni obiekt modelu

✅ Status: Tutaj jestem

4. **Generator wyrażeń**
    - Tworzenie losowych/pseudolosowych wyrażeń matematycznych
    - Generowanie zgodne z modelem

5. **Wymagania dla generatora**
    - Możliwość podania warunków dla wygenerowanego napisu
    - Przykład: funkcja bez miejsc zerowych

6. **Tekst zadania**
    - Przypisane kroki rozwiązania
    - Instrukcje dla użytkownika

7. **Mechanizm odczytywania kroków**
    - Interpretacja kroków zadania
    - Wykonanie odpowiednich funkcji backendu

8. **System odpowiedzi**
    - Podana odpowiedź lub zbiór odpowiedzi
    - Weryfikacja rozwiązań

9. **Testy końcowe**
    - Sprawdzenie sensowności zadania
    - Weryfikacja oczekiwanych wyników

---

### 4. 📊 Aktualny stan projektu

> **Status:** Faza tworzenia zadania - finalizacja modelu zbiorów

**Obecne prace:**
- ✅ Kończenie modelu zbiorów (część modelu funkcji)
- 🔄 Implementacja działań na zbiorach (ręczna - Symja ma ograniczenia)
- ⏳ **Następny krok:** Generowanie losowego zbioru

*Dużo wyników matematycznych jest w formie zbiorów, więc ten model jest kluczowy.*

---

### 5. 🎯 Planowane modele

**Cel:** `6 konkretnych modeli`

#### 📋 Lista modeli:
- 📈 **Model funkcji**
- ⚖️ **Model równań**
- 🔢 **Model ciągów**
- 📐 **Model geometrii analitycznej** *(najważniejszy - idealnie pasuje do mechanizmu kroków)*
- 🧮 **Model zbiorów** *(w trakcie)*
- ❓ **Model dodatkowy** *(w zależności od czasu i złożoności)*

> **Uwaga:** Liczba modeli zależy od biblioteki oraz złożoności innych komponentów aplikacji.

---

### 6. 🧠 Strategie rozwiązywania zadań

#### **Opcja 1: Podstawowa** ⚡
- Porównanie rozwiązania własnego z rozwiązaniem użytkownika
- Zwrócenie wyniku

#### **Opcja 2: Zaawansowana** 🎨
- Znalezienie API frontendu z funkcją tablicy
- Wyznaczenie miejsca na odpowiedź
- Odczytanie i porównanie

**Zalety opcji 2:**
- 💾 Zbieranie tablic do bazy danych
- 👤 Przechowywanie dla użytkownika
- 🎯 Centralizacja operacji matematycznych

---

### 7. 🤖 Rola AI w projekcie

**Pytanie:** Jak rozumieć wzmiankę o `OPCJONALNYM BOCIE AI`?
- Opcjonalny dla użytkownika? 🤔
- Opcjonalny w implementacji? 🤔

#### **Rozważane zastosowania:**
- Generowanie tekstu zadań
- Tworzenie losowych napisów

#### ⚖️ **Analiza pros & cons:**

| ✅ **Plusy** | ❌ **Minusy** |
|-------------|---------------|
| Szybkość implementacji | Połączenie Python + Java |
| Mało kodu | Nieprzewidywalność na produkcji |
| Niska złożoność | Wolniejsze przetwarzanie |
| Kontrola nad generowaniem | Utrata kontroli nad złożonością |

---

### 8. ⚖️ Kwestie prawne i współpraca

**Pytanie:** Czy można jawnie wymienić współpracowników?

**Planowana pomoc:**
- 🧪 **Osoba do testów** - zadeklarowała się
- 🎨 **Siostra** - pomoc w projektowaniu frontendu

> Czy takie osoby mogą otrzymać jawną wzmiankę o pomocy, czy należy to ukrywać?

---

### 9. 🏖️ Plan wakacyjny

#### 🎯 **Główne cele:**

1. **📝 Konkretne zadanie matematyczne**
    - Pełna implementacja jednego typu zadania

2. **👤 System użytkowników**
    - Autoryzacja i zarządzanie kontami

3. **🐳 Konfiguracja projektu**
    - Docker i infrastruktura

4. **🎨 Projekt frontendu**
    - Mockupy w Figma

---

## 🎯 **Podsumowanie**
*Projekt ambitny, ale dobrze przemyślany. Kluczowe będzie zarządzanie zakresem i utrzymanie motywacji przez cały proces implementacji.*