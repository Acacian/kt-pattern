package com.ktpattern.patternmatch

sealed class Command
data class Login(val username: String, val password: String) : Command()
data class Logout(val userId: Int) : Command()
data class Purchase(val item: String, val amount: Int) : Command()
data class Cancel(val orderId: String) : Command()

class StartsWithAdmin : SimplePattern<String>(String::class.java) {
    override fun matches(value: String): Boolean = value.startsWith("admin")
}

fun main() {
    val binder = SnapshotBinder()

    val inputs = listOf<Any>(
        Login("admin_user", "1234"),
        Login("guest", "1234"),
        Logout(42),
        Purchase("keyboard", 3),
        Purchase("gpu", 999),
        Cancel("ORD-99192"),
        "admin123",
        "hello world",
        777
    )

    for (input in inputs) {
        val result = match(input, snapshotBinder = binder) {
            // 🎯 관리자 로그인: Login + 조건
            caseOf<Login>({ it.username.startsWith("admin") }) {
                "👮 관리자 로그인: ${it.username}"
            }

            // 💸 고가 GPU 구매 감지: 구조 분해
            case(DestructurePattern(Purchase::class.java) { (item, amount): Purchase ->
                item == "gpu" && amount > 500
            }) {
                val purchase = it
                "💸 고가 GPU 구매 시도: ${purchase.amount}만원"
            }

            // 🧾 정확한 문자열
            whenValue("hello world") { "🧾 정확한 문자열 매칭" }

            // 🎯 문자열 커스텀 패턴
            case(StartsWithAdmin()) { "🎯 문자열이 'admin'으로 시작함: $it" }

            // ✅ 기본 타입 기반 DSL
            whenType<Logout> { "👋 로그아웃 요청: userId=${it.userId}" }
            whenType<Cancel> { "🚫 주문 취소: ${it.orderId}" }

            // 🤷 fallback
            else_ { "🤷 처리 불가: $it" }
        }

        println("▶ 입력: $input → 결과: $result")
    }

    println("\n📋 Snapshot 로그")
    binder.getAll().forEachIndexed { i, snap ->
        println("[$i] ${snap.status} | ${snap.value} | ${snap.pattern}")
    }
}
