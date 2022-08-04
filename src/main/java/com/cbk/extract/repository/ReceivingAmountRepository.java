package com.cbk.extract.repository;

import com.cbk.extract.entity.ReceivingAmount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivingAmountRepository extends JpaRepository<ReceivingAmount, Long> {
}
