/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.fluo.core.log;

import java.util.Iterator;

import com.google.common.collect.Iterators;
import org.apache.fluo.api.client.scanner.ColumnScanner;
import org.apache.fluo.api.client.scanner.RowScanner;

public class TracingRowScanner implements RowScanner {
  private final RowScanner wrappedScanner;
  private final long txid;
  private final String scanId;

  TracingRowScanner(RowScanner wrappedScanner, long txid, String scanId) {
    this.wrappedScanner = wrappedScanner;
    this.txid = txid;
    this.scanId = scanId;
  }

  @Override
  public Iterator<ColumnScanner> iterator() {
    return Iterators.transform(wrappedScanner.iterator(), cs -> {
      return new TracingColumnScanner(cs, txid, scanId);
    });
  }

}
