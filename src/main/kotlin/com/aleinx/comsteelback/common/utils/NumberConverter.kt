package com.aleinx.comsteelback.common.utils

import java.math.BigDecimal

object NumberConverter {

    private val UNIDADES = arrayOf("", "UN ", "DOS ", "TRES ", "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ")
    private val DECENAS = arrayOf("DIEZ ", "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS ", "DIECISIETE ", "DIECIOCHO ", "DIECINUEVE ", "VEINTE ", "TREINTA ", "CUARENTA ", "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ")
    private val CENTENAS = arrayOf("", "CIENTO ", "DOSCIENTOS ", "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ", "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS ")

    fun convert(amount: BigDecimal): String {
        // Separar parte entera y decimal
        val integerPart = amount.toBigInteger().toLong()
        val decimalPart = amount.remainder(BigDecimal.ONE).movePointRight(2).toInt()

        val text = if (integerPart == 0L) "CERO " else convertNumber(integerPart)

        // Formato estándar SUNAT: "SON: [TEXTO] CON [XX]/100 DOLARES"
        return "SON: ${text}CON ${String.format("%02d", decimalPart)}/100 DOLARES"
    }

    private fun convertNumber(n: Long): String {
        return when {
            n >= 1000000 -> "UN MILLON " + convertNumber(n % 1000000) // Simplificado para < 2 millones
            n >= 1000 -> {
                val miles = n / 1000
                val resto = n % 1000
                val milesText = if (miles == 1L) "MIL " else convertNumber(miles) + "MIL "
                milesText + convertNumber(resto)
            }
            n >= 100 -> {
                if (n == 100L) "CIEN " else CENTENAS[(n / 100).toInt()] + convertNumber(n % 100)
            }
            n >= 30 -> DECENAS[(n / 10).toInt() - 2 + 10] + if (n % 10 > 0) "Y " + convertNumber(n % 10) else "" // 30, 40...
            n >= 20 -> if (n == 20L) "VEINTE " else "VEINTI" + convertNumber(n % 10)
            n >= 10 -> DECENAS[(n - 10).toInt()]
            else -> UNIDADES[n.toInt()]
        }
    }
}