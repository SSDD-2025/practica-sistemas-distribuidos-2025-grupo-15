package com.example.demo.dto;

import org.mapstruct.Mapper;

import com.example.demo.model.Purchase;

import java.util.List;
import java.util.Collection;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {
    PurchaseDTO toDTO(Purchase purchase);
    List<PurchaseDTO> toDTOs(Collection<Purchase> purchases);
    Purchase toDomain(PurchaseDTO purchaseDTO);
}