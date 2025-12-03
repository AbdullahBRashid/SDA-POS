package com.DullPointers.model;

import com.DullPointers.model.enums.PaymentMethod;

import java.math.BigDecimal;

public interface IPayment {
    BigDecimal getAmount();

    PaymentMethod getMethod();
}
