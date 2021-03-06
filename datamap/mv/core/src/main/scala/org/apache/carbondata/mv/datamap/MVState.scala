/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.carbondata.mv.datamap

import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

import org.apache.carbondata.mv.plans.modular.SimpleModularizer
import org.apache.carbondata.mv.plans.util.BirdcageOptimizer
import org.apache.carbondata.mv.rewrite.{DefaultMatchMaker, Navigator, QueryRewrite, SummaryDatasetCatalog}

/**
 * A class that holds all session-specific state.
 */
private[mv] class MVState(summaryDatasetCatalog: SummaryDatasetCatalog) {

  // Note: These are all lazy vals because they depend on each other (e.g. conf) and we
  // want subclasses to override some of the fields. Otherwise, we would get a lot of NPEs.

  /**
   * Modular query plan modularizer
   */
  lazy val modularizer = SimpleModularizer

  /**
   * Logical query plan optimizer.
   */
  lazy val optimizer = BirdcageOptimizer

  lazy val matcher = DefaultMatchMaker

  lazy val navigator: Navigator = new Navigator(summaryDatasetCatalog, this)

  /**
   * Rewrite the logical query plan to MV plan if applicable.
   * @param plan
   * @return
   */
  def rewritePlan(plan: LogicalPlan): QueryRewrite = new QueryRewrite(this, plan)

}
