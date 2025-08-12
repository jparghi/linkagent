package com.linkagent.demo.connectors;

import java.util.Map;

public interface Connector {
  String name();
  boolean supportsIntent(String intent);
  Map<String,Object> execute(String intent, Map<String,Object> params);
}
