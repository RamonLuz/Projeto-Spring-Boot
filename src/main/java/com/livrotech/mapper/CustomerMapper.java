package com.livrotech.mapper;

import java.util.List;

import com.livrotech.dto.BookResponseDTO;
import com.livrotech.dto.CustomerResponseDTO;
import com.livrotech.entity.Customer;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponseDTO toResponse(Customer customer) {
        List<BookResponseDTO> purchases = customer.getPurchases() == null
                ? List.of()
                : customer.getPurchases().stream()
                        .map(BookMapper::toResponse)
                        .toList();

        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getStatus(),
                purchases
        );
    }
}
