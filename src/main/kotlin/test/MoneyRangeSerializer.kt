package test

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

/**
 * Serializer for a [MoneyRange].
 */
object MoneyRangeSerializer: KSerializer<MoneyRange> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("kotlin.ranges.MoneyClosedRange") {
    element<String>("start")
    element<String>("endInclusive")
  }

  override fun serialize(encoder: Encoder, value: MoneyRange) {
    encoder.encodeStructure(descriptor) {
      encodeIntElement(descriptor, 0, value.start.amount)
      encodeIntElement(descriptor, 1, value.endInclusive.amount)
    }
  }

  override fun deserialize(decoder: Decoder): MoneyRange {
    decoder.decodeStructure(descriptor) {
      var start: Money? = null
      var end: Money? = null
      while (true) {
        when(val index = decodeElementIndex(descriptor)) {
          CompositeDecoder.DECODE_DONE -> break
          0 -> start = Money(decodeIntElement(descriptor, index))
          else -> end = Money(decodeIntElement(descriptor, index))
        }
      }
      if (start == null || end == null) throw SerializationException("Both ends of Money closed range must be provided")
      return start..end
    }
  }
}
