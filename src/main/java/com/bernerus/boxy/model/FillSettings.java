package com.bernerus.boxy.model;

import lombok.Builder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.lang.NonNull;

@Builder
public record FillSettings(double fillFactor, int randomFillAttempts) {
    public boolean tryRandomFill() {
        return randomFillAttempts > 0;
    }

    @NonNull
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("fillFactor", fillFactor)
                .append("randomFillAttempts", randomFillAttempts)
                .toString();
    }
}
