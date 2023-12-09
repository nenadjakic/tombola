package com.github.nenadjakic.tombola.util;

import java.util.Map;

public interface Observer {
    void update(Map<String, Object> properties);
}
