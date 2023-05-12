package com.epam.esm.criteria;

import com.epam.esm.entity.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.swing.SortOrder;
import java.util.EnumMap;
import java.util.Set;

@Data
@Builder
@RequiredArgsConstructor
public class Criteria {
    private final Set<Tag> tags;
    private final SortOrder sortOrder;
    private final FilterParams filterParams;
    private final Integer size;
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
