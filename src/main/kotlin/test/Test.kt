package test

import kotlinx.serialization.Serializable

inline fun <reified T> Any.cast() = this as T

fun main() {
  val moneyRange = Money(1)..Money(2)

  // not ok, StackOverflow at compilation
  moneyRange.cast<MoneyRange>()
  // ok
  moneyRange.cast<ClosedRange<Money>>()
  // ok
  moneyRange as MoneyRange
}

@Serializable
@JvmInline
value class Money(val amount: Int): Comparable<Money> {
  override fun compareTo(other: Money): Int = amount.compareTo(other.amount)
}

typealias MoneyRange =
  @Serializable(with = MoneyRangeSerializer::class)
  ClosedRange<Money>
