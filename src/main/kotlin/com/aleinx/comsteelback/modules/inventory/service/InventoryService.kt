package com.aleinx.comsteelback.modules.inventory.service

import com.aleinx.comsteelback.common.enums.MovementType
import com.aleinx.comsteelback.modules.auth.repository.UserRepository
import com.aleinx.comsteelback.modules.catalog.repository.ProductRepository
import com.aleinx.comsteelback.modules.inventory.dto.StockEntryRequest
import com.aleinx.comsteelback.modules.inventory.model.StockMovement
import com.aleinx.comsteelback.modules.inventory.model.StockMovementDetail
import com.aleinx.comsteelback.modules.inventory.repository.StockMovementRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class InventoryService(
    private val movementRepository: StockMovementRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun registerEntry(request: StockEntryRequest, username: String): String {
        val user = userRepository.findByUsername(username)
            .orElseThrow { RuntimeException("Usuario no encontrado") }

        // 1. Crear Cabecera
        val movement = StockMovement(
            type = MovementType.ENTRY,
            reference = request.reference,
            comments = request.comments,
            user = user
        )

        // 2. Procesar Detalles
        request.items.forEach { item ->
            val product = productRepository.findById(item.productId)
                .orElseThrow { RuntimeException("Producto ID ${item.productId} no encontrado") }

            // --- LÓGICA DE COSTO PROMEDIO PONDERADO (CPP) ---
            // NuevoCosto = ((StockActual * CostoActual) + (Ingreso * CostoIngreso)) / (StockActual + Ingreso)
            // Nota: Usamos el 'unitPrice' del producto como referencia de su valor actual.
            val currentTotalValue = product.unitPrice.multiply(BigDecimal(product.stockQuantity))
            val incomingTotalValue = item.unitCost.multiply(BigDecimal(item.quantity))

            val newTotalStock = product.stockQuantity + item.quantity

            // Recalcular precio unitario (opcional, si quieres actualizar precios automáticamente)
            // Si prefieres NO cambiar el precio de venta automáticamente, comenta las siguientes 2 líneas:
            // val newWeightedPrice = currentTotalValue.add(incomingTotalValue)
            //     .divide(BigDecimal(newTotalStock), 2, RoundingMode.HALF_UP)
            // product.unitPrice = newWeightedPrice

            // 3. Actualizar Stock
            product.stockQuantity = newTotalStock
            productRepository.save(product)

            // 4. Guardar Detalle
            movement.addDetail(
                StockMovementDetail(
                    product = product,
                    quantity = item.quantity,
                    unitCost = item.unitCost
                )
            )
        }

        movementRepository.save(movement)
        return "Ingreso registrado correctamente. ID Movimiento: ${movement.id}"
    }
}