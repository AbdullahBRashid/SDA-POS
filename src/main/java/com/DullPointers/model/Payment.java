package com.DullPointers.model;

import com.DullPointers.model.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private BigDecimal amount;
    private PaymentMethod method;
    private LocalDateTime timestamp;
    private String referenceId; // E.g., "VISA-1234"

    public Payment() {}

    public Payment(BigDecimal amount, PaymentMethod method, String referenceId) {
        this.amount = amount;
        this.method = method;
        this.referenceId = referenceId;
        this.timestamp = LocalDateTime.now();
    }

    public BigDecimal getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
}