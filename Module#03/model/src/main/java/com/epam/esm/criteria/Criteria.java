package com.epam.esm.criteria;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.swing.SortOrder;
import java.util.EnumMap;

@Data
@Builder
@RequiredArgsConstructor
public class Criteria {

    private final SortOrder sortOrder;
    private final FilterParams filterParams;
    @Size(min = 1, max = 1000, message = "Size must be between 1 and 1000")
    private final Integer size;
    @Size(min = 1, max = 1000, message = "Size must be between 1 and 1000")
    private final Integer page;
    private final EnumMap<FilterParams, Object> paramsMap = new EnumMap<>(FilterParams.class);

    public void addParam(final Object... params) {
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof FilterParams) {
                paramsMap.put((FilterParams) params[i], params[i + 1]);
            }
        }
    }
}
