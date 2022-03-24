/*
 *    Copyright 2009-2021 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor.resultset;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;

/**
 * ResultSetHandler用于在StatementHandler对象执行完查询操作或存储过程后，
 * 对结果集或存储过程的执行结果进行处理。
 * 查询结果集处理
 *
 * @author Clinton Begin
 */
public interface ResultSetHandler {

  /**
   * 获取Statement对象中的ResultSet对象，对ResultSet对象进行处理，返回包含结果实体的List对象
   *
   * @param stmt {@link Statement}
   * @param <E>  类型参数
   * @return List
   * @throws SQLException SQLException
   */
  <E> List<E> handleResultSets(Statement stmt) throws SQLException;

  <E> Cursor<E> handleCursorResultSets(Statement stmt) throws SQLException;

  void handleOutputParameters(CallableStatement cs) throws SQLException;

}
