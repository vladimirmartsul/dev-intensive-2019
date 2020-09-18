package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*


const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


enum class TimeUnits {
    SECOND, MINUTE, HOUR, DAY;

    private fun pl(
        n: Int,
        zero: String,
        one: String,
        few: String,
        many: String
    ): String {
        return "$n " + if (n == 0) {
            zero
        }
        else {
            if (n % 10 == 1 && n % 100 != 11) {
                one
            }
            else {
                if (n % 10 in 2..4 && (n % 100 < 10 || n % 100 >= 20)) {
                    few
                }
                else {
                    many
                }
            }
        }
    }


    fun plural(value: Int = 0): String {
        return when (this) {
            SECOND -> pl(value, "секунд", "секунду", "секунды", "секунд")
            MINUTE -> pl(value, "минут", "минуту", "минуты", "минут")
            HOUR -> pl(value, "часов", "час", "часа", "часов")
            DAY -> pl(value, "дней", "день", "дня", "дней")
        }
    }
}


fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String = SimpleDateFormat(pattern, Locale("ru")).format(this)


fun Date.add(
    value: Int,
    units: TimeUnits = TimeUnits.SECOND
): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time

    return this
}


fun Date.humanizeDiff(date: Date = Date()): String {
    var diff = date.time - this.time

    return if (diff < 0) {
        diff = -diff
        when {
            diff > (360 * DAY) -> "более чем через год"
            diff > (26 * HOUR) -> "через ${TimeUnits.DAY.plural((diff / DAY).toInt())}"
            diff > (22 * HOUR) -> "через день"
            diff > (75 * MINUTE) -> "через ${TimeUnits.HOUR.plural((diff / HOUR).toInt())}"
            diff > (45 * MINUTE) -> "через час"
            diff > (75 * SECOND) -> "через ${TimeUnits.MINUTE.plural((diff / MINUTE).toInt())}"
            diff > (45 * SECOND) -> "через минуту"
            diff > (1 * SECOND) -> "через несколько секунд"
            else -> "только что"
        }
    }
    else {
        when {
            diff > (360 * DAY) -> "более года назад"
            diff > (26 * HOUR) -> "${TimeUnits.DAY.plural((diff / DAY).toInt())} назад"
            diff > (22 * HOUR) -> "день назад"
            diff > (75 * MINUTE) -> "${TimeUnits.HOUR.plural((diff / HOUR).toInt())} назад"
            diff > (45 * MINUTE) -> "час назад"
            diff > (75 * SECOND) -> "${TimeUnits.MINUTE.plural((diff / MINUTE).toInt())} минут назад"
            diff > (45 * SECOND) -> "минуту назад"
            diff > (1 * SECOND) -> "несколько секунд назад"
            else -> "только что"
        }
    }
}
