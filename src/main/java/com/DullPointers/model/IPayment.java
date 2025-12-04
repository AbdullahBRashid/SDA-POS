package com.DullPointers.model;

import com.DullPointers.model.enums.PaymentMethod;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

//@JsonSerialize(as = IPayment.class)
//@JsonDeserialize(as = IPayment.class)
public interface IPayment {
    BigDecimal getAmount();

    PaymentMethod getMethod();
}
