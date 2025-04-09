# 🔍 Kt-pattern - Kotlin Pattern Matching DSL

Kotlin에서 구조적 분기와 타입 기반 조건을 더 선언적이고 직관적으로 다룰 수 있는 패턴 매칭 DSL입니다.

표현력이 제한적인 when 문을 보완하고자 했으며, 현재 Kotlin 생태계에 마땅한 대안이 없어 직접 구현하게 되었습니다.

---

## ✨ Motivation

📌 왜 이 DSL이 필요한가?

Kotlin은 기본적으로 when 문을 통해 정적 타입 기반 분기는 지원하지만, 다음과 같은 한계가 있습니다:

런타임 타입 기반 분기를 깔끔하게 처리하기 어렵고,

값 기반 매칭이나 **복합 조건(type + predicate)**을 표현하기 위해선 is, ==, &&, if 등을 얽어 써야 하며,

sealed class를 사용해도 중첩 분기와 복잡한 조건문으로 인해 가독성이 떨어지기 쉽습니다.

즉, Kotlin의 when 문은 표현력이 부족한 경우가 많습니다.
예를 들어:

- 타입과 값, 구조를 동시에 매칭하고 싶을 때

- 복잡한 조건문을 선언적으로 분기하고 싶을 때

- 가독성과 유지보수가 중요한 DSL 구조가 필요할 때

---

## 🛠 기술 스택

- Kotlin 1.9.x
- Gradle Kotlin DSL
- JUnit5 (테스트)

---

## Installation

To use `Kt-pattern` in your project, add the following dependency to your `build.gradle`:

```gradle
dependencies {
    implementation "com.ktpattern:kt-pattern:1.0.0"
}
```

---

## 🚀 Features

- [ ] 타입 기반 매칭 (Type Matching)
- [ ] 값 기반 매칭 (Value Matching)
- [ ] 구조 분해 기반 매칭 (Destructuring Matching)
- [ ] 조건부 매칭 (`when` + predicate DSL)
- [ ] 스마트 캐스트 연계 (Smart-cast awareness)
- [ ] Sealed class 지원
- [ ] 커스텀 패턴 정의

---

## 🧠 설계 철학 - 3단계 분리 구조

1️⃣ 구조 분리  
→ `dsl-core`, `dsl-runtime`, `dsl-dsl` 등 책임 단위로 나눈 멀티모듈 구성

2️⃣ 실행 분리  
→ 실행 로직은 `dsl-dsl` 모듈에서 DSL 진입점 역할을 수행하며, 사용자 정의 패턴을 통해 직접 실행 가능

3️⃣ 런타임 교체 가능  
→ `PatternEvaluator`, `SnapshotBinder` 등 interface 기반 추상화로,  
 다양한 evaluator를 주입하거나 교체할 수 있도록 설계됨

---

## 🧩 멀티모듈 구성

| 모듈명        | 설명                                                                  |
| ------------- | --------------------------------------------------------------------- |
| `dsl-core`    | 핵심 구조 및 패턴 정의 (`Pattern`, `MatchResult`, `PatternEvaluator`) |
| `dsl-runtime` | 평가 로직 구현 (`DefaultPatternEvaluator`, snapshot 등)               |
| `dsl-dsl`     | 사용자 DSL 호출부 (`match`, `case`, `MatchBuilder`)                   |
| `dsl-test`    | 테스트 유틸 및 도구 (향후 확장 예정)                                  |

---

## 🧱 Architecture Overview

사용자 DSL 호출  
▼  
**MatchBuilder** (사용자 정의 패턴 + 액션 수집)  
▼  
**MatchContext** (패턴 목록 + 입력값 바인딩)  
▼  
**PatternEvaluator** (재귀적 평가 로직)  
├── TypePattern  
├── ValuePattern  
├── DestructurePattern  
└── PredicateCondition  
▼  
**MatchResult** (Success / Failure / Bindings)

---

```mermaid
graph TD
    User["👤 DSL 호출"]
    MB["🧱 MatchBuilder"]
    CTX["🌿 MatchContext"]
    EVAL["⚙️ PatternEvaluator"]
    TP["🔤 TypePattern"]
    VP["🔢 ValuePattern"]
    DP["📦 DestructurePattern"]
    PC["📃 PredicateCondition"]
    MR["✅ MatchResult"]

    User --> MB --> CTX --> EVAL
    EVAL --> TP
    EVAL --> VP
    EVAL --> DP
    EVAL --> PC
    EVAL --> MR
```
