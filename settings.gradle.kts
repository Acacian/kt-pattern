/*
 * kt-pattern: Kotlin 패턴 매칭 DSL 라이브러리
 * 
 * 멀티모듈 구조:
 * - dsl-core     : 핵심 패턴 정의 및 구조
 * - dsl-runtime  : 런타임 evaluator, snapshot 바인딩 등
 * - dsl-dsl      : DSL 진입점 (match, case 등)
 * - dsl-test     : 공용 테스트 유틸
 * - examples     : 사용 예시 및 실행 가능한 샘플
 */

rootProject.name = "kt-pattern"

include(
    "dsl-core",
    "dsl-runtime",
    "dsl-dsl",
    "dsl-test",
    "examples"
)