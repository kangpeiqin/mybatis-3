package org.apache.ibatis.learning;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author kpq
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class User {
  private String name;
}
