package com.livrotech.mapper;

import com.livrotech.dto.SaleResponseDTO;
import com.livrotech.entity.Sale;

public final class SaleMapper {

    private SaleMapper() {
    }

    public static SaleResponseDTO toResponse(Sale sale) {
        return new SaleResponseDTO(
                sale.getId(),
                sale.getCustomer().getId(),
                sale.getCustomer().getCpf(),
                sale.getEmployee().getId(),
                sale.getBook().getId(),
                sale.getSaleDate()
        );
    }
}
